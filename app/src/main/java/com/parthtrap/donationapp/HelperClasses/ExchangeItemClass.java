/*
Class Defined for items that are up for Exchange.
*/

package com.parthtrap.donationapp.HelperClasses;

public class ExchangeItemClass extends Items{

	private String wantFor = "";
	private String itemID = "";
	private double latitude = 0, longitude = 0;

	public double getLatitude(){
		return latitude;
	}

	public void setLatitude(double latitude){
		this.latitude = latitude;
	}

	public double getLongitude(){
		return longitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}

	public String getItemID(){
		return itemID;
	}

	public void setItemID(String itemID){
		this.itemID = itemID;
	}

	@Override public String toString(){
		return "ExchangeItemInfo{" + "Name='" + getName() + '\'' + ", Description='" + getDescription() + '\'' + ", Category='" + getCategory() + '\'' + ", OwnerID='" + getOwnerID() + '\'' + ", ImageURL='" + getImageURL() + '\'' + ", WantFor='" + wantFor + '\'' + '}';
	}
	public String getWantFor(){
		return wantFor;
	}

	public void setWantFor(String wantFor){
		this.wantFor = wantFor;
	}

	public ExchangeItemClass(){
	}

	public ExchangeItemClass(String name, String description, String category, String ownerID, String imageURL, String wantFor){
		setName(name);
		setDescription(description);
		setCategory(category);
		setOwnerID(ownerID);
		setImageURL(imageURL);
		this.wantFor = wantFor;
	}
}
