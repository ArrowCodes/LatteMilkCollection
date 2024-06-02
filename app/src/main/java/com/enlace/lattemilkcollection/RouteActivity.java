package com.enlace.lattemilkcollection;

import static com.enlace.lattemilkcollection.UUIDGenerator.generate12CharUUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class RouteActivity extends AppCompatActivity {
   FloatingActionButton fab;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private RouteListAdapter routeListAdapter;
    private List<RouteList> listItems;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        getSupportActionBar().setTitle("Routes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressbar = findViewById(R.id.progressbar);
        //initialize databases
        dbHelper = new DatabaseHelper(this);
        //Reyclerview initiate
        listItems= new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(RouteActivity.this));
        recyclerView.setHasFixedSize(true);
        loadRecyclerView();
        //initialize floating action button
        fab = findViewById(R.id.fab);
        //floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inflate the custom layout
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_route_layout, null);
                EditText editText = dialogView.findViewById(R.id.routeT);

                // Create the Sweet Alert Dialog
                new SweetAlertDialog(RouteActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Add Route.")
                        .setCustomView(dialogView)
                        .setConfirmText("OK")
                        .setCancelText("Cancel")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // Handle confirm button click
                                String inputText = editText.getText().toString().toUpperCase();
                                post_route(inputText);
                                // Do something with the input text
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // Handle cancel button click
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }

    private void post_route(final String route)
    {
        // Generate a 12-character UUID
        String sync_key = generate12CharUUID();
        String user_name = SharedPrefManager.getInstance(getApplicationContext()).getKeyUserName();
        dbHelper.insertRoutes(route,sync_key,user_name);
        Toasty.success(getApplicationContext(),"Route saved successfully",Toast.LENGTH_LONG,true).show();
        add_route(route,sync_key,user_name);
    }
    private void add_route(final String route,final String sync_key,final String user_name)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApi.ADD_ROUTE, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if(!obj.getBoolean("error"))
                {
                    loadRecyclerView();
                    Toasty.success(getApplicationContext(),"Route added successfully.", Toast.LENGTH_LONG,true).show();
                }
                else
                {

                }
            } catch (JSONException e) {
                System.out.println("API Error:"+e.getMessage());
                throw new RuntimeException(e);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               System.out.println("API Error:"+error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                params.put("route_name",route);
                params.put("sync_key",sync_key);
                params.put("user_name",user_name);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void loadRecyclerView()
    {

        progressbar.setVisibility(View.VISIBLE);
        listItems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApi.GET_ROUTES, response -> {
            progressbar.setVisibility(View.GONE);
            Log.d("Request Tag",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("routes");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    RouteList item = new RouteList(o.getString("route_id"),o.getString("route_name"),o.getString("user_name"));
                    listItems.add(item);
                }
                routeListAdapter = new RouteListAdapter(listItems,getApplicationContext());
                recyclerView.setAdapter(routeListAdapter);

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
                //params.put("rider_id",SharedPrefManager.getInstance(MyRidesActivity.this).getKeyUserId());
                return params;
            }
        };
        RequestHandler.getInstance(RouteActivity.this).addToRequestQueue(stringRequest);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}