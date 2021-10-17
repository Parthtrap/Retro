package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ExchangeItemView extends AppCompatActivity {
    TextView ItemNameBox, ItemDescBox, ItemCategoryBox, ItemWantForBox, ItemOwnerBox;
    ImageView ItemImageBox;
    ImageButton DeleteItemButton;
    Button InterestedButton;
    String ItemID;
    String BackID = "";
    String OwnerID;

    FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_item_view);

        ItemNameBox = findViewById(R.id.ExchangeItemNameDisplay);
        ItemDescBox = findViewById(R.id.ExchangeItemDescDisplay);
        ItemCategoryBox = findViewById(R.id.ExchangeItemCategoryDisplay);
        ItemWantForBox = findViewById(R.id.ExchangeItemWantForDisplay);
        ItemOwnerBox = findViewById(R.id.ExchangeItemOwnerDisplay);
        ItemImageBox = findViewById(R.id.ExchangeItemImageDisplay);
        DeleteItemButton = findViewById(R.id.DeleteButtonExchangeItemDisplay);
        DeleteItemButton.setVisibility(View.INVISIBLE);



        Intent i = getIntent();
        ItemID = i.getStringExtra("id");
        BackID = i.getStringExtra("idfrom");
        fsdb.collection("ExchangeItems").document(ItemID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ExchangeItemClass DisplayItem = documentSnapshot.toObject(ExchangeItemClass.class);
                OwnerID = DisplayItem.getOwnerID();
                ItemNameBox.setText(DisplayItem.getName());
                ItemDescBox.setText(DisplayItem.getDescription());
                ItemCategoryBox.setText(DisplayItem.getCategory());
                ItemWantForBox.setText(DisplayItem.getWantFor());
                Picasso.get().load(DisplayItem.getImageURL()).into(ItemImageBox);
                if (OwnerID.equals(auth.getCurrentUser().getUid()))
                {
                    DeleteItemButton.setVisibility(View.VISIBLE);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                fsdb.collection("UserProfiles").document(OwnerID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ItemOwnerBox.setText(documentSnapshot.getString("name"));
                    }
                });
            }
        });


        ItemOwnerBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExchangeItemView.this, ProfileDisplay.class);
                i.putExtra("id", OwnerID);
                startActivity(i);
            }
        });

        DeleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsdb.collection("ExchangeItems").document(ItemID).delete();
                if (!BackID.equals("null"))
                {
                    Intent i = new Intent(ExchangeItemView.this, ProfileDisplay.class);
                    i.putExtra("id", BackID);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(ExchangeItemView.this, HomePage.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!BackID.equals("null"))
        {
            Intent i = new Intent(ExchangeItemView.this, ProfileDisplay.class);
            i.putExtra("id", BackID);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(ExchangeItemView.this, HomePage.class);
            startActivity(i);
            finish();
        }

    }
}