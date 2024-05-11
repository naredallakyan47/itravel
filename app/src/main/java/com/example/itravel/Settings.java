package com.example.itravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView emailTextView;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        emailTextView = findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        TextView usernameTextView = findViewById(R.id.username);
        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            if (username != null && !username.isEmpty()) {
                usernameTextView.setText(username);
            } else {
                usernameTextView.setText("Username not available");
            }
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            if (username != null) {
                usernameTextView.setText(username);
            }
            if (email != null) {
                emailTextView.setText(email);
            }
        }

    }

    public void Del(View view){
        Intent intent = new Intent(this, Splash_screen_del.class);
        startActivity(intent);
    }



    public void Home(View view){
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    public void map(View view){
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }



    public void Log_out(View view) {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }






}