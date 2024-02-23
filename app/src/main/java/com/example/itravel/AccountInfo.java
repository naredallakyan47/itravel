package com.example.itravel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountInfo extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);

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
}
