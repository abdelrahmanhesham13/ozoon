package com.ozoon.ozoon;

public class UserModelSearch {
    String id;
    String name;
    String rate;
    String image;

    public UserModelSearch(String id, String name, String rate, String image) {
        this.id = id;
        this.name = name;
        this.rate = rate;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
