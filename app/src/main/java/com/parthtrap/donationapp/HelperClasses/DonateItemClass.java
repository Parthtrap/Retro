/*
Class Defined for items that are up for Donation.
*/

package com.parthtrap.donationapp.HelperClasses;

public class DonateItemClass extends Items{

	public String getItemID(){
		return itemID;
	}
	public void setItemID(String itemID){
		this.itemID = itemID;
	}
	private String itemID;

	public DonateItemClass(){
	}

	public DonateItemClass(String name, String description, String category, String ownerID, String imageURL){
		setName(name);
		setDescription(description);
		setCategory(category);
		setOwnerID(ownerID);
		setImageURL(imageURL);
	}
}
