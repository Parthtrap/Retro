/*
Upload Item for Donation Page
 */

package com.parthtrap.donationapp.UserPortal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.HelperFunctions.ExchangeItemLocationUpdate;
import com.parthtrap.donationapp.R;

public class ExchangeItemUploadPage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("EXCHANGE_ITEM_UPLOAD_PAGE_LOGS");

	// Defining Front End Components
	ImageView ItemImageBox;
	Button UploadImageButton, SubmitButton;
	EditText NameBox, DescBox, CategoryBox, WantForBox;

	// Connecting to Firebase Authentication and Database. Also getting Document and Collection References
	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseUser user = auth.getCurrentUser();
	FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
	DocumentReference userDocument = fsdb.collection("UserProfiles").document(user.getUid());
	CollectionReference exchangeItemCollection = fsdb.collection("ExchangeItems");
	FirebaseStorage storage = FirebaseStorage.getInstance();

	// Variable Storage
	ExchangeItemClass itemInfo = new ExchangeItemClass();
	private Uri imageURI;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exchange_item_request);

		Log.d(LOG.getPAGES_LOG(), "Exchange Item Upload Page Created");
		Log.d(LOG.getLOCAL_LOG(), "Exchange Item Upload Page Created");

		// Linking Frontend to Backend via ID
		ItemImageBox = findViewById(R.id.ExchangeItemImageUploadForm);
		UploadImageButton = findViewById(R.id.UploadExchangeImageButtonForm);
		SubmitButton = findViewById(R.id.ExchangeItemSubmitButtonForm);
		NameBox = findViewById(R.id.ItemExchangeNameForm);
		DescBox = findViewById(R.id.ItemExchangeDescriptionForm);
		CategoryBox = findViewById(R.id.ItemExchangeCategoryForm);
		WantForBox = findViewById(R.id.ItemExchangeWantForForm);

		// Setting User Rating to the Item
		userDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@Override public void onSuccess(DocumentSnapshot documentSnapshot){
				Log.d(LOG.getDATA_LOG(), "User Rating Received");
				itemInfo.setRating(documentSnapshot.getLong("rating"));
			}
		});

		// Image Upload
		UploadImageButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Image Upload Button Clicked");
				Intent imageGetter = new Intent();
				imageGetter.setAction(Intent.ACTION_GET_CONTENT);
				imageGetter.setType("image/*");
				startActivityForResult(imageGetter, 1);
			}
		});

		// Submit Button pressing
		SubmitButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Item Upload Submit Button");
				if(imageURI != null){
					UploadImageButton.setClickable(false);
					SubmitButton.setClickable(false);
					uploadFireBase();
				}else{
					Log.d(LOG.getLOCAL_LOG(), "No Image uploaded");
					Toast.makeText(ExchangeItemUploadPage.this, "Upload Image First", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// Show image on Image View when Image is uploaded
	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == RESULT_OK && data != null){
			ItemImageBox = findViewById(R.id.ExchangeItemImageUploadForm);
			imageURI = data.getData();
			ItemImageBox.setImageURI(imageURI);
		}
	}

	// Upload Data to Firebase
	public void uploadFireBase(){
		Log.d(LOG.getLOCAL_LOG(), "Upload Function Called");
		StorageReference ref = storage.getReference().child("Exchange Images").child(System.currentTimeMillis() + "." + getContentResolver().getType(imageURI).substring(getContentResolver().getType(imageURI).lastIndexOf("/") + 1));
		ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
			// If the upload is Successfull
			@Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
				Log.d(LOG.getDATA_LOG(), "Image Uploaded");
				ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
					@Override public void onSuccess(Uri uri){
						itemInfo.setImageURL(uri.toString());
						uploadData();
					}
				});

			}
		}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
			@Override public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot){

			}
		}).addOnFailureListener(new OnFailureListener(){
			// If the upload Fails
			@Override public void onFailure(@NonNull Exception e){

			}
		});
	}

	// Data Uploading Function
	private void uploadData(){

		// Linking Frontend to Backend via ID
		NameBox = findViewById(R.id.ItemExchangeNameForm);
		DescBox = findViewById(R.id.ItemExchangeDescriptionForm);
		CategoryBox = findViewById(R.id.ItemExchangeCategoryForm);
		WantForBox = findViewById(R.id.ItemExchangeWantForForm);

		// Setting Data into the item Object
		itemInfo.setName(NameBox.getText().toString().toLowerCase());
		itemInfo.setDescription(DescBox.getText().toString());
		itemInfo.setCategory(CategoryBox.getText().toString().toLowerCase());
		itemInfo.setWantFor(WantForBox.getText().toString());
		itemInfo.setOwnerID(user.getUid());

		// Upload the item to database
		exchangeItemCollection.add(itemInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
			@Override public void onSuccess(DocumentReference documentReference){
				Log.d(LOG.getDATA_LOG(), "Full Item Uploaded");
			}
		});

		// Getting Location of owner and setting it to the item object
		fsdb.collection("UserProfiles").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@Override public void onSuccess(DocumentSnapshot documentSnapshot){
				new ExchangeItemLocationUpdate(auth.getCurrentUser().getUid(), documentSnapshot.getDouble("latitude"), documentSnapshot.getDouble("longitude"));
			}
		});

		finish();
	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "Exchange Item Upload Page Destroyed");
		super.onDestroy();
	}
}