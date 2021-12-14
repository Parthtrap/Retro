
package com.parthtrap.donationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.parthtrap.donationapp.HelperApadters.AdvancedSearchItemAdapter;
import com.parthtrap.donationapp.HelperClasses.ExchangeItemClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdvancedSearchPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner SearchSpinner;
    EditText AdvancedSearchBar;
    ImageButton SearchButton;
    String SearchedText;
    RecyclerView SearchDisplay;
    AdvancedSearchItemAdapter SearchAdapter;
    String SearchOptionText = "Item Name";
    List<ExchangeItemClass> itemStorage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search_page);
        AdvancedSearchBar = findViewById(R.id.SearchBarAdvanceSearch);
        SearchButton = findViewById(R.id.AdvancedSearchButton);
        SearchDisplay = findViewById(R.id.AdvancedSearchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        SearchDisplay.setLayoutManager(layoutManager);
        SearchSpinner = findViewById(R.id.DropDownListAdvancedSearch);
        SpinnerDefining();

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchedText = AdvancedSearchBar.getText().toString().toLowerCase();
                if (!SearchedText.isEmpty()) {


                    if (SearchOptionText.equals("Item Name")) {
                        FirebaseFirestore.getInstance().collection("ExchangeItems").whereGreaterThanOrEqualTo("name", SearchedText).whereLessThanOrEqualTo("name", SearchedText + '\uf8ff').get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                itemStorage.clear();
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    ExchangeItemClass item = snapshot.toObject(ExchangeItemClass.class);
                                    item.setItemID(snapshot.getId());
                                    itemStorage.add(item);
                                }

                                itemStorage.sort(new Comparator<ExchangeItemClass>() {
                                    @Override
                                    public int compare(ExchangeItemClass o1, ExchangeItemClass o2) {
                                        if (o1.getRating() > o2.getRating())
                                            return -1;
                                        else if (o1.getRating() < o2.getRating())
                                            return 1;
                                        else
                                            return o2.getName().compareTo(o1.getName());
                                    }
                                });
                                Log.d("Items", itemStorage.toString());
                                AdapterDefining();
                            }
                        });
                    } else if (SearchOptionText.equals("Item Category")) {
                        FirebaseFirestore.getInstance().collection("ExchangeItems").whereGreaterThanOrEqualTo("category", SearchedText).whereLessThanOrEqualTo("category", SearchedText + '\uf8ff').get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                itemStorage.clear();
                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    ExchangeItemClass item = snapshot.toObject(ExchangeItemClass.class);
                                    item.setItemID(snapshot.getId());
                                    itemStorage.add(item);
                                }
                                itemStorage.sort(new Comparator<ExchangeItemClass>() {
                                    @Override
                                    public int compare(ExchangeItemClass o1, ExchangeItemClass o2) {
                                        if (o1.getRating() > o2.getRating())
                                            return -1;
                                        else if (o1.getRating() < o2.getRating())
                                            return 1;
                                        else
                                            return o2.getName().compareTo(o1.getName());
                                    }
                                });
                                Log.d("Items", itemStorage.toString());
                                AdapterDefining();
                            }
                        });
                    } else
                        Toast.makeText(AdvancedSearchPage.this, "Invalid Search", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(AdvancedSearchPage.this, "Search Something", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AdapterDefining() {
        SearchAdapter = new AdvancedSearchItemAdapter(itemStorage);
        SearchAdapter.setOnItemClickListener(new AdvancedSearchItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ExchangeItemClass item, int position) {
                Intent intent = new Intent(AdvancedSearchPage.this,
                        ExchangeItemViewPage.class);
                intent.putExtra("id", item.getItemID());
                intent.putExtra("idfrom", "null");
                startActivity(intent);
            }
        });
        SearchDisplay.setAdapter(SearchAdapter);
        SearchAdapter.notifyDataSetChanged();
    }

    public void SpinnerDefining() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AdvancedSearchPage.this
                , R.layout.custom_spinner_advanced_search
                , getResources().getStringArray(R.array.AdvancedSearchSpinnerData));
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_advanced_search_drop_down);
        SearchSpinner.setAdapter(spinnerAdapter);
        SearchSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SearchOptionText = parent.getItemAtPosition(position).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        if (SearchedText.isEmpty())
            super.onBackPressed();
        else {
            itemStorage.clear();
            SearchedText = "";
            AdvancedSearchBar.setText("");
            SearchAdapter = new AdvancedSearchItemAdapter(itemStorage);
            SearchDisplay.setAdapter(SearchAdapter);
            SearchAdapter.notifyDataSetChanged();
        }

    }
}
