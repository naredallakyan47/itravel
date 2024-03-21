package com.example.itravel;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatActivity extends AppCompatActivity {

    private EditText editTextUser;
    private Button buttonSearch;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mAuth = FirebaseAuth.getInstance();

        editTextUser = findViewById(R.id.editTextUser);
        buttonSearch = findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUser.getText().toString();
                searchUser(username);
            }
        });

        // Назначить слушатель нажатия на LinearLayout, чтобы открывать новую активность
        LinearLayout infoLayout = findViewById(R.id.info);
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUser.getText().toString();
                openMassageChatActivity(username);
            }
        });
    }

    private void searchUser(String username) {
        FirebaseUser user = mAuth.getCurrentUser();
        TextView textViewResult = findViewById(R.id.user);
        if (user != null) {
            if (username.equals(user.getDisplayName())) {
                textViewResult.setText("User found: " + username);
            } else {
                Toast.makeText(ChatActivity.this, "User not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChatActivity.this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMassageChatActivity(String username) {
        Intent intent = new Intent(this, massage_chat.class);
        intent.putExtra("USERNAME", username); // передача имени пользователя через Intent
        startActivity(intent);
    }

    public void message(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}




