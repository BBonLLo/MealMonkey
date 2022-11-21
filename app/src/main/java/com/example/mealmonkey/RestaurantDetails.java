package com.example.mealmonkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class RestaurantDetails extends AppCompatActivity {

    private ImageButton imageButtonMaps;
    private TextView details_name;
    private TextView details_description;
    private RatingBar ratingBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String title;
    private String email;
    private String desc;
    private Button button;
    private float score;
    private double latPos;
    private double longPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_restaurant_details);

        title = getIntent().getStringExtra("title");
        email = getIntent().getStringExtra("email");

        imageButtonMaps = findViewById(R.id.imageButtonMapDetails);
        details_name = findViewById(R.id.details_name);
        details_name.setText(title);
        details_description = findViewById(R.id.details_description);
        ratingBar = findViewById(R.id.ratingBarDetails);
        button = findViewById(R.id.buttonEdit);
        button.setVisibility(View.INVISIBLE);

        db.collection("markers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (title.equals(document.get("Name"))) {
                                    Map<String, Object> markers = document.getData();
                                    desc = markers.get("Description").toString();
                                    latPos = Double.parseDouble(markers.get("Lat").toString());
                                    longPos = Double.parseDouble(markers.get("Long").toString());
                                    score = Float.parseFloat(markers.get("Score").toString());
                                    if (email.equals(markers.get("User").toString())) {
                                        button.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            details_description.setText(desc);
                            ratingBar.setRating(score);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RestaurantDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        imageButtonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title.replace("\\s+", "+");
                Uri gmmIntentUri = Uri.parse("geo:" + latPos + "," + longPos + "?z=18&q=" + title); //
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantDetails.this, RestaurantAdd.class);
                intent.putExtra("name", title);
                intent.putExtra("email", email);
                intent.putExtra("latPos", latPos);
                intent.putExtra("longPos", longPos);
                startActivity(intent);
                finish();
            }
        });
    }
}