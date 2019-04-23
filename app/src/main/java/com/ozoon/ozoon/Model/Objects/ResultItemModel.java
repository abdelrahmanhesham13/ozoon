package com.ozoon.ozoon.Model.Objects;

public class ResultItemModel {
    String fromCity;
    String toCity;
    String price;
    String time;
    String id;
    User user;

    public ResultItemModel(String fromCity, String toCity, String price, String time, String id, User user) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.price = price;
        this.time = time;
        this.id = id;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
