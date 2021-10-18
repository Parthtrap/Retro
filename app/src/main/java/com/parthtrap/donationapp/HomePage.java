package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.parthtrap.donationapp.HelperApadters.ProfilePageItemAdapter;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;

public class HomePage extends AppCompatActivity {

    TextView HomePageNameDisplay;
    Button LogOut, ExchangeItem, DonateItem;
    ImageButton SearchButton;
    EditText SearchBox;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProfilePageItemAdapter adapter;
    CollectionReference userCollection = db.collection("UserProfiles");
    CollectionReference itemCollection = db.collection("ExchangeItems");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        LogOut = findViewById(R.id.LogOutButtonHomePage);
        HomePageNameDisplay = findViewById(R.id.TempTextView);
        ExchangeItem = findViewById(R.id.ExchangeItemButtonHomePage);
        SearchButton = findViewById(R.id.HomePageSearchButton);
        SearchBox = findViewById(R.id.HomePageSearchBar);

        db.collection("UserProfiles").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String UserName = documentSnapshot.getString("name");
                HomePageNameDisplay.setText("Welcome " + UserName);
            }
        });
        HomePageNameDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this, ProfileDisplay.class);
                i.putExtra("id", user.getUid());
                startActivity(i);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(HomePage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        ExchangeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this, ExchangeItemUploadPage.class);
                startActivity(i);
            }
        });
        FirestoreRecyclerOptions<ExchangeItemClass> options =
                new FirestoreRecyclerOptions.Builder<ExchangeItemClass>().setQuery(itemCollection.orderBy("rating", Query.Direction.DESCENDING).limit(10), ExchangeItemClass.class).build();
        adapter = new ProfilePageItemAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.HomePageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProfilePageItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Intent intent = new Intent(HomePage.this, ExchangeItemViewPage.class);
                intent.putExtra("id", documentSnapshot.getId());
                intent.putExtra("idfrom", "null");
                startActivity(intent);
                finish();
            }
        });
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SearchedString = SearchBox.getText().toString().toLowerCase().trim();
                Query query = null;
                if (SearchedString.isEmpty()) {
                    query = itemCollection.orderBy("rating", Query.Direction.DESCENDING);
                } else {
                    query = itemCollection.whereEqualTo("name", SearchedString).orderBy("rating", Query.Direction.DESCENDING);
                }
                FirestoreRecyclerOptions<ExchangeItemClass> option =
                        new FirestoreRecyclerOptions.Builder<ExchangeItemClass>().setQuery(query, ExchangeItemClass.class).build();
                adapter = new ProfilePageItemAdapter(option);
                RecyclerView recyclerView = findViewById(R.id.HomePageRecyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                recyclerView.setAdapter(adapter);
                adapter.startListening();

            }
        });
        adapter.startListening();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }
}