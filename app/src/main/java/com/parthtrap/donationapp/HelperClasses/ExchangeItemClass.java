package com.parthtrap.donationapp.HelperClasses;

public class ExchangeItemClass {
    String Name = "";
    String Description = "";
    String Category = "";
    String OwnerID = "";
    String ImageURL = "";
    String WantFor = "";
    float Rating = 0;


    @Override
    public String toString() {
        return "ExchangeItemInfo{" +
                "Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Category='" + Category + '\'' +
                ", OwnerID='" + OwnerID + '\'' +
                ", ImageURL='" + ImageURL + '\'' +
                ", WantFor='" + WantFor + '\'' +
                '}';
    }


    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        this.Rating = rating;
    }

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

    public String getWantFor() {
        return WantFor;
    }

    public void setWantFor(String wantFor) {
        WantFor = wantFor;
    }

    public ExchangeItemClass() {
    }

    public ExchangeItemClass(String name, String description, String category, String ownerID, String imageURL, String wantFor) {
        Name = name;
        Description = description;
        Category = category;
        OwnerID = ownerID;
        ImageURL = imageURL;
        WantFor = wantFor;
    }
}
