package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Top_places extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_places);
    }


    public void home(View v) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    public void map(View v) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void like(View v) {
        Intent intent = new Intent(this, Likes.class);
        startActivity(intent);
    }

    public void Profile(View v) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}