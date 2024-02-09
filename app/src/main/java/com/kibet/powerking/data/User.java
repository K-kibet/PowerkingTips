package com.kibet.powerking.data;

public class User {
    String username;
    String fullName;
    String email;
    String password;
    String image;
    Boolean isPremium = false;
    public User(String username,
                String fullName,
                String email,
                String password,
                String image,
                Boolean isPremium) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.image = image;
        this.isPremium = isPremium;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public Boolean getPremium() {
        return isPremium;
    }
}
