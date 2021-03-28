package com.example.fypasthmaapp;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String id;
    ArrayList<User> users = new ArrayList<>();

    public User(String aId, String aName, String aEmail) {
        this.id = aId;
        this.name = aName;
        this.email = aEmail;

    }

    public String getName(){ return name; }
    public void setName(){
        String aName = name;
    }

    public String getEmail(){return email;}
    public void setEmail(){
        String aEmail = email;
    }

    public String getId() { return id; }
    public void setId(){
        String aId = id;
    }

    public void updateUsers(User auser){
        users.add(auser);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

}
