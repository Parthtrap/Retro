package com.parthtrap.donationapp.HelperClasses;

public class ExchangeItemClass {
    String Name = "";
    String Description = "";
    String Category = "";
    String OwnerID = "";
    String ImageURL = "";
    String WantFor = "";
    String ItemID = "";
    float Rating = 0;
    double latitude = 0, longitude = 0, distance = 0;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }


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
