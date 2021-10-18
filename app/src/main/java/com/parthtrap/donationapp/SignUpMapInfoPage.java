package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SignUpMapInfoPage extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mapView;
    Button getButton;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    boolean Filled = false;
    String Idfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_map_info_page);
        getButton = findViewById(R.id.LocationSubmitButton);
        getButton.setClickable(false);
        Intent intent = getIntent();
        Idfrom = intent.getStringExtra("idfrom");

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.GoogleMapLocationInput);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mapView = googleMap;
        Log.d("Parthtrap", "onMapReady: Map Loaded");
        mapView.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("longitude", latLng.longitude);
                updates.put("latitude", latLng.latitude);
                Log.d("Parthtrap", "onMapReady: Uploaded Data");
                mapView.clear();
                mapView.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Exchange Location"));

                FirebaseFirestore.getInstance().collection("UserProfiles").document(auth.getCurrentUser().getUid()).set(updates, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Filled = true;
                    }
                });
            }
        });
        getButton.setClickable(true);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Filled) {
                    if (Idfrom == null) {
                        Intent i = new Intent(SignUpMapInfoPage.this, HomePage.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SignUpMapInfoPage.this, ProfileDisplay.class);
                        i.putExtra("id", Idfrom);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
    }
}