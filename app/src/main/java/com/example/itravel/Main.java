package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onImageClick(View v) {
        
        Intent intent = new Intent(this, Information.class);
        startActivity(intent);
    }

    public void map(View v) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }
}