package com.example.itravel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private EditText mNameField;
    private EditText mLocationField;
    private EditText mDescField;
    private ImageView mAddPhotoButton;
    private Button mAddButton;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        mNameField = findViewById(R.id.name);
        mLocationField = findViewById(R.id.location);
        mDescField = findViewById(R.id.desc);
        mAddPhotoButton = findViewById(R.id.add_photo);
        mAddButton = findViewById(R.id.add_button);
        mProgressBar = findViewById(R.id.progress_bar);

        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                int YOUR_IMAGE_REQUEST_CODE = 1001;
                startActivityForResult(intent, YOUR_IMAGE_REQUEST_CODE);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mNameField.getText().toString().trim();
                String location = mLocationField.getText().toString().trim();
                String desc = mDescField.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(desc) || mImageUri == null) {
                    Toast.makeText(Add.this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidLocationFormat(location)) {
                    Toast.makeText(Add.this, "Please enter the location in the correct format (latitude, longitude)", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Показываем прогресс-бар и блокируем кнопку "Добавить"
                mProgressBar.setVisibility(View.VISIBLE);
                mAddButton.setEnabled(false);

                StorageReference imageRef = mStorage.child("images").child(mImageUri.getLastPathSegment());
                imageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                saveDataToFirebase(name, location, desc, imageUrl);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        // Скрываем прогресс-бар и разблокируем кнопку "Добавить"
                        mProgressBar.setVisibility(View.GONE);
                        mAddButton.setEnabled(true);
                    }
                });
            }
        });
    }

    private boolean isValidLocationFormat(String location) {
        String[] parts = location.split(",");
        if (parts.length != 2) {
            return false;
        }
        try {
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void saveDataToFirebase(String name, String location, String desc, String imageUrl) {
        mDatabase.child("Places").child(name).child("Desc").setValue(desc);
        mDatabase.child("Places").child(name).child("Image").setValue(imageUrl);

        DatabaseReference locationRef = mDatabase.child("Location").child(name);

        String[] coordinates = location.split(",");
        if (coordinates.length == 2) {
            locationRef.child("Latitude").setValue(coordinates[0].trim());
            locationRef.child("Longitude").setValue(coordinates[1].trim());
        }

        mNameField.setText("");
        mLocationField.setText("");
        mDescField.setText("");

        // После завершения загрузки, скрываем прогресс-бар и переходим на MainActivity
        mProgressBar.setVisibility(View.GONE);
        mAddButton.setEnabled(true);
        Intent intent = new Intent(Add.this, Main.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mAddPhotoButton.setImageURI(mImageUri);
        }
    }
}
