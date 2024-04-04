package com.example.itravel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference mDatabase;

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void toggleLike(String placeName, boolean isLiked) {

        DatabaseReference likesRef = mDatabase.child("likes").child(placeName);

        likesRef.setValue(isLiked);
    }
}
