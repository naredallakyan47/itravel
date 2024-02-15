package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void Register(View v) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }


    public void main(View v) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}