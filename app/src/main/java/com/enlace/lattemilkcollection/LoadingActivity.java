package com.enlace.lattemilkcollection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.codeshuffle.typewriterview.TypeWriterView;

public class LoadingActivity extends AppCompatActivity {
    private String name,email;
    CatLoadingView mView;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private DatabaseHelper dbHelper;
    //private MFDBHelper mfdbHelper;
    SQLiteDatabase db,mfdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getSupportActionBar().hide();

        //initialize databases
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

       /* mfdbHelper = new MFDBHelper(this);
        mfdb = mfdbHelper.getWritableDatabase();*/

        // Insert dummy data to ensure database is created
        //dbHelper.insertCategoryType("01", "P", "CLIENT");
        //Create Object and refer to layout view
        TypeWriterView typeWriterView=(TypeWriterView)findViewById(R.id.typeWriterView);

        //Setting each character animation delay
        typeWriterView.setDelay(1);

        //Setting music effect On/Off
        typeWriterView.setWithMusic(false);

        //Animating Text
        typeWriterView.animateText("Welcome to Latte Milk Collector.");

        // Using handler with postDelayed called runnable run method
        new Handler().postDelayed(() -> {

            Intent i = new Intent(LoadingActivity.this,LoginActivity.class);

            startActivity(i);

            // close this activity

            finish();

        }, 5*1000); // wait for 8 seconds

        load_users_client();
        //load_routes();
    }
    //load users client
    private void load_users_client()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApi.GET_USERS_CLIENT, response -> {
            //progressbar.setVisibility(View.GONE);
            Log.d("Request Tag",response);
            //Toasty.info(getApplicationContext(),response, Toast.LENGTH_LONG,false).show();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("users_client");
                for(int i=0;i<array.length();i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    dbHelper.insertUsersClient(o.getString("user_name"), o.getString("user_code"),o.getString("client_id"),o.getString("level"),o.getString("firstname"),o.getString("middlename"),o.getString("surname"),o.getString("email"));
                    //Toasty.success(getApplicationContext(),"Category Types Added Successfully",Toast.LENGTH_LONG,false).show();

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
                //params.put("rider_id",SharedPrefManager.getInstance(MyRidesActivity.this).getKeyUserId());
                return params;
            }
        };
        RequestHandler.getInstance(LoadingActivity.this).addToRequestQueue(stringRequest);
    }
}