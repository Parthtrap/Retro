/*
Class Defined to store the Info about user
 */

package com.parthtrap.donationapp.HelperClasses;

public class UserInfoHelper{

	private String name = "";
	private String emailId = "";
	private String phoneNumber = "";
	private String address = "";
	private Boolean publicPhone = true;

	float rating = 0;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getEmailId(){
		return emailId;
	}

	public void setEmailId(String emailId){
		this.emailId = emailId;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public Boolean getPublicPhone(){
		return publicPhone;
	}

	public void setPublicPhone(Boolean publicPhone){
		this.publicPhone = publicPhone;
	}

	public float getRating(){
		return rating;
	}

	public void setRating(float rating){
		this.rating = rating;
	}

	public UserInfoHelper(){
	}

	public UserInfoHelper(String name, String emailId, String phoneNumber, String address, Boolean publicPhone, float rating){
		this.name = name;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.publicPhone = publicPhone;
		this.rating = rating;
	}
}
