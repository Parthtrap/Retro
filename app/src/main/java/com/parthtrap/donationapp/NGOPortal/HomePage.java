package com.parthtrap.donationapp.NGOPortal;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.storage.FirebaseStorage;
import com.parthtrap.donationapp.AuthenticationPages.NGOSignUpInfoInputPage;
import com.parthtrap.donationapp.HelperApadters.DonateItemDisplayAdapter;
import com.parthtrap.donationapp.HelperApadters.ExchangeItemDisplayAdapter;
import com.parthtrap.donationapp.HelperClasses.DonateItemClass;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;
import com.parthtrap.donationapp.MainActivity;
import com.parthtrap.donationapp.ProfileDisplay;
import com.parthtrap.donationapp.R;
import com.parthtrap.donationapp.UserPortal.ExchangeItemViewPage;

public class HomePage extends AppCompatActivity{

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

		// Connecting Front end to Back end
		HomePageNameDisplay = findViewById(R.id.TempTextView2);
		SearchButton = findViewById(R.id.NGOHomePageSearchButton);
		SearchBox = findViewById(R.id.NGOHomePageSearchBar);
		AdvancedSearch = findViewById(R.id.NGOAdvancedSearchButtonHomePage);

		// Advanced Search Button On CLick to redirect to advanced search page
		AdvancedSearch.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				Intent i = new Intent(HomePage.this, AdvancedSearchPage.class);
				startActivity(i);
			}
		});

		// Displaying Name on Home page
		db.collection("NGOProfiles").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
			@SuppressLint("SetTextI18n") @Override public void onSuccess(DocumentSnapshot documentSnapshot){
				String UserName = documentSnapshot.getString("name");

				// If Name is null.. then take to Sign Up Info Input Page
				if(UserName == null){
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
						// Ask if Wanna Order it or Skip...
						Toast.makeText(HomePage.this, "Item Clicked", Toast.LENGTH_SHORT).show();
					}
				});
				adapter.startListening();
			}
		});

		// Adapter onClick should redirect to item display
		adapter.setOnItemClickListener(new DonateItemDisplayAdapter.OnItemClickListener(){
			@Override public void onItemClick(DocumentSnapshot documentSnapshot, int position){
				// Ask if Wanna Order it or Skip...
				Toast.makeText(HomePage.this, "Item Clicked", Toast.LENGTH_SHORT).show();

			}
		});
		// Start the adapter
		adapter.startListening();

	}

	@Override protected void onDestroy(){
		super.onDestroy();
		// Stop Adapter when the page is destroyed
		adapter.stopListening();
	}
}