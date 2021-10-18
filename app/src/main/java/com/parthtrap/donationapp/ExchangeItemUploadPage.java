package com.parthtrap.donationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;

public class ExchangeItemUploadPage extends AppCompatActivity {

    ImageView ItemImageBox;
    Button UploadImageButton, SubmitButton;
    EditText NameBox, DescBox, CategoryBox, WantForBox;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
    DocumentReference userDocument = fsdb.collection("UserProfiles").document(user.getUid());
    CollectionReference exchangeItemCollection = fsdb.collection("ExchangeItems");
    FirebaseStorage storage = FirebaseStorage.getInstance();

    ExchangeItemClass itemInfo = new ExchangeItemClass();

    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_item_request);

        ItemImageBox = findViewById(R.id.ExchangeItemImageUploadForm);
        UploadImageButton = findViewById(R.id.UploadExchangeImageButtonForm);
        SubmitButton = findViewById(R.id.ExchangeItemSubmitButtonForm);
        NameBox = findViewById(R.id.ItemExchangeNameForm);
        DescBox = findViewById(R.id.ItemExchangeDescriptionForm);
        CategoryBox = findViewById(R.id.ItemExchangeCategoryForm);
        WantForBox = findViewById(R.id.ItemExchangeWantForForm);

        userDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                itemInfo.setRating(documentSnapshot.getLong("rating"));
            }
        });

        UploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageGetter = new Intent();
                imageGetter.setAction(Intent.ACTION_GET_CONTENT);
                imageGetter.setType("image/*");
                startActivityForResult(imageGetter, 1);
            }
        });

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageURI != null) {
                    UploadImageButton.setClickable(false);
                    SubmitButton.setClickable(false);
                    uploadFireBase();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ItemImageBox = findViewById(R.id.ExchangeItemImageUploadForm);
            imageURI = data.getData();
            ItemImageBox.setImageURI(imageURI);
        }
    }

    public void uploadFireBase() {
        StorageReference ref =
                storage.getReference().child("Exchange Images").child(System.currentTimeMillis()
                        + "."
                        + getContentResolver().getType(imageURI).substring(getContentResolver().getType(imageURI).lastIndexOf("/") + 1));
        ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        itemInfo.setImageURL(uri.toString());
                        uploadData();

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void uploadData() {
        NameBox = findViewById(R.id.ItemExchangeNameForm);
        DescBox = findViewById(R.id.ItemExchangeDescriptionForm);
        CategoryBox = findViewById(R.id.ItemExchangeCategoryForm);
        WantForBox = findViewById(R.id.ItemExchangeWantForForm);
        itemInfo.setName(NameBox.getText().toString().toLowerCase());
        itemInfo.setDescription(DescBox.getText().toString());
        itemInfo.setCategory(CategoryBox.getText().toString());
        itemInfo.setWantFor(WantForBox.getText().toString());
        itemInfo.setOwnerID(user.getUid());

        exchangeItemCollection.add(itemInfo);
        finish();

    }
}