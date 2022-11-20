package com.example.mealmonkey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RestaurantAdd extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button button;
    private String title = null;
    private String desc;
    private float score;
    private double latPos;
    private double longPos;
    private LatLng markerPos;
    private TextView textViewNameAdd;
    private TextView textViewDescriptionAdd;
    private RatingBar ratingBarAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_restaurant_add);

        String email = getIntent().getStringExtra("email");
        title = getIntent().getStringExtra("name");
        latPos = getIntent().getDoubleExtra("lat", 0);
        longPos = getIntent().getDoubleExtra("long", 0);
        markerPos = new LatLng(latPos, longPos);

        textViewNameAdd = findViewById(R.id.add_name);
        textViewDescriptionAdd = findViewById(R.id.add_description);
        ratingBarAdd = findViewById(R.id.ratingBarAdd);


        if (title != null) {
            db.collection("markers").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (title.equals(document.get("Name"))) {
                                        Map<String, Object> markers = document.getData();
                                        textViewNameAdd.setText(title);
                                        textViewNameAdd.setEnabled(false);
                                        textViewDescriptionAdd.setText(markers.get("Description").toString());
                                        ratingBarAdd.setRating(Float.parseFloat(markers.get("Score").toString()));
                                        button.setText(R.string.text_edit);
                                        latPos = Double.parseDouble(markers.get("Lat").toString());
                                        longPos = Double.parseDouble(markers.get("Long").toString());
                                        markerPos = new LatLng(latPos, longPos);
                                    }
                                }
                            }
                        }
                    });
        }

        button = findViewById(R.id.buttonAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title == null) {
                    Map<String, Object> marker = new HashMap<>();
                    marker.put("Name", textViewNameAdd.getText().toString());
                    marker.put("Description", textViewDescriptionAdd.getText().toString());
                    marker.put("Score", ratingBarAdd.getRating());
                    marker.put("Lat", latPos);
                    marker.put("Long", longPos);
                    marker.put("LatLong", markerPos);
                    marker.put("User", email);

                    //Añadir marcador a la base de datos
                    db.collection("markers").document(textViewNameAdd.getText().toString()).set(marker)
                            //mete en la base de datos el primary key con toda la informacion del objeto marker
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    boolean done = true;
                                    getIntent().putExtra("done", done);
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    boolean done = false;
                                    getIntent().putExtra("done", done);
                                    setResult(RESULT_CANCELED, getIntent());
                                    finish();
                                }
                            });
                } else {

                    Map<String, Object> marker = new HashMap<>();
                    marker.put("Name", textViewNameAdd.getText().toString());
                    marker.put("Description", textViewDescriptionAdd.getText().toString());
                    marker.put("Score", ratingBarAdd.getRating());
                    marker.put("Lat", latPos);
                    marker.put("Long", longPos);
                    marker.put("LatLong", markerPos);
                    marker.put("User", email);

                    //Añadir marcador a la base de datos
                    db.collection("markers").document(textViewNameAdd.getText().toString()).set(marker)
                            //mete en la base de datos el primary key con toda la informacion del objeto marker
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    boolean done = true;
                                    getIntent().putExtra("done", done);
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    boolean done = false;
                                    getIntent().putExtra("done", done);
                                    setResult(RESULT_CANCELED, getIntent());
                                    finish();
                                }
                            });
                }
            }
        });
    }
}