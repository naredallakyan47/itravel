package com.example.itravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Settings extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView emailTextView;
    private ImageView profileImageView;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        emailTextView = findViewById(R.id.email);
        profileImageView = findViewById(R.id.profile_img);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        TextView usernameTextView = findViewById(R.id.username);
        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            if (username != null && !username.isEmpty()) {
                usernameTextView.setText(username);

                // Проверяем, есть ли у пользователя фотография в базе данных
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(username);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("img")) {
                            String imgUri = dataSnapshot.child("img").getValue(String.class);
                            if (imgUri != null && !imgUri.isEmpty()) {
                                // Загружаем и устанавливаем фотографию
                                loadAndSetProfileImage(imgUri);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Обработка ошибки при чтении данных из базы данных
                    }
                });
            } else {
                usernameTextView.setText("Username not available");
            }
        }

        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                emailTextView.setText(email);
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                saveCircularImageToDatabase(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveCircularImageToDatabase(Bitmap bitmap) {
        Bitmap circularBitmap = ImageUtils.getCircularBitmap(bitmap);
        profileImageView.setImageBitmap(circularBitmap);

        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getDisplayName());
            userRef.child("img").setValue(circularBitmap.toString());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAndSetProfileImage(String imgUri) {
        // Получаем ссылку на Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Получаем ссылку на изображение в Firebase Storage
        StorageReference imgRef = storageRef.child(imgUri);

        // Загружаем изображение и устанавливаем его в profileImageView с помощью Picasso
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Используем Picasso для загрузки изображения по URI и установки его в profileImageView
                Picasso.get().load(uri).into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Обработка ошибки загрузки изображения
            }
        });
    }

    public void Del(View view) {
        Intent intent = new Intent(this, Splash_screen_del.class);
        startActivity(intent);
    }

    public void Home(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    public void map(View view) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void like(View view){
        Intent intent = new Intent(this, Likes.class);
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
