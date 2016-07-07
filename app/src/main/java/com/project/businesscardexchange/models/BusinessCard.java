package com.project.businesscardexchange.models;

/**
 * Created by Yan on 2/4/2016.
 */
public class BusinessCard {
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getIsOwn() {
        return isOwn;
    }

    public void setIsOwn(int isOwn) {
        this.isOwn = isOwn;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String timestamp;
    private int isOwn=1;

    private String name;
    private String companyName;
    private String websiteUrl;
    private String phone;
    private String emailAddress;
    private String directPhone;
    private String photo;
    private String post;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String photocompanylogo;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    private String countryName;

   public BusinessCard(){

   }

    public BusinessCard(String companyName, String emailAddress, String name, String phone, String websiteUrl, String directPhone, String street, String photo, String post, String city, String state, String zipCode,String photocompanylogo) {
        this.companyName = companyName;
        this.emailAddress = emailAddress;
        this.name = name;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
        this.directPhone = directPhone;
        this.street = street;
        this.photo = photo;

        this.post = post;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.photocompanylogo = photocompanylogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setDirectPhone(String directPhone){this.directPhone = directPhone;}

    public void setStreet(String street){
        this.street = street;
    }

    public String getDirectPhone(){return directPhone;}
    public String getStreet(){return street;}
    public String getPhoto(){return photo;}

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getPhotocompanylogo(){
        return photocompanylogo;
    }
    public void setPhotocompanylogo(String photocompanylogo){
        this.photocompanylogo = photocompanylogo;
    }
}
