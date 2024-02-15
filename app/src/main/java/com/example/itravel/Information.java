package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        String textFromDatabase = Database.getText();

        TextView textView = findViewById(R.id.textView);

        textView.setText(textFromDatabase);
    }
}
