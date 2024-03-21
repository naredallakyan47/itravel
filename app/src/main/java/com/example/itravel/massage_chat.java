package com.example.itravel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class massage_chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage_chat);

        String username = getIntent().getStringExtra("USERNAME");

        TextView usernameTextView = findViewById(R.id.username);
        usernameTextView.setText(username);
    }

    public void send(View view) {
        EditText messageEditText = findViewById(R.id.message);
        String message = messageEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            displayMessage(message);
            messageEditText.setText("");
        } else {
            Toast.makeText(this, "Write massage", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayMessage(String message) {
        LinearLayout mainLayout = findViewById(R.id.main);

        TextView textView = new TextView(this);
        textView.setText(message);

        textView.setTextColor(Color.WHITE);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(16);
        textView.setBackgroundResource(R.drawable.rounded_text_background);
        textView.setGravity(Gravity.START);

        Paint paint = new Paint();
        Rect bounds = new Rect();
        paint.setTextSize(textView.getTextSize());
        paint.getTextBounds(message, 0, message.length(), bounds);
        int width = bounds.width() + 50;

        textView.setWidth(width);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 0);
        textView.setLayoutParams(layoutParams);

        mainLayout.addView(textView);
    }



}
