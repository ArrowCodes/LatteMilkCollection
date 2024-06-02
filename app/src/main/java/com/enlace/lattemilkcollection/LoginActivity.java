package com.enlace.lattemilkcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appz.abhi.simplebutton.SimpleButton;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private EditText user_nameT,user_codeT;
    private SimpleButton loginB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
            finish();
        }

        //initialize view
        user_nameT = findViewById(R.id.user_nameT);
        user_codeT = findViewById(R.id.user_codeT);
        loginB = findViewById(R.id.loginB);
        //insert action
        loginB.setOnClickListener(view -> {
            if(user_nameT.getText().toString().equals(""))
            {
                user_nameT.setError("This field is required.");
                user_nameT.requestFocus();
            }
            else if(user_codeT.getText().toString().equals(""))
            {
                user_codeT.setError("This field is required.");
                user_codeT.requestFocus();
            }
            else
            {
                final String user_name = user_nameT.getText().toString().trim();
                final String user_code = user_codeT.getText().toString().trim();
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                boolean loginSuccess = dbHelper.loginUser(user_name, user_code, getApplicationContext(), MainActivity.class);

                if (loginSuccess) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    Toasty.success(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT,true).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}