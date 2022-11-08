package com.example.mealmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

public class RestaurantDetails extends AppCompatActivity {

    private ImageButton imageButtonMaps;
    private TextView details_text_name;
    private TextView details_name;
    private TextView details_text_description;
    private TextView details_description;
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
        details_name.setText(title);
        details_text_description = findViewById(R.id.details_text_description);
        details_description = findViewById(R.id.details_description);
        ratingBar = findViewById(R.id.ratingBarDetails);
        ratingBar.setRating(3.5f);
        textView = findViewById(R.id.textView2);
        textView.setText(title);

        imageButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}