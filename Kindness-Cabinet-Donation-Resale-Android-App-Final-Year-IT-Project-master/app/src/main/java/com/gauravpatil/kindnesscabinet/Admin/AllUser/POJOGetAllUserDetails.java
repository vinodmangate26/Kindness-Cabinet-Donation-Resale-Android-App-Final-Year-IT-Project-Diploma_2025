package com.gauravpatil.kindnesscabinet.Admin.AllUser;

import android.widget.EditText;

public class POJOGetAllUserDetails {


    String Id,Image,Name,MobileNo,Emailid,Address,Gender,Age,Username;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getEmailid() {
        return Emailid;
    }

    public void setEmailid(String emailid) {
        Emailid = emailid;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public POJOGetAllUserDetails(String id, String image, String name, String mobileNo, String emailid, String gender, String age, String address,String username) {
        Id = id;
        Image = image;
        Name = name;
        MobileNo = mobileNo;
        Emailid = emailid;
        Gender = gender;
        Age = age;
        Address = address;
        Username = username;
    }
}
