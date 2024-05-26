package com.enlace.lattemilkcollection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {

    String searchString = "";
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<RouteList> listItems;
    private Context context;
    private OnMenuClickListener mOnMenuClickListener;

    public RouteListAdapter(List<RouteList> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    public interface OnMenuClickListener {
        void OnClickMenu(View view, int position);
    }

    public void setmOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final RouteList listItem = listItems.get(position);
        Random mRandom = new Random();
        int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        ((GradientDrawable) holder.imageView.getBackground()).setColor(color);

        holder.imageView.setText(listItem.getRoute_name().substring(0, 1).toUpperCase());
        holder.route_nameTV.setText(listItem.getRoute_name());
        holder.user_nameTV.setText("Route By:" + listItem.getUser_name());
        //holder.fareTV.setText("Fare:KES " + listItem.getTotal_fare());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnMenuClickListener.OnClickMenu(view, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView imageView, route_nameTV, user_nameTV;
        public ImageView buttonViewOption;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            route_nameTV = itemView.findViewById(R.id.route_nameTV);
            user_nameTV = itemView.findViewById(R.id.user_nameTV);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }

        /*ImageView getDeleteTripIV() {
           // return cancel_tripIV;
        }*/

    }

    public static void setFilter(List<RouteList> listItems) {
        listItems = new ArrayList<>();
        listItems.addAll(listItems);

    }

    public void update(List<RouteList> listItems) {
        listItems.clear();
        listItems.addAll(listItems);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<RouteList> listItems) {
        this.listItems = listItems;
    }

    public void notify(ArrayList<RouteList> list) {
        if (listItems != null) {
            listItems.clear();
            listItems.addAll(list);

        } else {
            listItems = list;
        }
        notifyDataSetChanged();
    }

    public void updateData(List<RouteList> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }
}


