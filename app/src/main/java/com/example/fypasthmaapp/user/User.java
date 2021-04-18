package com.example.fypasthmaapp.user;

/**
 * POJO for creating a user object
 */
public class User {
    private String name;
    private String email;
    private String id;

    public User(String aId, String aName, String aEmail) {
        this.id = aId;
        this.name = aName;
        this.email = aEmail;

    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getId() {
        return id;
    }

}
