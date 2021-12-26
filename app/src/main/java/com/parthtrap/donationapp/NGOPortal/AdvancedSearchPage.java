/*
Advanced Search Page.

People can Search for items via their name or their category.
Its slower than Normal Search but it can search items by their starting substring and then sort them by their ratings.
*/

package com.parthtrap.donationapp.NGOPortal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.parthtrap.donationapp.HelperApadters.AdvancedSearchDonateItemAdapter;
import com.parthtrap.donationapp.HelperClasses.DonateItemClass;
import com.parthtrap.donationapp.R;
import com.parthtrap.donationapp.UserPortal.ExchangeItemViewPage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdvancedSearchPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

	// Defining Front End Components
	Spinner searchSpinner;
	EditText advancedSearchBar;
	ImageButton searchButton;
	String searchedText;
	RecyclerView searchDisplay;
	AdvancedSearchDonateItemAdapter searchAdapter;

	String searchOptionText = "Item Name"; // Default Search Option
	List<DonateItemClass> itemStorage = new ArrayList<>(); // ArrayList to store all items

	@Override protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_search_page);

		// Linking Frontend to Backend via ID
		advancedSearchBar = findViewById(R.id.SearchBarAdvanceSearch);
		searchButton = findViewById(R.id.AdvancedSearchButton);
		searchDisplay = findViewById(R.id.AdvancedSearchRecyclerView);
		searchSpinner = findViewById(R.id.DropDownListAdvancedSearch);

		// Layout Manager Defining and Spinner Defining
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(RecyclerView.VERTICAL);
		searchDisplay.setLayoutManager(layoutManager);
		SpinnerDefining();

		// Onclick on the Search Button
		searchButton.setOnClickListener(new View.OnClickListener(){
			@Override public void onClick(View v){
				// String stored in a variable and checked if empty or not
				searchedText = advancedSearchBar.getText().toString().toLowerCase();
				if(!searchedText.isEmpty()){

					// If Searched by Item Name
					if(searchOptionText.equals("Item Name")){

						// Getting all items with name starting with substring 'searchedText'
						FirebaseFirestore.getInstance().collection("DonateItems").whereGreaterThanOrEqualTo("name", searchedText).whereLessThanOrEqualTo("name", searchedText + '\uf8ff').get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
							@Override public void onSuccess(QuerySnapshot queryDocumentSnapshots){

								// Refreshing the List and Filling it up again
								itemStorage.clear();
								for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
									DonateItemClass item = snapshot.toObject(DonateItemClass.class); // Convert to Exchange Class Items
									item.setItemID(snapshot.getId()); // Setting Item ID
									itemStorage.add(item); // Adding Item to ArrayList to be Sorted later
								}

								// Sorting the List of Items according to rating
								itemStorage.sort(new Comparator<DonateItemClass>(){
									@Override public int compare(DonateItemClass o1, DonateItemClass o2){
										if(o1.getRating() > o2.getRating())
											return -1;
										else if(o1.getRating() < o2.getRating())
											return 1;
										else
											return o2.getName().compareTo(o1.getName());
									}
								});
								// Log.d("AdvSorting", itemStorage.toString());
								AdapterDefining(); // Updating view Adapter
							}
						});
					}
					// If Searched by Item Category
					else if(searchOptionText.equals("Item Category")){

						// Getting all items with name starting with substring 'searchedText'
						FirebaseFirestore.getInstance().collection("DonateItems").whereGreaterThanOrEqualTo("category", searchedText).whereLessThanOrEqualTo("category", searchedText + '\uf8ff').get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
							@Override public void onSuccess(QuerySnapshot queryDocumentSnapshots){

								// Refreshing the List and Filling it up again
								itemStorage.clear();
								for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
									DonateItemClass item = snapshot.toObject(DonateItemClass.class); // Convert to Exchange Class Items
									item.setItemID(snapshot.getId()); // Setting Item ID
									itemStorage.add(item); // Adding Item to ArrayList to be Sorted later
								}

								// Sorting the List of Items according to rating
								itemStorage.sort(new Comparator<DonateItemClass>(){
									@Override public int compare(DonateItemClass o1, DonateItemClass o2){
										if(o1.getRating() > o2.getRating())
											return -1;
										else if(o1.getRating() < o2.getRating())
											return 1;
										else
											return o2.getName().compareTo(o1.getName());
									}
								});
								// Log.d("AdvSorting", itemStorage.toString());
								AdapterDefining(); // Updating view Adapter
							}
						});
					}

					// Invalid Choice... just in Case
					else
						Toast.makeText(AdvancedSearchPage.this, "Invalid Search", Toast.LENGTH_SHORT).show();
				}

				// If Searched string is Empty
				else
					Toast.makeText(AdvancedSearchPage.this, "Nothing to Search", Toast.LENGTH_SHORT).show();
			}
		});
	}

	// Putting The Arraylist in the Recycler view by updating the Adapter
	private void AdapterDefining(){
		searchAdapter = new AdvancedSearchDonateItemAdapter(itemStorage);

		// Setting onclicks on each Entry
		searchAdapter.setOnItemClickListener(new AdvancedSearchDonateItemAdapter.OnItemClickListener(){
			@Override public void onItemClick(DonateItemClass item, int position){
				Toast.makeText(AdvancedSearchPage.this, "Item Clicked", Toast.LENGTH_SHORT).show();
			}
		});

		// Adapter Connected to Recycler View and Refreshed
		searchDisplay.setAdapter(searchAdapter);
		searchAdapter.notifyDataSetChanged();
	}

	// Defining the DropDown List.. aka Spinner
	public void SpinnerDefining(){
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AdvancedSearchPage.this, R.layout.custom_spinner_advanced_search, getResources().getStringArray(R.array.AdvancedSearchSpinnerData));
		spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_advanced_search_drop_down);
		searchSpinner.setAdapter(spinnerAdapter);
		searchSpinner.setOnItemSelectedListener(this);
	}

	// On Pressing an item on the Spinner... Value stored in a variable
	@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		searchOptionText = parent.getItemAtPosition(position).toString();
	}

	// Default Selection
	@Override public void onNothingSelected(AdapterView<?> parent){
		// Already Defined beforehand
	}

	// On Pressing back
	@Override public void onBackPressed(){

		// If Searchbar is Empty... Take back to previous activity
		if(searchedText.isEmpty()){
			super.onBackPressed();
		}

		// If Search Bar is not empty... Empty it on back Press
		else{
			itemStorage.clear();
			searchedText = "";
			advancedSearchBar.setText("");
			searchAdapter = new AdvancedSearchDonateItemAdapter(itemStorage);
			searchDisplay.setAdapter(searchAdapter);
			searchAdapter.notifyDataSetChanged();
		}
	}
}
