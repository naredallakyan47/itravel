package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.app_name);

        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);

        logo.startAnimation(rotateAnimation);
        appName.startAnimation(scaleAnimation);


        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashScreenActivity.this, Login.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
