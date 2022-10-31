package com.example.mealmonkey;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Switch aSwitch;
    private ToggleButton tbLeft, tbRight;
    private Button buttonMarkIt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                if (b) {
                    tbRight.setChecked(true);
                    tbLeft.setChecked(false);
                } else {
                    tbRight.setChecked(false);
                    tbLeft.setChecked(true);
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
                        startActivity(new Intent(getApplicationContext(), Feed.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
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
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.5f));
                MarkerOptions newMarker = new MarkerOptions();
                newMarker.position(latLng)
                        .title(String.valueOf(R.string.text_add_restaurant))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                mMap.addMarker(newMarker);
                buttonMarkIt.animate();
                buttonMarkIt.setVisibility(View.VISIBLE);
                loadMarkers();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                return false;
            }
        });
    }

    private void loadMarkers() {

        LatLng bilbao = new LatLng(43.263021508769505, -2.9349855166425436);
        LatLng santutxu = new LatLng(43.25419917602184, -2.911833814662221);

        Marker markerBilbao = mMap.addMarker(new MarkerOptions().position(bilbao).title("Bilbao").icon(BitmapDescriptorFactory.defaultMarker(45)));
        Marker markerSantutxu = mMap.addMarker(new MarkerOptions().position(santutxu).title("Santutxu").icon(BitmapDescriptorFactory.defaultMarker(45)));

    }

}