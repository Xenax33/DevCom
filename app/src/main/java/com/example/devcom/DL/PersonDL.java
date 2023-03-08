package com.example.devcom.DL;

import com.example.devcom.BL.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonDL {
    public static HashMap<Person , ArrayList<Person>> Graph = new HashMap<>();
    public static ArrayList<Person> vertices = new ArrayList<>();
    public static void AddVertex(Person p , ArrayList<Person> frends)
    {
        Graph.put(p , frends);
    }
    public static void addEdge(Person user , Person frend)
    {
        ArrayList<Person> lst = Graph.get(user);
        lst.add(frend);
        Graph.put(user , lst);
        lst = Graph.get(frend);
        lst.add(user);
        Graph.put(frend , lst);
    }
    public static ArrayList<Person> getFriends(Person p)
    {
        return Graph.get(p);
    }

}
