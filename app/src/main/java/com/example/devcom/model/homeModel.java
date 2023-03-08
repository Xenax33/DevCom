package com.example.devcom.model;

public class homeModel {
    int profileImage , postImage;
    String name;
    String date;
    int commment;
    String post;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getPost() {
        return post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    public homeModel(int profileImage, int postImage, String name, int commment , String post , String date) {
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.name = name;
        this.commment = commment;
        this.post = post;
        this.date  = date;
    }
    public int getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(int profile) {
        this.profileImage = profile;
    }
    public int getPostImage() {
        return postImage;
    }
    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCommment() {
        return commment;
    }
    public void setCommment(int commment) {
        this.commment = commment;
    }
}
