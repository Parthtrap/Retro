/*
Parent Class Defined for items.
*/

package com.parthtrap.donationapp.HelperClasses;

public class Items{

	private String name = "";
	private String description = "";
	private String category = "";
	private String ownerID = "";
	private String imageURL = "";
	private float rating = 0;

	public float getRating(){
		return rating;
	}

	public void setRating(float rating){
		this.rating = rating;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getCategory(){
		return category;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getOwnerID(){
		return ownerID;
	}

	public void setOwnerID(String ownerID){
		this.ownerID = ownerID;
	}

	public String getImageURL(){
		return imageURL;
	}

	public void setImageURL(String imageURL){
		this.imageURL = imageURL;
	}

}
