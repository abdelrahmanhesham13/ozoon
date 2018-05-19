package com.nadernabil216.wlaashal.Model.Objects;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public class User {
    private String id ="";
    private String name="" ;
    private String username="" ;
    private String mobile="" ;
    private String token="" ;
    private String role="" ;
    private String activate="" ;
    private String image="";

    public User() {
    }

    public User(String id, String name, String username, String mobile, String token, String role, String activate) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.token = token;
        this.role = role;
        this.activate = activate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }
}
