package com.example.itravel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        if (intent != null) {
            Login.GuestMode = intent.getBooleanExtra("GuestMode", false);
        }

        ImageView profileImageView = findViewById(R.id.profile);
        Bitmap savedImage = loadImageFromInternalStorage();
        if (savedImage != null) {
            profileImageView.setImageBitmap(getCircularBitmap(savedImage));
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        TextView usernameTextView = findViewById(R.id.username);

        if (Login.GuestMode) {
            usernameTextView.setText("Guest Mode");
        } else {
            if (currentUser != null) {
                String username = currentUser.getDisplayName();
                if (username != null && !username.isEmpty()) {
                    usernameTextView.setText(username);
                } else {
                    // Если имя пользователя не установлено, вы можете отобразить сообщение об ошибке или что-то другое
                    usernameTextView.setText("Username not available");
                }
            } else {
                // Если пользователь не вошел, например, приложение запущено впервые, или нет сети, напишите здесь логику для входа пользователя.
            }
        }
    }






    public void chooseImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView profileImageView = findViewById(R.id.profile);
                profileImageView.setImageBitmap(getCircularBitmap(bitmap));
                saveImageToInternalStorage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_image.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap loadImageFromInternalStorage() {
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("images", Context.MODE_PRIVATE);
            File mypath = new File(directory, "profile_image.jpg");
            return BitmapFactory.decodeStream(new FileInputStream(mypath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int diameter = Math.min(width, height);

        Bitmap output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, diameter, diameter);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.RED);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (diameter - width) / 2, (diameter - height) / 2, paint);

        return output;
    }



    public void settings(View view){
        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("GuestMode", Login.GuestMode);
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
