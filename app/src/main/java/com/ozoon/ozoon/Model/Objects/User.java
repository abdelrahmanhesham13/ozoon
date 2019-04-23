package com.ozoon.ozoon.Model.Objects;

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
    private String points ="";
    private String rate ="";
    private String code;
    private String ads ="";
    private int ads_count ;
    private String driver = "" ;
    private String delivery = "";
    private int follower = 0;
    private int following = 0;
    private String followed = "";

    public User() {
    }



    public User(String id, String name, String username, String mobile, String token, String role, String activate, String image, String points, String rate, String ads, int ads_count, String driver, String delivery) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.token = token;
        this.role = role;
        this.activate = activate;
        this.image = image;
        this.points = points;
        this.rate = rate;
        this.ads = ads;
        this.ads_count = ads_count;
        this.driver = driver;
        this.delivery = delivery;
    }

    public User(String id, String name, String username, String mobile, String token, String role, String activate, String image, String points, String rate, String ads, int ads_count, String driver, String delivery, int follower, int following) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.token = token;
        this.role = role;
        this.activate = activate;
        this.image = image;
        this.points = points;
        this.rate = rate;
        this.ads = ads;
        this.ads_count = ads_count;
        this.driver = driver;
        this.delivery = delivery;
        this.follower = follower;
        this.following = following;
    }

    public User(String id, String name, String username, String mobile, String token, String role, String activate, String image, String points, String rate, String code, String ads, int ads_count, String driver, String delivery, int follower, int following) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.token = token;
        this.role = role;
        this.activate = activate;
        this.image = image;
        this.points = points;
        this.rate = rate;
        this.code = code;
        this.ads = ads;
        this.ads_count = ads_count;
        this.driver = driver;
        this.delivery = delivery;
        this.follower = follower;
        this.following = following;
    }

    public User(String id, String name, String username, String mobile, String token, String role, String activate, String image, String points, String rate, String code, String ads, int ads_count, String driver, String delivery, int follower, int following, String followed) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.token = token;
        this.role = role;
        this.activate = activate;
        this.image = image;
        this.points = points;
        this.rate = rate;
        this.code = code;
        this.ads = ads;
        this.ads_count = ads_count;
        this.driver = driver;
        this.delivery = delivery;
        this.follower = follower;
        this.following = following;
        this.followed = followed;
    }

    public String  isFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getFollower() {
        return follower;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
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


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public int getAds_count() {
        return ads_count;
    }

    public void setAds_count(int ads_count) {
        this.ads_count = ads_count;
    }
}
