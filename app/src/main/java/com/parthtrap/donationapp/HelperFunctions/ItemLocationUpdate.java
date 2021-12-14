package com.parthtrap.donationapp.HelperFunctions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ItemLocationUpdate {

    public ItemLocationUpdate(String UID, double latitude, double longitude) {
        FirebaseFirestore.getInstance().collection("ExchangeItems").whereEqualTo("ownerID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    FirebaseFirestore.getInstance().collection("ExchangeItems").document(snapshot.getId()).update("longitude", longitude);
                    FirebaseFirestore.getInstance().collection("ExchangeItems").document(snapshot.getId()).update("latitude", latitude);
                }
            }
        });
    }
}
