package com.parthtrap.donationapp.HelperClasses;

public class DonateItemClass {
    String Name = "";
    String Description = "";
    String Category = "";
    String OwnerID = "";
    String ImageURL = "";
    float Rating = 0;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public DonateItemClass() {
    }

    public DonateItemClass(String name, String description, String category, String ownerID, String imageURL, float rating) {
        Name = name;
        Description = description;
        Category = category;
        OwnerID = ownerID;
        ImageURL = imageURL;
        Rating = rating;
    }
}
