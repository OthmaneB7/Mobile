package com.example.bariki_othmane;

import java.io.Serializable;

public class UserHelper implements Serializable {
        String fullName,email,username,password,gender,birthday,phoneNumber,cin,numIimmatriculation,ext;
        String image_URL;
    private double Latitude,Longitude;
    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public UserHelper(){

    }
    public String getCin() {
        return cin;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getExt() {
        return ext;
    }

    public String getNumIimmatriculation() {
        return numIimmatriculation;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public UserHelper(String fullName, String email, String username, String gender, String birthday, String phoneNumber, String cin, String numIimmatriculation, String ext, String image_URL) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.cin = cin;
        this.numIimmatriculation = numIimmatriculation;
        this.ext = ext;
        this.image_URL = image_URL;
    }

    public void setNumIimmatriculation(String numero_immatriculation) {
        this.numIimmatriculation = numero_immatriculation;
    }

    public UserHelper(String fullName, String email, String username, String password) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
