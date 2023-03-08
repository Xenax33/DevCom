package com.example.devcom.BL;

import java.util.ArrayList;

public class Post {
    String postDatabaseID;
    String postImage;
    String postedBy;
    String postDescription;
    long postedAt;
    public Post(String postDatabaseID, String postImage, String postedBy, String postDescription, long postedAt) {
        this.postDatabaseID = postDatabaseID;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postedAt = postedAt;
    }
    public Post() {
    }
    public String getPostDatabaseID() {
        return postDatabaseID;
    }
    public void setPostDatabaseID(String postDatabaseID) {
        this.postDatabaseID = postDatabaseID;
    }
    public String getPostImage() {
        if(postImage == null)
            return "";
        if(postImage.isEmpty())
            return "";
        return postImage;
    }
    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
    public String getPostedBy() {
        return postedBy;
    }
    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
    public String getPostDescription() {
        return postDescription;
    }
    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
    public long getPostedAt() {
        return postedAt;
    }
    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }
}

