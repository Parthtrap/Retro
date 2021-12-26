package com.parthtrap.donationapp.UserPortal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.parthtrap.donationapp.AuthenticationPages.NGOSignUpInfoInputPage;
import com.parthtrap.donationapp.AuthenticationPages.SignUpInfoInputPage;
import com.parthtrap.donationapp.HelperApadters.ExchangeItemDisplayAdapter;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.ProfileDisplay;
import com.parthtrap.donationapp.R;

public class HomePage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("USER_HOME_PAGE_LOGS");

	// Declaring Front end Components
	TextView HomePageNameDisplay, AdvancedSearch;
	Button ExchangeItem, DonateItem;
	ImageButton SearchButton;
	EditText SearchBox;

	// Connecting to Firebase Authenticator and Database
	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseUser user = auth.getCurrentUser();
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	CollectionReference userCollection = db.collection("UserProfiles");
	CollectionReference itemCollection = db.collection("ExchangeItems");

	// Adapter for Recycler View
	ExchangeItemDisplayAdapter adapter;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		Log.d(LOG.getPAGES_LOG(), "User Home Page Created");
		Log.d(LOG.getLOCAL_LOG(), "User Home Page Created");

		// Connecting Front end to Back end
		HomePageNameDisplay = findViewById(R.id.TempTextView);
		ExchangeItem = findViewById(R.id.ExchangeItemButtonHomePage);
		DonateItem = findViewById(R.id.DonateItemButtonHomePage);
		SearchButton = findViewById(R.id.HomePageSearchButton);
		SearchBox = findViewById(R.id.HomePageSearchBar);
		AdvancedSearch = findViewById(R.id.AdvancedSearchButtonHomePage);

		// Advanced Search Button On CLick to redirect to advanced search page
		AdvancedSearch.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Advanced Search Button Clicked... Redirecting...");
				Intent i = new Intent(HomePage.this, AdvancedSearchPage.class);
				startActivity(i);
			}
		});

		// Displaying Name on Home page
		db.collection("UserProfiles").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@SuppressLint("SetTextI18n") @Override public void onSuccess(DocumentSnapshot documentSnapshot){
				String UserName = documentSnapshot.getString("name");
				Log.d(LOG.getDATA_LOG(), "Username Data Received from Database");

				// If Name is null.. then take to Sign Up Info Input Page
				if(UserName == null){
					// Not a User.. Check for NGO
					db.collection("NGOProfiles").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
						@Override public void onSuccess(DocumentSnapshot documentSnapshot){
							String UserName2 = documentSnapshot.getString("name");
							// Its an NGO... but no Info Filled
							if(UserName2.equals("null")){
								Log.d(LOG.getLOCAL_LOG(), "NGO Sign Up Details incomplete");
								Intent i = new Intent(HomePage.this, NGOSignUpInfoInputPage.class);
								startActivity(i);
								finish();
							}else{
								Log.d(LOG.getLOCAL_LOG(), "NGO Signed In... Redirect to NGO Home Page");
								Intent i = new Intent(HomePage.this, com.parthtrap.donationapp.NGOPortal.HomePage.class);
								startActivity(i);
								finish();
							}
						}
					});
				}else if(UserName.equals("null")){
					Log.d(LOG.getLOCAL_LOG(), "User Sign Up Details incomplete");
					Intent i = new Intent(HomePage.this, SignUpInfoInputPage.class);
					startActivity(i);
					finish();
				}
				Log.d(LOG.getLOCAL_LOG(), "User Logged In");
				HomePageNameDisplay.setText(getString(R.string.welcome) + " " + UserName);
			}
		});

		// OnClick on the Name to redirect to profile page
		HomePageNameDisplay.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Name Clicked... Redirect to Profile Page");
				Intent i = new Intent(HomePage.this, ProfileDisplay.class);
				i.putExtra("id", user.getUid());
				startActivity(i);
			}
		});

		// OnClick on Exchange Item button to redirect to Exchange Item Form
		ExchangeItem.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Exchange Item Clicked... Redirect to Item upload page");
				Intent i = new Intent(HomePage.this, ExchangeItemUploadPage.class);
				startActivity(i);
			}
		});

		// OnClick on Donate Item button to redirect to Donate Item Form
		DonateItem.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Donate Item Clicked... Redirect to Item upload page");
				Intent i = new Intent(HomePage.this, DonateItemUploadPage.class);
				startActivity(i);
			}
		});

		// Recycler View Building Code
		FirestoreRecyclerOptions<ExchangeItemClass> options = new FirestoreRecyclerOptions.Builder<ExchangeItemClass>().setQuery(itemCollection.orderBy("rating", Query.Direction.DESCENDING).limit(10), ExchangeItemClass.class).build();
		adapter = new ExchangeItemDisplayAdapter(options);
		RecyclerView recyclerView = findViewById(R.id.HomePageRecyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);

		// Search Button onClick
		SearchButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Search Button Pressed");
				String SearchedString = SearchBox.getText().toString().toLowerCase().trim();
				Query query = null;
				// If Searched string is null, Show all the items
				if(SearchedString.isEmpty()){
					query = itemCollection.orderBy("rating", Query.Direction.DESCENDING);
				}
				// Else show what's being searched
				else{
					query = itemCollection.whereEqualTo("name", SearchedString).orderBy("rating", Query.Direction.DESCENDING);
				}

				// Get items using the above query and update the Adapter
				FirestoreRecyclerOptions<ExchangeItemClass> option = new FirestoreRecyclerOptions.Builder<ExchangeItemClass>().setQuery(query, ExchangeItemClass.class).build();
				adapter = new ExchangeItemDisplayAdapter(option);
				RecyclerView recyclerView = findViewById(R.id.HomePageRecyclerView);
				recyclerView.setHasFixedSize(true);
				recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
				recyclerView.setAdapter(adapter);

				// Onclick in the Adapter should redirect to Item Display page
				adapter.setOnItemClickListener(new ExchangeItemDisplayAdapter.OnItemClickListener(){
					@Override public void onItemClick(DocumentSnapshot documentSnapshot, int position){
						Log.d(LOG.getLOCAL_LOG(), "Item Clicked... Take to Item Display Page");
						Intent intent = new Intent(HomePage.this, ExchangeItemViewPage.class);
						intent.putExtra("id", documentSnapshot.getId());
						intent.putExtra("idfrom", "null");
						startActivity(intent);
						// finish();
					}
				});
				adapter.startListening();
			}
		});

		// Adapter onClick should redirect to item display
		adapter.setOnItemClickListener(new ExchangeItemDisplayAdapter.OnItemClickListener(){
			@Override public void onItemClick(DocumentSnapshot documentSnapshot, int position){
				Log.d(LOG.getLOCAL_LOG(), "Item Clicked... Take to Item Display Page");
				Intent intent = new Intent(HomePage.this, ExchangeItemViewPage.class);
				intent.putExtra("id", documentSnapshot.getId());
				intent.putExtra("idfrom", "null");
				startActivity(intent);
			}
		});
		// Start the adapter
		adapter.startListening();

	}

	@Override protected void onDestroy(){
		super.onDestroy();
		// Stop Adapter when the page is destroyed
		Log.d(LOG.getPAGES_LOG(), "User Home Page Destroyed");
		adapter.stopListening();
	}
}