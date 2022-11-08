package com.example.mealmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;
import android.net.Uri;

public class AppLoadActivity extends AppCompatActivity {

    private VideoView videoView;
    private final int DURATION_SPLASH = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_app_load);

        videoView = findViewById(R.id.videoView3);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.burger;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AppLoadActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURATION_SPLASH);
        /*try {
            Thread.sleep(5000);
            Intent intent = new Intent(AppLoadActivity.this, MapsActivity.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}