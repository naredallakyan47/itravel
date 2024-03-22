package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PlaceFragment extends DialogFragment {

    private String placeName;

    public PlaceFragment(String placeName) {
        this.placeName = placeName;
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
                if (placeName.equals("Haghpat")) {
                    Intent intent = new Intent(getActivity(), Information.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "No information available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
