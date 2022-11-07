package com.example.mealmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class RestaurantDetails extends AppCompatActivity {

    private ImageButton imageButtonMaps;
    private TextView details_text_name;
    private TextView details_name;
    private TextView details_text_date;
    private TextView details_date;
    private RatingBar ratingBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_restaurant_details);

        String title = getIntent().getStringExtra("TITLE");

        imageButtonMaps = findViewById(R.id.imageButtonMapDetails);
        details_text_name = findViewById(R.id.details_text_name);
        details_name = findViewById(R.id.details_name);
        details_text_date = findViewById(R.id.details_text_date);
        details_date = findViewById(R.id.details_date);
        ratingBar = findViewById(R.id.ratingBarDetails);
        ratingBar.setRating(3.5f);
        textView = findViewById(R.id.textView2);
        textView.setText(title);
    }
}