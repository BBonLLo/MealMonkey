package com.example.mealmonkey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.example.mealmonkey.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Switch aSwitch;
    private ToggleButton tbLeft, tbRight;
    private Button buttonMarkIt;
    private MarkerOptions newMarkerO;
    private String email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getStringExtra("email");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        aSwitch = findViewById(R.id.switch1);
        tbLeft = findViewById(R.id.tbLeft);
        tbRight = findViewById(R.id.tbRight);
        buttonMarkIt = findViewById(R.id.btnMarkIt);
        buttonMarkIt.setVisibility(View.INVISIBLE);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mMap.clear();
                if (b) {
                    tbRight.setChecked(true);
                    tbLeft.setChecked(false);
                    buttonMarkIt.setVisibility(View.INVISIBLE);
                    loadMarkersGroup();
                } else {
                    tbRight.setChecked(false);
                    tbLeft.setChecked(true);
                    loadMarkers();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.map:
                        return true;
                    case R.id.feed:
                        startActivity(new Intent(getApplicationContext(), Feed.class).putExtra("email", email));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class).putExtra("email", email));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        buttonMarkIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double latitude = newMarkerO.getPosition().latitude;
                Double longitude = newMarkerO.getPosition().longitude;

                Intent intent = new Intent(MapsActivity.this, RestaurantAdd.class);
                intent.putExtra("email", email);
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longitude);
                startActivityForResult(intent, 1);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean done = data.getBooleanExtra("done", false);
        if (done) {
            mediaPlayer = MediaPlayer.create(this, R.raw.addmarker);
            mediaPlayer.start();
            Toast.makeText(MapsActivity.this, R.string.text_marker_added, Toast.LENGTH_LONG).show();
            mMap.clear();
            buttonMarkIt.setVisibility(View.INVISIBLE);
            loadMarkers();
        } else {
            Toast.makeText(MapsActivity.this, R.string.error_marker_not_added, Toast.LENGTH_LONG).show();
        }
    }

    private void notImplemented() {
        Toast toast = Toast.makeText(this, R.string.text_error_not_implemented, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(45);

        loadMarkers();

        LatLng bilbao = new LatLng(43.263021508769505, -2.9349855166425436);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bilbao, 10));
        mMap.setMinZoomPreference(5);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (!aSwitch.isChecked()) {
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.5f));
                    MarkerOptions newMarker = new MarkerOptions();
                    newMarker.position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    newMarkerO = newMarker;
                    mMap.addMarker(newMarker);
                    buttonMarkIt.animate();
                    buttonMarkIt.setVisibility(View.VISIBLE);
                    loadMarkers();
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (marker.getTitle() == null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                    return false;
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                    marker.getTitle();

                    Intent intent = new Intent(MapsActivity.this, RestaurantDetails.class);
                    intent.putExtra("title", marker.getTitle());
                    intent.putExtra("email", email);
                    startActivity(intent);
                    return false;
                }
            }
        });
    }

    private void loadMarkers() {
        db.collection("markers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (email.equals(document.get("User"))) {
                                    Map<String, Object> markers = document.getData();
                                    String name = markers.get("Name").toString();
                                    double latPos = Double.parseDouble(markers.get("Lat").toString());
                                    double longPos = Double.parseDouble(markers.get("Long").toString());
                                    LatLng latLong = new LatLng(latPos, longPos);

                                    if (markers.get("Score").toString().equals("0")) {
                                        Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLong).title(name).icon(BitmapDescriptorFactory.defaultMarker(20)));
                                    } else {
                                        Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLong).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(MapsActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadMarkersGroup() {
        db.collection("markers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> markers = document.getData();
                                String name = markers.get("Name").toString();
                                double latPos = Double.parseDouble(markers.get("Lat").toString());
                                double longPos = Double.parseDouble(markers.get("Long").toString());

                                LatLng latLong = new LatLng(latPos, longPos);
                                if (email.equals(document.get("User"))) {
                                    Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLong).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                } else {
                                    Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLong).title(name).icon(BitmapDescriptorFactory.defaultMarker(45)));
                                }
                            }

                        } else {
                            Toast.makeText(MapsActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}