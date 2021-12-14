package com.parthtrap.donationapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ExchangeItemViewPage extends AppCompatActivity {
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
        InterestedButton = findViewById(R.id.ExchangeItemInterestedButton);
        DeleteItemButton.setVisibility(View.INVISIBLE);
        InterestedButton.setVisibility(View.INVISIBLE);


        Intent i = getIntent();
        ItemID = i.getStringExtra("id");
        BackID = i.getStringExtra("idfrom");

        fsdb.collection("ExchangeItems").document(ItemID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ExchangeItemClass DisplayItem = documentSnapshot.toObject(ExchangeItemClass.class);
                OwnerID = DisplayItem.getOwnerID();
                ItemNameBox.setText(DisplayItem.getName().substring(0, 1).toUpperCase() + DisplayItem.getName().substring(1));
                ItemDescBox.setText(DisplayItem.getDescription());
                ItemCategoryBox.setText(DisplayItem.getCategory());
                ItemWantForBox.setText(DisplayItem.getWantFor());
                Picasso.get().load(DisplayItem.getImageURL()).into(ItemImageBox);
                if (OwnerID.equals(auth.getCurrentUser().getUid()))
                    DeleteItemButton.setVisibility(View.VISIBLE);
                else
                    InterestedButton.setVisibility(View.VISIBLE);
            }
        }).

                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                Intent i = new Intent(ExchangeItemViewPage.this, ProfileDisplay.class);
                i.putExtra("id", OwnerID);
                startActivity(i);
            }
        });

        DeleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ExchangeItemViewPage.this);
                alert.setTitle(R.string.DeleteItemTitle)
                        .setMessage(R.string.DeleteItemText)
                        .setNegativeButton(R.string.DeleteItemNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton(R.string.DeleteItemYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fsdb.collection("ExchangeItems").document(ItemID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imageURL")).delete();
                                if (!BackID.equals("null")) {
                                    Intent i = new Intent(ExchangeItemViewPage.this, ProfileDisplay.class);
                                    i.putExtra("id", BackID);
                                    startActivity(i);
                                    fsdb.collection("ExchangeItems").document(ItemID).delete();
                                    finish();
                                } else {
                                    Intent i = new Intent(ExchangeItemViewPage.this, HomePage.class);
                                    startActivity(i);
                                    fsdb.collection("ExchangeItems").document(ItemID).delete();
                                    finish();
                                }
                            }
                        });

                    }
                }).show();
                alert.create();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!BackID.equals("null")) {
            Intent i = new Intent(ExchangeItemViewPage.this, ProfileDisplay.class);
            i.putExtra("id", BackID);
            startActivity(i);
            finish();
        } else {
            super.onBackPressed();
        }

    }
}