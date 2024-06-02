package com.enlace.lattemilkcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.roger.catloadinglibrary.CatLoadingView;

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
    }
}