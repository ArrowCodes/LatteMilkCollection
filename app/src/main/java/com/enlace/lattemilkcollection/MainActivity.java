package com.enlace.lattemilkcollection;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TextView name, email;
    EasyWayLocation easyWayLocation;
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String lat,lng;
    private TextView greetings_tv,amount_tv,litres_tv;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private SalesListAdapter salesListAdapter;
    private List<SalesList> listItems;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Latte");
        setSupportActionBar(toolbar);

        //initialize views
        greetings_tv = findViewById(R.id.greetings_tv);
        amount_tv = findViewById(R.id.amount_tv);
        litres_tv = findViewById(R.id.litres_tv);
        progressbar = findViewById(R.id.progressbar);
        //greetings
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            greetings_tv.setText("GOOD MORNING,"+SharedPrefManager.getInstance(getApplicationContext()).getKeyUserFname());
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetings_tv.setText("GOOD AFTERNOON,"+SharedPrefManager.getInstance(getApplicationContext()).getKeyUserFname());
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greetings_tv.setText("GOOD EVENING,"+SharedPrefManager.getInstance(getApplicationContext()).getKeyUserFname());
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            greetings_tv.setText("BLESSED NIGHT,"+SharedPrefManager.getInstance(getApplicationContext()).getKeyUserFname());
        }

        listItems= new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);
        loadRecyclerView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        name = (TextView) header.findViewById(R.id.header_title);
        name.setText(SharedPrefManager.getInstance(getApplicationContext()).getKeyUserFname()+" "+SharedPrefManager.getInstance(getApplicationContext()).getKeyUserLname());
        email = header.findViewById(R.id.sub_title);
        email.setText(SharedPrefManager.getInstance(getApplicationContext()).getKeyUserName());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION_CODE);
        } else {
            getLastLocation();
        }
        summary();

    }

    private void loadRecyclerView()
    {

        progressbar.setVisibility(View.VISIBLE);
        listItems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApi.SALES_BY_USERNAME, response -> {
            progressbar.setVisibility(View.GONE);
            Log.d("Request Tag",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("sales");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    SalesList item = new SalesList(o.getString("id"),o.getString("farmer_id"),o.getString("name"),o.getString("litres"),o.getString("amount_to_pay"));
                    listItems.add(item);
                }
                salesListAdapter= new SalesListAdapter(listItems,getApplicationContext());
                recyclerView.setAdapter(salesListAdapter);
                salesListAdapter.setmOnMenuClickListener(new SalesListAdapter.OnMenuClickListener() {
                    @Override
                    public void OnClickMenu(View view, int position) {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Are you sure you want to delete this sale?")
                                .setConfirmText("Yes, delete it!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog
                                                .setTitleText("Deleted!")
                                                .setContentText("Your file has been deleted!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        // Handle the confirmed action here
                                        delete_sale(listItems.get(position).getId());
                                    }
                                })
                                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
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
                params.put("username",SharedPrefManager.getInstance(MainActivity.this).getKeyUserName());
                return params;
            }
        };
        RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }
    private void delete_sale(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApi.DELETE_SALE, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if(!obj.getBoolean("error"))
                {
                    loadRecyclerView();
                }
                else
                {

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
                params.put("id",id);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void summary()
    {
        Calendar calendar = Calendar.getInstance();
        GetTimeData getTimeData = new GetTimeData(calendar);
        final String text_date = getTimeData.getTextDate();
        final String sysTime = getTimeData.getHour();
        final String sysDate = getTimeData.getSysDate();
        @SuppressLint("SetTextI18n") StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApi.SUMMARY, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                amount_tv.setText("Collection(Kshs):"+obj.getString("amount_payable"));
                litres_tv.setText("Collection(Litres):"+obj.getString("litres"));
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
                params.put("username",SharedPrefManager.getInstance(getApplicationContext()).getKeyUserName());
                params.put("sys_date",sysDate);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    String locationString = "Lat: " + location.getLatitude() + " , Long: " + location.getLongitude();
                    SharedPrefManager.getInstance(getApplicationContext()).saveDeviceLatitude(String.valueOf(location.getLatitude()));
                    SharedPrefManager.getInstance(getApplicationContext()).saveDeviceLng(String.valueOf(location.getLongitude()));
                } else {
                    Toast.makeText(MainActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_routes) {
            Intent i = new Intent(getApplicationContext(), RouteActivity.class);
            startActivity(i);
        }  else if (id == R.id.nav_farmers) {
            Intent i = new Intent(getApplicationContext(), FarmersActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_buy) {
            Intent i = new Intent(getApplicationContext(), BuyActivity.class);
            startActivity(i);
        }
        else if(id == R.id.nav_logout)
        {
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}