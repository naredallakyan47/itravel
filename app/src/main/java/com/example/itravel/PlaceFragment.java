package com.example.itravel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

import java.io.InputStream;

public class PlaceFragment extends DialogFragment {

    private static final String TAG = "PlaceFragment";
    public static String placeName;
    private String username;

    private boolean isLiked = false;

    public PlaceFragment(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            username = currentUser.getDisplayName();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_dialog, container, false);
        TextView textView = view.findViewById(R.id.place_name);
        textView.setText(placeName);

        Button infoButton = view.findViewById(R.id.info_button);
        ImageView likeImage = view.findViewById(R.id.like_image);
        ImageView imageView = view.findViewById(R.id.image);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInformationActivity();
            }
        });

        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLiked = !isLiked;
                likeImage.setImageResource(isLiked ? R.drawable.like_on : R.drawable.like_off);
                DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes").child(username).child(placeName);

                if (isLiked) {
                    likesRef.setValue(true);
                } else {
                    likesRef.removeValue();
                }
            }
        });


        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes").child(username).child(placeName);
        likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isLiked = dataSnapshot.getValue(Boolean.class);
                    likeImage.setImageResource(isLiked ? R.drawable.like_on : R.drawable.like_off);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error reading like status", databaseError.toException());
            }
        });

        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference("Places").child(placeName).child("Image");
        placeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.getValue(String.class);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            new DownloadImageTask(imageView).execute(uri.toString());
                        }
                    }).addOnFailureListener(e -> Log.e(TAG, "Error downloading image", e));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error reading data", databaseError.toException());
            }
        });
        return view;
    }

    private void openInformationActivity() {
        Intent intent = new Intent(getActivity(), Information.class);
        intent.putExtra("placeName", placeName);
        startActivity(intent);
    }

    static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            try {
                InputStream inputStream = new java.net.URL(imageUrl).openStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e(TAG, "Error downloading image", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}
