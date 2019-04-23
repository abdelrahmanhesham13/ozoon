package com.ozoon.ozoon.Model.Objects;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/16/2018.
 */
public class Product implements Serializable,Comparable<Product> {
    private String name ;
    private String id ;
    private String category_name ;
    private String category_id ;
    private String longtide ;
    private String latitude ;
    private String body ;
    private String created ;
    private String user ;
    private String user_id ;
    private String mobile ;
    private String city ;
    private ArrayList<String> images ;
    private String image ;
    private String rate ;
    String address;
    String subcategory_id;
    String distance;
    String date;
    String count;
    String price;
    boolean favourite;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public Product(String name, String id, String category_name, String category_id, String longtide, String latitude, String body, String created, String user, String user_id, String mobile, String city, ArrayList<String> images, String image, String rate, String address, String subcategory_id) {
        this.name = name;
        this.id = id;
        this.category_name = category_name;
        this.category_id = category_id;
        this.longtide = longtide;
        this.latitude = latitude;
        this.body = body;
        this.created = created;
        this.user = user;
        this.user_id = user_id;
        this.mobile = mobile;
        this.city = city;
        this.images = images;
        this.image = image;
        this.rate = rate;
        this.address = address;
        this.subcategory_id = subcategory_id;
    }


    public Product(String name, String id, String category_name, String category_id, String longtide, String latitude, String body, String created, String user, String user_id, String mobile, String city, ArrayList<String> images, String image, String rate, String address, String subcategory_id, String distance, String date, String count, String price) {
        this.name = name;
        this.id = id;
        this.category_name = category_name;
        this.category_id = category_id;
        this.longtide = longtide;
        this.latitude = latitude;
        this.body = body;
        this.created = created;
        this.user = user;
        this.user_id = user_id;
        this.mobile = mobile;
        this.city = city;
        this.images = images;
        this.image = image;
        this.rate = rate;
        this.address = address;
        this.subcategory_id = subcategory_id;
        this.distance = distance;
        this.date = date;
        this.count = count;
        this.price = price;
    }

    public Product(String name, String id, String category_name, String category_id, String longtide, String latitude, String body, String created, String user, String user_id, String mobile, String city, ArrayList<String> images, String image, String rate, String address, String subcategory_id, String distance, String date, String count, String price, boolean favourite) {
        this.name = name;
        this.id = id;
        this.category_name = category_name;
        this.category_id = category_id;
        this.longtide = longtide;
        this.latitude = latitude;
        this.body = body;
        this.created = created;
        this.user = user;
        this.user_id = user_id;
        this.mobile = mobile;
        this.city = city;
        this.images = images;
        this.image = image;
        this.rate = rate;
        this.address = address;
        this.subcategory_id = subcategory_id;
        this.distance = distance;
        this.date = date;
        this.count = count;
        this.price = price;
        this.favourite = favourite;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLongtide() {
        return longtide;
    }

    public void setLongtide(String longtide) {
        this.longtide = longtide;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getAddress() {
        return address;
    }


    public double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f/Math.PI);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public int compareTo(@NonNull Product o) {
        return rate.compareTo(o.getRate());
    }
}
