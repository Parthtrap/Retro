package com.parthtrap.donationapp.NGOPortal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.parthtrap.donationapp.HelperApadters.DonateItemDisplayAdapter;
import com.parthtrap.donationapp.HelperClasses.DonateItemClass;
import com.parthtrap.donationapp.HelperClasses.LoggingClass;
import com.parthtrap.donationapp.MainActivity;
import com.parthtrap.donationapp.R;

public class HomePage extends AppCompatActivity{

	// Log Message Tag
	private final LoggingClass LOG = new LoggingClass("NGO_HOME_PAGE_LOGS");

	// Declaring Front end Components
	TextView HomePageNameDisplay, AdvancedSearch;
	ImageButton SearchButton;
	EditText SearchBox;

	// Connecting to Firebase Authenticator and Database
	FirebaseAuth auth = FirebaseAuth.getInstance();
	FirebaseUser user = auth.getCurrentUser();
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	CollectionReference userCollection = db.collection("UserProfiles");
	CollectionReference itemCollection = db.collection("DonateItems");

	// Adapter for Recycler View
	DonateItemDisplayAdapter adapter;

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page2);

		Log.d(LOG.getPAGES_LOG(), "NGO Home Page Created");
		Log.d(LOG.getLOCAL_LOG(), "NGO Home Page Created");

		// Connecting Front end to Back end
		HomePageNameDisplay = findViewById(R.id.TempTextView2);
		SearchButton = findViewById(R.id.NGOHomePageSearchButton);
		SearchBox = findViewById(R.id.NGOHomePageSearchBar);
		AdvancedSearch = findViewById(R.id.NGOAdvancedSearchButtonHomePage);

		// Advanced Search Button On CLick to redirect to advanced search page
		AdvancedSearch.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Advanced Search Button Clicked... Redirecting...");
				Intent i = new Intent(HomePage.this, AdvancedSearchPage.class);
				startActivity(i);
			}
		});

		// Displaying Name on Home page
		db.collection("NGOProfiles").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@SuppressLint("SetTextI18n") @Override public void onSuccess(DocumentSnapshot documentSnapshot){
				String UserName = documentSnapshot.getString("name");
				Log.d(LOG.getDATA_LOG(), "Username Data Received from Database");

				// If Name is null.. then take to Sign Up Info Input Page
				if(UserName == null){
					Log.d(LOG.getLOCAL_LOG(), "Sign Up Details not completely filled, Redirecting to Info Inputting page");
					Intent i = new Intent(com.parthtrap.donationapp.NGOPortal.HomePage.this, NGOSignUpInfoInputPage.class);
					startActivity(i);
					finish();
				}
				HomePageNameDisplay.setText(getString(R.string.welcome) + " " + UserName);
			}
		});

		// OnClick on the Name .. Ask to LogOut
		HomePageNameDisplay.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Log.d(LOG.getLOCAL_LOG(), "Signing Out");
				// Alert to be Added
				FirebaseAuth.getInstance().signOut();
				Intent i = new Intent(HomePage.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		});

		// Recycler View Building Code
		FirestoreRecyclerOptions<DonateItemClass> options = new FirestoreRecyclerOptions.Builder<DonateItemClass>().setQuery(itemCollection.orderBy("rating", Query.Direction.DESCENDING).limit(10), DonateItemClass.class).build();
		adapter = new DonateItemDisplayAdapter(options);
		RecyclerView recyclerView = findViewById(R.id.NGOHomePageRecyclerView);
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
				FirestoreRecyclerOptions<DonateItemClass> option = new FirestoreRecyclerOptions.Builder<DonateItemClass>().setQuery(query, DonateItemClass.class).build();
				adapter = new DonateItemDisplayAdapter(option);
				RecyclerView recyclerView = findViewById(R.id.NGOHomePageRecyclerView);
				recyclerView.setHasFixedSize(true);
				recyclerView.setLayoutManager(new LinearLayoutManager(com.parthtrap.donationapp.NGOPortal.HomePage.this));
				recyclerView.setAdapter(adapter);

				// Onclick in the Adapter should redirect to Item Display page
				adapter.setOnItemClickListener(new DonateItemDisplayAdapter.OnItemClickListener(){
					@Override public void onItemClick(DocumentSnapshot documentSnapshot, int position){
						Log.d(LOG.getLOCAL_LOG(), "Item Ordered");
						// Ask if Wanna Order it or Skip...
						Toast.makeText(HomePage.this, "Messaging Feature to be Added!", Toast.LENGTH_SHORT).show();
					}
				});
				adapter.startListening();
			}
		});

		// Adapter onClick should redirect to item display
		adapter.setOnItemClickListener(new DonateItemDisplayAdapter.OnItemClickListener(){
			@Override public void onItemClick(DocumentSnapshot documentSnapshot, int position){
				Log.d(LOG.getLOCAL_LOG(), "Item Ordered");
				// Ask if Wanna Order it or Skip...
				Toast.makeText(HomePage.this, "Messaging Feature to be Added!", Toast.LENGTH_SHORT).show();

			}
		});
		// Start the adapter
		adapter.startListening();

	}

	@Override protected void onDestroy(){
		super.onDestroy();
		// Stop Adapter when the page is destroyed
		Log.d(LOG.getPAGES_LOG(), "NGO Home Page Destroyed");
		adapter.stopListening();
	}
}