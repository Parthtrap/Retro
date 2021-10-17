package com.parthtrap.donationapp.HelperClasses;

import com.google.firebase.firestore.DocumentReference;

public class ModelClass {
    DocumentReference name;

    public ModelClass() {
    }

    public DocumentReference getName() {
        return name;
    }

    public void setName(DocumentReference name) {
        this.name = name;
    }

    public ModelClass(DocumentReference name) {
        this.name = name;
    }
}

