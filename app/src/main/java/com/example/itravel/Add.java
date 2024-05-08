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
    private ImageView[] mAddPhotoImageViews;
    private Button mAddButton;
    private ProgressBar mProgressBar;

    private Uri[] mImageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        mNameField = findViewById(R.id.name);
        mLocationField = findViewById(R.id.location);
        mDescField = findViewById(R.id.desc);
        mAddButton = findViewById(R.id.add_button);
        mProgressBar = findViewById(R.id.progress_bar);

        mAddPhotoImageViews = new ImageView[] {
                findViewById(R.id.add_photo1),
                findViewById(R.id.add_photo2),
                findViewById(R.id.add_photo3),
                findViewById(R.id.add_photo4),
                findViewById(R.id.add_photo5)
        };

        mImageUris = new Uri[mAddPhotoImageViews.length];

        for (int i = 0; i < mAddPhotoImageViews.length; i++) {
            final int index = i;
            mAddPhotoImageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, index);
                }
            });
        }

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mNameField.getText().toString().trim();
                String location = mLocationField.getText().toString().trim();
                String desc = mDescField.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(desc) || !areAllImagesSelected()) {
                    Toast.makeText(Add.this, "Please fill in all fields and select all images", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidLocationFormat(location)) {
                    Toast.makeText(Add.this, "Please enter the location in the correct format (latitude, longitude)", Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);
                mAddButton.setEnabled(false);

                for (int i = 0; i < mImageUris.length; i++) {
                    if (mImageUris[i] != null) {
                        uploadImageAndSaveData(name, location, desc, mImageUris[i], i);
                    }
                }
            }
        });
    }

    private boolean areAllImagesSelected() {
        for (Uri uri : mImageUris) {
            if (uri == null) {
                return false;
            }
        }
        return true;
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

    private void uploadImageAndSaveData(final String name, final String location, final String desc, Uri imageUri, final int index) {
        final StorageReference imageRef = mStorage.child("images").child(imageUri.getLastPathSegment());
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        saveDataToFirebase(name, location, desc, imageUrl, index);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mAddButton.setEnabled(true);
            }
        });
    }

    private void saveDataToFirebase(String name, String location, String desc, String imageUrl, int index) {
        mDatabase.child("Places").child(name).child("Desc").setValue(desc);
        mDatabase.child("Places").child(name).child("Images").child("Image" + (index + 1)).setValue(imageUrl);

        DatabaseReference locationRef = mDatabase.child("Location").child(name);

        String[] coordinates = location.split(",");
        if (coordinates.length == 2) {
            locationRef.child("Latitude").setValue(coordinates[0].trim());
            locationRef.child("Longitude").setValue(coordinates[1].trim());
        }

        resetFields();

        if (index == mImageUris.length - 1) {
            mProgressBar.setVisibility(View.GONE);
            mAddButton.setEnabled(true);
            Intent intent = new Intent(Add.this, Main.class);
            startActivity(intent);
            finish();
        }
    }

    private void resetFields() {
        mNameField.setText("");
        mLocationField.setText("");
        mDescField.setText("");
        for (int i = 0; i < mAddPhotoImageViews.length; i++) {
            mAddPhotoImageViews[i].setImageResource(R.drawable.add);
            mImageUris[i] = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            mImageUris[requestCode] = imageUri;
            mAddPhotoImageViews[requestCode].setImageURI(imageUri);
        }
    }
}
