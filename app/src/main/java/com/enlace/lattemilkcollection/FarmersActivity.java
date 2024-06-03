package com.enlace.lattemilkcollection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

public class FarmersActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private ProgressBar progressbar;
    private RecyclerView recyclerView;
    private FarmersListAdapter farmersListAdapter;
    private List<FarmersList> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers);
        getSupportActionBar().setTitle("Farmers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressbar = findViewById(R.id.progressbar);
        //initialize floating action button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddFarmersActivity.class);
                startActivity(i);
                finish();
            }
        });
        listItems= new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(FarmersActivity.this));
        recyclerView.setHasFixedSize(true);
        loadRecyclerView();

    }
    private void loadRecyclerView()
    {

        progressbar.setVisibility(View.VISIBLE);
        listItems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApi.LOAD_FARMERS, response -> {
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
        RequestHandler.getInstance(FarmersActivity.this).addToRequestQueue(stringRequest);
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