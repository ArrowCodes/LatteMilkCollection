package com.enlace.lattemilkcollection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class BuyActivity extends AppCompatActivity {
    private CircleButton search_route_button;
    private EditText routeT;
    private SpinnerDialog spinnerDialogRoutes;
    private ArrayList<String> route_ids;
    private ArrayList<String> route_names;
    private DatabaseHelper dbHelper;
    private String route_id;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private FarmersListAdapter farmersListAdapter;
    private List<FarmersList> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        getSupportActionBar().setTitle("Milk Collection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recyclerview inits
        listItems= new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(BuyActivity.this));
        recyclerView.setHasFixedSize(true);
        //initialize array list
        route_ids = new ArrayList<String>();
        route_names = new ArrayList<String>();
        //initialize databases
        dbHelper = new DatabaseHelper(this);
        //load routes
        loadRoutes();
        //initialize views
        progressbar = findViewById(R.id.progressbar);
        routeT = findViewById(R.id.routeT);
        routeT.setInputType(InputType.TYPE_NULL);
        search_route_button = findViewById(R.id.search_route_button);
        //spinner dialogs
        spinnerDialogRoutes = new SpinnerDialog(BuyActivity.this, route_names, "SELECT ROUTE", R.style.AppTheme, "Close");
        spinnerDialogRoutes.setCancellable(true);
        spinnerDialogRoutes.bindOnSpinerListener((item, position) -> {
            routeT.setText(item);
            route_id = route_ids.get(position);
            farmers_by_route(route_id);
            //Toasty.info(getApplicationContext(), route_id, Toast.LENGTH_LONG, false).show();
        });
        //activate spinner
        search_route_button.setOnClickListener(view -> spinnerDialogRoutes.showSpinerDialog());
    }

    private void loadRoutes() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApi.GET_ROUTES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        route_names.add(o.getString("route_name"));
                        route_ids.add(o.getString("route_id"));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void farmers_by_route(final String route)
    {

        progressbar.setVisibility(View.VISIBLE);
        listItems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApi.FARMERS_BY_ROUTE, response -> {
            progressbar.setVisibility(View.GONE);
            Log.d("Request Tag",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("farmers");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    FarmersList item = new FarmersList(o.getString("farmer_id"),o.getString("name"),o.getString("lat"),o.getString("lng"),o.getString("rate_per_litre"),o.getString("pnumber"),o.getString("route_id"),o.getString("route_name"),o.getString("username"));
                    listItems.add(item);
                }
                farmersListAdapter = new FarmersListAdapter(listItems,getApplicationContext());
                recyclerView.setAdapter(farmersListAdapter);
                farmersListAdapter.setmOnMenuClickListener((view, position) -> {
                    // Inflate the custom layout
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.milk_collection, null);
                    EditText editText = dialogView.findViewById(R.id.litresT);

                    // Create the Sweet Alert Dialog
                    new SweetAlertDialog(BuyActivity.this, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText(listItems.get(position).getName())
                            .setCustomView(dialogView)
                            .setConfirmText("Confirm Collection")
                            .setCancelText("Cancel")
                            .setConfirmClickListener(sDialog -> {
                                // Handle confirm button click
                                String litres = editText.getText().toString().toUpperCase();
                                String rate_per_litre = listItems.get(position).getRate_per_litre();
                                String farmer_id = listItems.get(position).getFarmer_id();
                                final Double amount_to_pay = Integer.parseInt(litres) * Double.parseDouble(listItems.get(position).getRate_per_litre());
                                //Toasty.success(getApplicationContext(),String.valueOf(amount_to_pay),Toast.LENGTH_LONG,true).show();
                                //post_route(inputText);
                                // Do something with the input text
                                post_collection(farmer_id,rate_per_litre,litres,String.valueOf(amount_to_pay));
                                sDialog.dismissWithAnimation();
                            })
                            .setCancelClickListener(sDialog -> {
                                // Handle cancel button click
                                sDialog.dismissWithAnimation();
                            })
                            .show();
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbar.setVisibility(View.GONE);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("route",route);
                return params;
            }
        };
        RequestHandler.getInstance(BuyActivity.this).addToRequestQueue(stringRequest);
    }

    private void post_collection(final String farmer_id,final String rate_per_litre,final String litres,final String amount_to_pay)
    {
        Calendar calendar = Calendar.getInstance();
        GetTimeData getTimeData = new GetTimeData(calendar);
        final String text_date = getTimeData.getTextDate();
        final String sysTime = getTimeData.getHour();
        final String sysDate = getTimeData.getSysDate();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApi.POST_COLLECTION, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if(!obj.getBoolean("error"))
                {
                    Toasty.success(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG,true).show();
                }
                else
                {
                    Toasty.warning(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG,true).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("farmer_id",farmer_id);
                params.put("rate_per_litre",rate_per_litre);
                params.put("litres",litres);
                params.put("amount_to_pay",amount_to_pay);
                params.put("sys_date",sysDate);
                params.put("sys_time",sysTime);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}