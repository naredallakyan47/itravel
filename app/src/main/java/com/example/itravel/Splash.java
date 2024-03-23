package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private ImageView logoImageView;
    private TextView textView;
    private int animationDuration = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoImageView = findViewById(R.id.logoImageView);
        textView = findViewById(R.id.textView);

        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        logoImageView.startAnimation(rotation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(android.view.View.VISIBLE);
                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, animationDuration);
    }
}
