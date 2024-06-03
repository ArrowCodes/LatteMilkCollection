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

public class SalesListAdapter extends RecyclerView.Adapter<SalesListAdapter.ViewHolder> {

    String searchString = "";
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<SalesList> listItems;
    private Context context;
    private OnMenuClickListener mOnMenuClickListener;

    public SalesListAdapter(List<SalesList> listItems, Context context) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_list, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SalesList listItem = listItems.get(position);
        Random mRandom = new Random();
        int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        ((GradientDrawable) holder.imageView.getBackground()).setColor(color);

        holder.imageView.setText(listItem.getName().substring(0, 1).toUpperCase());
        holder.nameTV.setText(listItem.getName());
        holder.litresTV.setText("Litres:"+listItem.getLitres());
        holder.amountTV.setText("Kshs:" + listItem.getAmount_to_pay());
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

        public TextView imageView, nameTV,litresTV,amountTV;
        public ImageView buttonViewOption;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTV = itemView.findViewById(R.id.nameTV);
            litresTV = itemView.findViewById(R.id.litresTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            linearLayout = itemView.findViewById(R.id.linearLayout);

        }

        /*ImageView getDeleteTripIV() {
           // return cancel_tripIV;
        }*/

    }

    public static void setFilter(List<SalesList> listItems) {
        listItems = new ArrayList<>();
        listItems.addAll(listItems);

    }

    public void update(List<SalesList> listItems) {
        listItems.clear();
        listItems.addAll(listItems);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<SalesList> listItems) {
        this.listItems = listItems;
    }

    public void notify(ArrayList<SalesList> list) {
        if (listItems != null) {
            listItems.clear();
            listItems.addAll(list);

        } else {
            listItems = list;
        }
        notifyDataSetChanged();
    }

    public void updateData(List<SalesList> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }
}


