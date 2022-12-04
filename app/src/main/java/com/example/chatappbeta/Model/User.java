package com.example.chatappbeta.Model;

public class User {
    String name , email,pass,profile_photo;

    public User() {
    }

    public User(String name, String email, String pass) {  // CONDTRUCOR USHE FILED KA BANA OH JESH ME VALUE PASS HO
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }
}
