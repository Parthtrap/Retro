/*
Class Defined to store the Info about NGO
 */

package com.parthtrap.donationapp.HelperClasses;

public class NGOInfoHelper{

	private String name = "";
	private String emailId = "";
	private String phoneNumber = "";
	private String address = "";

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

	public NGOInfoHelper(){
	}

	public NGOInfoHelper(String name, String emailId, String phoneNumber, String address){
		this.name = name;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
}
