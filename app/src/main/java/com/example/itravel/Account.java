package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

    }

    public void Info(View view){
        Intent intent = new Intent(this, AccountInfo.class);
        startActivity(intent);
    }
    public void Del(View view){
        Intent intent = new Intent(this, Dell.class);
        startActivity(intent);
    }
}