package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.parthtrap.donationapp.HelperApadters.ProfilePageItemAdapter;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.parthtrap.donationapp.HelperClasses.UserInfoHelper;

import java.util.HashMap;

public class ProfileDisplay extends AppCompatActivity {

    TextView NameBox, AddressBox, PhoneBox, EmailBox, RatingDisplay;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("UserProfiles");
    CollectionReference userCollections = db.collection("UserProfiles");
    CollectionReference itemCollection = db.collection("ExchangeItems");
    ProfilePageItemAdapter adapter;
    LinearLayout RatingLayout;
    EditText ReviewBox;
    RatingBar RatingBox;
    Button SubmitRating, LocationChange, LogOutButton;
    float NewRating = 0;
    int RatingCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);
        Intent i = getIntent();
        String id = i.getStringExtra("id");

        NameBox = findViewById(R.id.NameDisplayDisplayPage);
        AddressBox = findViewById(R.id.AddressDisplayDisplayPage);
        PhoneBox = findViewById(R.id.PhoneDisplayDisplayPage);
        EmailBox = findViewById(R.id.EmailDisplayDisplayPage);
        LogOutButton = findViewById(R.id.LogOutButton);
        RatingLayout = findViewById(R.id.RatingLayoutProfilePage);
        ReviewBox = findViewById(R.id.ReviewBarProfilePage);
        RatingBox = findViewById(R.id.RatingBarProfilePage);
        RatingBox.setStepSize(1);
        RatingDisplay = findViewById(R.id.RatingDisplayDisplayPage);
        SubmitRating = findViewById(R.id.RatingSubmitButtonProfilePage);
        LocationChange = findViewById(R.id.LocationChangeButtonProfilePage);

        if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            RatingLayout.setVisibility(View.INVISIBLE);
        }

        userCollection.document(id).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    finish();

                UserInfoHelper DisplayUser = value.toObject(UserInfoHelper.class);
                NameBox.setText(DisplayUser.getName());
                AddressBox.setText(DisplayUser.getAddress());
                EmailBox.setText(DisplayUser.getEmailId());
                RatingDisplay.setText(DisplayUser.getRating() + "");

                if (DisplayUser.getPublicPhone())
                    PhoneBox.setText(DisplayUser.getPhoneNumber());
                else
                    PhoneBox.setText("[Hidden]");
            }
        });

        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ProfileDisplay.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        FirestoreRecyclerOptions<ExchangeItemClass> options =
                new FirestoreRecyclerOptions.Builder<ExchangeItemClass>().setQuery(itemCollection.whereEqualTo("ownerID", id), ExchangeItemClass.class).build();
        adapter = new ProfilePageItemAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.ItemListRecyclerViewProfilePage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProfilePageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(ProfileDisplay.this, ExchangeItemViewPage.class);
                intent.putExtra("id", documentSnapshot.getId());
                intent.putExtra("idfrom", id);
                startActivity(intent);
                finish();
            }
        });

        SubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RatingBox.getRating() != 0 && !ReviewBox.getText().toString().trim().isEmpty()) {
                    HashMap<String, Object> ratingMap = new HashMap<>();
                    ratingMap.put("Rating", RatingBox.getRating());
                    ratingMap.put("Review", ReviewBox.getText().toString().trim());
                    userCollection.document(id).collection("Ratings").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(ratingMap);
                    RefreshUserRatings(id);
                }
            }
        });

        LocationChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDisplay.this, SignUpMapInfoPage.class);
                intent.putExtra("idfrom", id);
                startActivity(intent);
                finish();
            }
        });
    }


    private void RefreshUserRatings(String id) {
        NewRating = 0;
        RatingCount = 0;

        userCollections.document(id).collection("Ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    NewRating += snapshot.getLong("Rating").intValue();
                    RatingCount++;
                }
                NewRating = NewRating / RatingCount;
                if (RatingCount == 0)
                    userCollections.document(id).update("rating", 0);
                else
                    userCollections.document(id).update("rating", NewRating);
                RefreshItemRatings(id);
            }

        });

    }

    private void RefreshItemRatings(String id) {
        Log.d("Parthtrap", "onSuccess: Function Started");

        itemCollection.whereEqualTo("ownerID", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("Parthtrap", "onSuccess: Items Found");
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Log.d("Parthtrap", "onSuccess: " + snapshot.getId() + " Rating : " + NewRating);
                    itemCollection.document(snapshot.getId()).update("rating", NewRating);
                }
                Log.d("Parthtrap", "onSuccess: Items Changed");
            }
        });
        Log.d("Parthtrap", "onSuccess: Function End");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.stopListening();
    }
}