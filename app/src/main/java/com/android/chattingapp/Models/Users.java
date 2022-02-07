package com.android.chattingapp.Models;

public class Users {
     String uid;
     String address;
     String localaddress;
     String pin;
     String localpin;
     String age;
     String dob;
     String email;
     String gender;
     String imageUri;
     String name;
     String phone;
     String status;
     String lastmessage;
     String bloodgroup;
     String timestamp;
     String password;
     String type;

    public Users() {
    }



    public Users(String uid, String address, String age,String bloodgroup, String dob, String email, String gender, String imageUri,String localaddress,String localpin,String name, String phone,String pin, String status, String timestamp,String password,String type) {
        this.uid = uid;
        this.address = address;
        this.age = age;
        this.bloodgroup = bloodgroup;
        this.dob = dob;
        this.email = email;
        this.gender = gender;
        this.imageUri = imageUri;
        this.localaddress = localaddress;
        this.localpin = localpin;
        this.name = name;
        this.phone = phone;
        this.pin = pin;
        this.status=status;
        this.lastmessage = lastmessage;
        this.timestamp = timestamp;
        this.password = password;
        this.type = type;



    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocaladdress() {
        return localaddress;
    }

    public void setLocaladdress(String localaddress) {
        this.localaddress = localaddress;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getLocalpin() {
        return localpin;
    }

    public void setLocalpin(String localpin) {
        this.localpin = localpin;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
