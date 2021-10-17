package com.parthtrap.donationapp.HelperClasses;

public class UserInfoHelper {


    String Name = "";
    String EmailId = "";
    String PhoneNumber = "";
    String Address = "";
    Boolean PublicPhone = true;
    float Rating = 0;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Boolean getPublicPhone() {
        return PublicPhone;
    }

    public void setPublicPhone(Boolean publicPhone) {
        PublicPhone = publicPhone;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public UserInfoHelper() {
    }

    public UserInfoHelper(String name, String emailId, String phoneNumber, String address, Boolean publicPhone, float rating) {
        Name = name;
        EmailId = emailId;
        PhoneNumber = phoneNumber;
        Address = address;
        PublicPhone = publicPhone;
        Rating = rating;
    }
}
