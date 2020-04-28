package com.example.freshapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGetButtonClick(View v) {
        Intent GetIntent = new Intent(getApplicationContext(), Getdata.class);
        startActivity(GetIntent);
    }

    public void onPussButtonClick(View v) {
        Intent PostIntent = new Intent(getApplicationContext(), Postdata.class);
        startActivity(PostIntent);
    }
}
