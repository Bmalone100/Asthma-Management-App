package com.example.fypasthmaapp;

public class User {
    private String name;
    private String email;
    private String id;
    User(String aName, String aEmail, String aId) {
        this.name = aName;
        this.email = aEmail;
        this.id = aId;
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
}
