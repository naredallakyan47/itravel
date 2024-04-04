package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class PlaceFragment extends DialogFragment {

    private String placeName;
    private boolean isLiked = false;
    private FirebaseHelper firebaseHelper;

    public PlaceFragment(String placeName) {
        this.placeName = placeName;
        firebaseHelper = new FirebaseHelper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_dialog, container, false);
        TextView textView = view.findViewById(R.id.place_name);
        textView.setText(placeName);

        Button infoButton = view.findViewById(R.id.info_button);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInformationActivity();
            }
        });


        ImageView likeImage = view.findViewById(R.id.like_image);

        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {

                    likeImage.setImageResource(R.drawable.like_off);
                    isLiked = false;
                } else {
                    likeImage.setImageResource(R.drawable.like_on);
                    isLiked = true;
                }

                firebaseHelper.toggleLike(placeName, isLiked);
            }
        });


        return view;
    }

    private void openInformationActivity() {
        Intent intent = new Intent(getActivity(), Information.class);
        intent.putExtra("placeName", placeName);
        startActivity(intent);
    }
}
