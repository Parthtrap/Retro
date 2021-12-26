/*
Call This Function when you want to Update the Coordinates onto Database.
*/

package com.parthtrap.donationapp.HelperFunctions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DonateItemLocationUpdate{

	public DonateItemLocationUpdate(String UID, double latitude, double longitude){

		// Updating Location to Database
		FirebaseFirestore.getInstance().collection("DonateItems").whereEqualTo("ownerID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
			@Override public void onSuccess(QuerySnapshot queryDocumentSnapshots){
				for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
					FirebaseFirestore.getInstance().collection("DonateItems").document(snapshot.getId()).update("longitude", longitude);
					FirebaseFirestore.getInstance().collection("DonateItems").document(snapshot.getId()).update("latitude", latitude);
				}
			}
		});
	}
}
