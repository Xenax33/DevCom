package com.example.devcom.BL;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Person {
    String dataBaseID;
    String name;
    String ID;
    String password;
    String profile_pic;
    int followersCount;
    public int getFollowersCount() {
        return followersCount;
    }
    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
    public String getDataBaseID() {return dataBaseID;}
    public void setDataBaseID(String dataBaseID) {this.dataBaseID = dataBaseID;}
    public String getProfile_pic() {
        return profile_pic;
    }
    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getPassword() {return password;}
    public void setPassword(String password) {
        this.password = password;
    }
    public Person(String ID , String password ,  String name) {
        this.name = name;
        this.ID = ID;
        this.password = password;
    }
    public Person(){};

}
