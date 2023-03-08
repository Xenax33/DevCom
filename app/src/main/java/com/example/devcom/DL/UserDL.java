package com.example.devcom.DL;

import com.example.devcom.BL.Person;

import java.util.HashMap;

public class UserDL {
    public static HashMap<String , Person> users = new HashMap<>();

    public static void addUser(String ID , Person p)
    {
        users.put(ID  , p);
    }
    public static boolean  isValidUser(String key , String Password)
    {
        if(users.containsKey(key)) {
            Person o = users.get(key);
            if(o.getPassword().equals(Password) )
                return true;
        }
        return false;
    }
    public static Person getPerson(String ID)
    {
        return users.get(ID);
    }

}
