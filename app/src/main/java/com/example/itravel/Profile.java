package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        } else {

            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);
            finish();
        }
    }

    public void settings(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
    public void map(View v) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }


    public void home(View v) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}

