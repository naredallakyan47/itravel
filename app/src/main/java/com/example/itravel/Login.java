package com.example.itravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    static Boolean GuestMode = false;
    private static final String TAG = "LoginActivity";

    EditText emailEditText;
    TextView error;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.gmail);
        passwordEditText = findViewById(R.id.password);
        error = findViewById(R.id.error);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        boolean isGuestMode = sharedPreferences.getBoolean("isGuestMode", false);

        if (isLoggedIn ) {
            startActivity(new Intent(this, Main.class));
            finish();
        } else {

        }


        Intent intent = getIntent();
        if (intent != null) {
            Login.GuestMode = intent.getBooleanExtra("GuestMode", false);
        }
    }

    public void signIn(View v) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                Intent intent = new Intent(Login.this, Main.class);
                                startActivity(intent);
                            } else if (user != null && !user.isEmailVerified()) {
                                error.setText("Please verify your email before logging in");
                            }
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            error.setText("Authentication failed: " + task.getException().getMessage());
                        }
                    }
                });
    }


    public void Register(View v) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void ForgetPass(View v) {
        Intent intent = new Intent(this, ForgetPass.class);
        startActivity(intent);
    }


}







