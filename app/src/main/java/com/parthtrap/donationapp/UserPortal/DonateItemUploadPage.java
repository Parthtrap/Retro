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
import com.parthtrap.donationapp.HelperClasses.DonateItemClass;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.HelperFunctions.DonateItemLocationUpdate;
import com.parthtrap.donationapp.R;

public class DonateItemUploadPage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("DONATE_ITEM_UPLOAD_PAGE_LOGS");

	// Defining Front End Components
	ImageView ItemImageBox;
	Button UploadImageButton, SubmitButton;
	EditText NameBox, DescBox, CategoryBox;

	// Connecting to Firebase Authentication and Database. Also getting Document and Collection References
	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseUser user = auth.getCurrentUser();
	FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
	DocumentReference userDocument = fsdb.collection("UserProfiles").document(user.getUid());
	CollectionReference donateItemCollection = fsdb.collection("DonateItems");
	FirebaseStorage storage = FirebaseStorage.getInstance();

	// Variable Storage
	DonateItemClass itemInfo = new DonateItemClass();
	private Uri imageURI;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donate_item_upload_page);

		Log.d(LOG.getPAGES_LOG(), "Donation Item Upload Page Created");
		Log.d(LOG.getLOCAL_LOG(), "Donation Item Upload Page Created");

		// Linking Frontend to Backend via ID
		ItemImageBox = findViewById(R.id.DonateItemImageUploadForm);
		UploadImageButton = findViewById(R.id.UploadDonateImageButtonForm);
		SubmitButton = findViewById(R.id.DonateItemSubmitButtonForm);
		NameBox = findViewById(R.id.ItemDonateNameForm);
		DescBox = findViewById(R.id.ItemDonateDescriptionForm);
		CategoryBox = findViewById(R.id.ItemDonateCategoryForm);

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
					Toast.makeText(DonateItemUploadPage.this, "Upload Image First", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// Show image on Image View when Image is uploaded
	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == RESULT_OK && data != null){
			ItemImageBox = findViewById(R.id.DonateItemImageUploadForm);
			imageURI = data.getData();
			ItemImageBox.setImageURI(imageURI);
		}
	}

	// Upload Data to Firebase
	public void uploadFireBase(){
		Log.d(LOG.getLOCAL_LOG(), "Upload Function Called");
		StorageReference ref = storage.getReference().child("Donate Images").child(System.currentTimeMillis() + "." + getContentResolver().getType(imageURI).substring(getContentResolver().getType(imageURI).lastIndexOf("/") + 1));
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
			@Override public void onFailure(@NonNull Exception e){
				// If the upload Fails
			}
		});
	}

	// Data Uploading Function
	private void uploadData(){

		// Linking Frontend to Backend via ID
		NameBox = findViewById(R.id.ItemDonateNameForm);
		DescBox = findViewById(R.id.ItemDonateDescriptionForm);
		CategoryBox = findViewById(R.id.ItemDonateCategoryForm);

		// Setting Data into the item Object
		itemInfo.setName(NameBox.getText().toString().trim().toLowerCase());
		itemInfo.setDescription(DescBox.getText().toString().trim());
		itemInfo.setCategory(CategoryBox.getText().toString().trim().toLowerCase());
		itemInfo.setOwnerID(user.getUid());

		// Upload the item to database
		donateItemCollection.add(itemInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
			@Override public void onSuccess(DocumentReference documentReference){
				Log.d(LOG.getDATA_LOG(), "Full Item Uploaded");
			}
		});

		// Getting Location of owner and setting it to the item object
		fsdb.collection("UserProfiles").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@Override public void onSuccess(DocumentSnapshot documentSnapshot){
				new DonateItemLocationUpdate(auth.getCurrentUser().getUid(), documentSnapshot.getDouble("latitude"), documentSnapshot.getDouble("longitude"));
			}
		});

		finish();
	}

	// On Activity Destroy
	@Override protected void onDestroy(){
		Log.d(LOG.getPAGES_LOG(), "Donation Item Upload Page Destroyed");
		super.onDestroy();
	}
}