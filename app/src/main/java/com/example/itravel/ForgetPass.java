package com.example.itravel;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        mAuth = FirebaseAuth.getInstance();
    }

    public void sendVerificationCodeToEmail(View view) {
        EditText emailEditText = findViewById(R.id.gmail);
        String email = emailEditText.getText().toString().trim();

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ForgetPass.this, "Verification code sent to your email.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgetPass.this, "Failed to send verification code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void resetPassword(View view) {
        EditText codeEditText = findViewById(R.id.code);
        EditText passwordEditText = findViewById(R.id.password);

        String code = codeEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();

        try {
            mAuth.checkActionCode(code);
            mAuth.getCurrentUser().updatePassword(newPassword)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ForgetPass.this, "Password successfully updated.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgetPass.this, "Failed to update password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Invalid verification code.", Toast.LENGTH_SHORT).show();
        }
    }
}
