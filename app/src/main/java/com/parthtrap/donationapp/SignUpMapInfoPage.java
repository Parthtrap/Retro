package com.parthtrap.donationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.parthtrap.donationapp.HelperFunctions.ItemLocationUpdate;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SignUpMapInfoPage extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mapView;
    Button getButton;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    boolean Filled = false;
    String Idfrom;
    Object OriginalLatitude, OriginalLongitude;
    double NewLatitude, NewLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_map_info_page);
        getButton = findViewById(R.id.LocationSubmitButton);
        getButton.setClickable(false);
        Intent intent = getIntent();
        Idfrom = intent.getStringExtra("idfrom");

        FirebaseFirestore.getInstance().collection("UserProfiles").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                OriginalLatitude = documentSnapshot.get("latitude");
                OriginalLongitude = documentSnapshot.get("longitude");
            }
        });

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.GoogleMapLocationInput);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mapView = googleMap;
        mapView.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                NewLatitude = latLng.latitude;
                NewLongitude = latLng.longitude;
                Map<String, Object> updates = new HashMap<>();
                updates.put("longitude", latLng.longitude);
                updates.put("latitude", latLng.latitude);
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
                    new ItemLocationUpdate(auth.getCurrentUser().getUid(), NewLatitude, NewLongitude);
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

    @Override
    public void onBackPressed() {
        if (OriginalLatitude != null) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.MapBackTitle).setMessage(R.string.MapBackText).setNegativeButton(R.string.DeleteItemNo,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton(R.string.DeleteItemYes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("longitude", OriginalLongitude);
                    updates.put("latitude", OriginalLatitude);
                    FirebaseFirestore.getInstance().collection("UserProfiles").document(auth.getCurrentUser().getUid()).set(updates, SetOptions.merge());
                    SignUpMapInfoPage.super.onBackPressed();
                }
            }).show();
            alert.create();
        } else
            Toast.makeText(this, "Enter Your Location", Toast.LENGTH_SHORT).show();
    }
}