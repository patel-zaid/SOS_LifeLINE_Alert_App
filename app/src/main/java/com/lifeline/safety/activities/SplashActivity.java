package com.lifeline.safety.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lifeline.safety.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        ImageView imgLogo   = findViewById(R.id.imgLogo);
        TextView  tvAppName = findViewById(R.id.tvAppName);
        TextView  tvTagline = findViewById(R.id.tvTagline);
        TextView  tvBadge   = findViewById(R.id.tvBadge);
        View      dotLeft   = findViewById(R.id.dotLeft);
        View      dotCenter = findViewById(R.id.dotCenter);
        View      dotRight  = findViewById(R.id.dotRight);

        // Animate logo pop-in
        AnimationSet logoAnim = new AnimationSet(true);
        ScaleAnimation scaleUp = new ScaleAnimation(
                0.3f, 1f, 0.3f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleUp.setDuration(700);
        AlphaAnimation fadeInLogo = new AlphaAnimation(0f, 1f);
        fadeInLogo.setDuration(700);
        logoAnim.addAnimation(scaleUp);
        logoAnim.addAnimation(fadeInLogo);
        logoAnim.setFillAfter(true);
        imgLogo.startAnimation(logoAnim);

        // Animate app name
        AlphaAnimation fadeInName = new AlphaAnimation(0f, 1f);
        fadeInName.setDuration(600);
        fadeInName.setStartOffset(600);
        fadeInName.setFillAfter(true);
        tvAppName.startAnimation(fadeInName);

        // Animate tagline
        AlphaAnimation fadeInTag = new AlphaAnimation(0f, 1f);
        fadeInTag.setDuration(600);
        fadeInTag.setStartOffset(900);
        fadeInTag.setFillAfter(true);
        tvTagline.startAnimation(fadeInTag);

        // Animate dots as a progress bar
        dotLeft.setAlpha(0.3f);
        dotCenter.setAlpha(0.3f);
        dotRight.setAlpha(0.3f);
        dotCenter.setScaleX(0.3f);

        // Animate progress: left dot -> center bar grows -> right dot
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            dotLeft.animate().alpha(1f).setDuration(200).start();
        }, 900);
        handler.postDelayed(() -> {
            dotCenter.animate().alpha(1f).scaleX(1.8f).setDuration(700).start();
        }, 1200);
        handler.postDelayed(() -> {
            dotRight.animate().alpha(1f).setDuration(200).start();
        }, 2000);
        handler.postDelayed(() -> {
            dotCenter.animate().scaleX(1f).setDuration(200).start();
        }, 2300);

        // Fade in badge text early so it's visible during progress
        tvBadge.setAlpha(0f);
        handler.postDelayed(() -> {
            tvBadge.animate().alpha(1f).setDuration(400).start();
        }, 1200);

        // Animate badge
        AlphaAnimation fadeInBadge = new AlphaAnimation(0f, 1f);
        fadeInBadge.setDuration(500);
        fadeInBadge.setStartOffset(2000);
        fadeInBadge.setFillAfter(true);
        tvBadge.startAnimation(fadeInBadge);

        // Animate center dot width (progress bar effect)
        handler.postDelayed(() -> {
            dotCenter.animate().scaleX(1.8f).setDuration(900).withEndAction(() -> {
                dotCenter.animate().scaleX(1f).setDuration(200).start();
            }).start();
        }, 1400);

        // Go to next screen after animation
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2700);
    }
}