package com.enlace.lattemilkcollection;

import static com.enlace.lattemilkcollection.UUIDGenerator.generate12CharUUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appz.abhi.simplebutton.SimpleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AddFarmersActivity extends AppCompatActivity {
    private EditText nameT, pnumberT, rate_per_litreT, routeT, account_numberT, mpesa_numberT, pay_dayT;
    private TextView account_numberTV, mpesa_numberTV;
    private SimpleButton saveB;
    private RadioGroup radioGroupPaymentMode;
    private String payment_mode,route_id,route_name;
    private RadioButton radioButton;
    private DatabaseHelper dbHelper;
    private SpinnerDialog spinnerDialogRoutes;
    private ArrayList<String> route_ids;
    private ArrayList<String> route_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farmers);
        getSupportActionBar().setTitle("Add Farmers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize array list
        route_ids = new ArrayList<String>();
        route_names = new ArrayList<String>();
        //initialize databases
        dbHelper = new DatabaseHelper(this);
        //load routes
        loadRoutes();
        //initialize views
        nameT = findViewById(R.id.nameT);
        pnumberT = findViewById(R.id.pnumberT);
        rate_per_litreT = findViewById(R.id.rate_per_litreT);
        routeT = findViewById(R.id.routeT);
        routeT.setInputType(InputType.TYPE_NULL);
        account_numberT = findViewById(R.id.account_numberT);
        account_numberT.setText("Not Defined");
        account_numberT.setEnabled(false);
        mpesa_numberT = findViewById(R.id.mpesa_numberT);
        mpesa_numberT.setText("Not Defined");
        mpesa_numberT.setEnabled(false);
        pay_dayT = findViewById(R.id.pay_dayT);
        account_numberTV = findViewById(R.id.account_numberTV);
        mpesa_numberTV = findViewById(R.id.mpesa_numberTV);
        radioGroupPaymentMode = findViewById(R.id.radioGroupPaymentMode);
        saveB = findViewById(R.id.saveB);
        //spinner dialogs
        spinnerDialogRoutes = new SpinnerDialog(AddFarmersActivity.this, route_names, "SELECT ROUTE", R.style.AppTheme, "Close");
        spinnerDialogRoutes.setCancellable(true);
        spinnerDialogRoutes.bindOnSpinerListener((item, position) -> {
            routeT.setText(item);
            route_id = route_ids.get(position);
            Toasty.info(getApplicationContext(),route_id, Toast.LENGTH_LONG,false).show();
        });
        //activate spinner
        routeT.setOnClickListener(view -> spinnerDialogRoutes.showSpinerDialog());
        //actions
        radioGroupPaymentMode.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = findViewById(checkedId);
            payment_mode = radioButton.getText().toString().trim();
            if (payment_mode.equals("Cash")) {
                account_numberTV.setVisibility(View.GONE);
                account_numberT.setVisibility(View.GONE);
                mpesa_numberTV.setVisibility(View.GONE);
                mpesa_numberT.setVisibility(View.GONE);

            } else if (payment_mode.equals("Mpesa")) {
                account_numberTV.setVisibility(View.GONE);
                account_numberT.setVisibility(View.GONE);
                mpesa_numberTV.setVisibility(View.VISIBLE);
                mpesa_numberT.setVisibility(View.VISIBLE);
                mpesa_numberT.setEnabled(true);
                mpesa_numberT.setError("Mpesa number is required.");
                mpesa_numberT.requestFocus();
                //mpesa_number = mpesa_numberT.getText().toString().trim();
            } else if (payment_mode.equals("Bank")) {
                account_numberTV.setVisibility(View.VISIBLE);
                account_numberT.setVisibility(View.VISIBLE);
                mpesa_numberTV.setVisibility(View.GONE);
                mpesa_numberT.setVisibility(View.GONE);
                account_numberT.setEnabled(true);
                account_numberT.setError("Add account number required.");
                account_numberT.requestFocus();
                //account = account_numberT.getText().toString().trim();
            }
            Toast.makeText(getApplicationContext(), "Selected: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
        });

        //save Button
        saveB.setOnClickListener(view -> {
            if(payment_mode==null)
            {
                Toasty.warning(getApplicationContext(),"Payment mode must be selected",Toast.LENGTH_LONG,true).show();
            }
            else if(nameT.getText().toString().trim().equals(""))
            {
                nameT.setError("This field is required");
                nameT.requestFocus();
            }
            else if(rate_per_litreT.getText().toString().trim().equals(""))
            {
                rate_per_litreT.setError("This field is required");
                rate_per_litreT.requestFocus();
            }
            else if(routeT.getText().toString().trim().equals(""))
            {
                routeT.setError("This field is required");
                routeT.requestFocus();
            }
            else if(pay_dayT.getText().toString().trim().equals(""))
            {
                pay_dayT.setError("This field is required");
                pay_dayT.requestFocus();
            }
            else if(account_numberT.getText().toString().trim().equals(""))
            {
                account_numberT.setError("This field is required");
                account_numberT.requestFocus();
            }
            else if(mpesa_numberT.getText().toString().trim().equals(""))
            {
                mpesa_numberT.setError("This field is required.");
                mpesa_numberT.requestFocus();
            }
            else
            {
                String sync_key = generate12CharUUID();
                final String name = nameT.getText().toString().trim().toUpperCase();
                final String lat = SharedPrefManager.getInstance(getApplicationContext()).getDeviceLat();
                final String lng = SharedPrefManager.getInstance(getApplicationContext()).getDeviceLng();
                final String rate_per_litre = rate_per_litreT.getText().toString().trim();
                final String pay_day = pay_dayT.getText().toString().trim();
                final String pnumber = pnumberT.getText().toString().trim();
                final String account = account_numberT.getText().toString().trim();
                final String mpesa_number = mpesa_numberT.getText().toString().trim();
                final String username = SharedPrefManager.getInstance(getApplicationContext()).getKeyUserName();
                dbHelper.insertFarmers(name,lat,lng,rate_per_litre,payment_mode,account,pay_day,pnumber,mpesa_number,route_id,sync_key,username);
                Toasty.success(getApplicationContext(),"Farmer added successfully",Toast.LENGTH_LONG,false).show();
                save_farmer(name,lat,lng,rate_per_litre,payment_mode,account,pay_day,pnumber,mpesa_number,route_id,sync_key,username);
                //dbHelper.insertFarmers()
            }
        });

    }

    private void save_farmer(final String name,final String lat,final String lng,final String rate_per_litre,final String payment_mode,final String account,final String pay_day,final String pnumber,final String mpesa_number,final String route_id,final String sync_key,final String user_name)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApi.SAVE_FARMER, response -> {
            try {
                JSONObject obj = new JSONObject(response);
                if(!obj.getBoolean("error"))
                {
                    nameT.setText("");
                    pnumberT.setText("");
                    rate_per_litreT.setText("");
                    routeT.setText("");
                    account_numberT.setText("");
                    mpesa_numberT.setText("");
                    pay_dayT.setText("");
                   Toasty.success(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG,true).show();
                }
                else
                {
                    Toasty.error(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG,true).show();
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
                params.put("name",name);
                params.put("lat",lat);
                params.put("lng",lng);
                params.put("rate_per_litre",rate_per_litre);
                params.put("payment_mode",payment_mode);
                params.put("account",account);
                params.put("pay_day",pay_day);
                params.put("pnumber",pnumber);
                params.put("mpesa_number",mpesa_number);
                params.put("route",route_id);
                params.put("sync_key",sync_key);
                params.put("user_name",user_name);
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void loadRoutes() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApi.GET_ROUTES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    for(int i=0;i<array.length();i++) {
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
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params = new HashMap<>();
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), FarmersActivity.class);
            startActivity(i);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}