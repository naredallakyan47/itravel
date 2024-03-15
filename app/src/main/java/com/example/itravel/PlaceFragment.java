package com.example.itravel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
        return view;
    }
}
