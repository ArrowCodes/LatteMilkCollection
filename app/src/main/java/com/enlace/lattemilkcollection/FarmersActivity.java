package com.enlace.lattemilkcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FarmersActivity extends AppCompatActivity {
   FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers);
        getSupportActionBar().setTitle("Farmers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initializa fab
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),AddFarmersActivity.class);
            startActivity(i);
        });
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