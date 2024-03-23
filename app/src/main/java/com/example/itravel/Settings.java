package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        if (intent != null) {
            Login.GuestMode = intent.getBooleanExtra("GuestMode", false);
        }
    }

    public void Account(View view){
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }

    public void Lang(View view){
        Intent intent = new Intent(this, Language.class);
        startActivity(intent);
    }

    public void Log_out(View view) {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(this, Login.class);
        intent.putExtra("GuestMode", Login.GuestMode);
        startActivity(intent);
        finish();
    }




}