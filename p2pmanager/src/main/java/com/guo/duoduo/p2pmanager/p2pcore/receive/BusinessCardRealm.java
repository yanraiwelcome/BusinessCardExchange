package com.guo.duoduo.p2pmanager.p2pcore.receive;

import java.io.Serializable;

/**
 * Created by CrazyCoder on 6/8/2016.
 */

public class BusinessCardRealm implements Serializable {
    private boolean isOwn=true;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;
    public boolean isOwn() {
        return isOwn;
    }

    public void setOwn(boolean own) {
        isOwn = own;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDirectPhone() {
        return directPhone;
    }

    public void setDirectPhone(String directPhone) {
        this.directPhone = directPhone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public String getPhotocompanylogo() {
        return photocompanylogo;
    }

    public void setPhotocompanylogo(String photocompanylogo) {
        this.photocompanylogo = photocompanylogo;
    }

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

    public String getMyUniqueFilename() {
        return myUniqueFilename;
    }

    public void setMyUniqueFilename(String myUniqueFilename) {
        this.myUniqueFilename = myUniqueFilename;
    }

    private String myUniqueFilename;

    public BusinessCardRealm() { }



}