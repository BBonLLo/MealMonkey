package com.example.mealmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.net.Uri;

import com.bumptech.glide.Glide;

public class AppLoadActivity extends AppCompatActivity {

    private ImageView imageView;
    private final int DURATION_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_app_load);

        String urlGif = "https://c.tenor.com/OtltxmQRCh4AAAAi/burger-burger-time.gif";

        imageView = findViewById(R.id.imageView);
        Uri uri = Uri.parse(urlGif);
        Glide.with(getApplicationContext()).load(uri).into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AppLoadActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURATION_SPLASH);
    }
}