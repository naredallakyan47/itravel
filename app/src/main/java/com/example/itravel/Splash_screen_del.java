package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_screen_del extends AppCompatActivity {

    private EditText usernameEditText;
    private CheckBox confirmCheckBox;

    private FirebaseAuth mAuth;
    public static boolean deleted = false;
    String deletionConfirmation = "I am deleting my account";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_del);

        usernameEditText = findViewById(R.id.usernameEditText);

    }

    public void confirmDeletion(View view) {
        String username = usernameEditText.getText().toString().trim();
        if (username.equals(deletionConfirmation)) {
            Intent intent = new Intent(this, Dell.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please write this text correctly", Toast.LENGTH_SHORT).show();
        }
    }

}

