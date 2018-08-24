package com.smatech.wlaashal.Model.Objects;

public class Request {

    String id;
    String user_id;
    String lat_from;
    String long_from;
    String address_from;
    String lat_to;
    String long_to;
    String address_to;
    String price;
    String status;
    String created;
    String city_to;
    String driver_id;
    String city_from;
    String longtiude;
    String lat;
    String address;
    String city;
    String title;
    String delivery_id;


    public Request(String id, String user_id, String price, String status, String created, String longtiude, String lat, String address, String city, String title, String delivery_id) {
        this.id = id;
        this.user_id = user_id;
        this.price = price;
        this.status = status;
        this.created = created;
        this.longtiude = longtiude;
        this.lat = lat;
        this.address = address;
        this.city = city;
        this.title = title;
        this.delivery_id = delivery_id;
    }

    public Request(String id, String user_id, String lat_from, String long_from, String address_from, String lat_to, String long_to, String address_to, String price, String status, String created, String city_to, String driver_id, String city_from) {
        this.id = id;
        this.user_id = user_id;
        this.lat_from = lat_from;
        this.long_from = long_from;
        this.address_from = address_from;
        this.lat_to = lat_to;
        this.long_to = long_to;
        this.address_to = address_to;
        this.price = price;
        this.status = status;
        this.created = created;
        this.city_to = city_to;
        this.driver_id = driver_id;
        this.city_from = city_from;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLat_from() {
        return lat_from;
    }

    public void setLat_from(String lat_from) {
        this.lat_from = lat_from;
    }

    public String getLong_from() {
        return long_from;
    }

    public void setLong_from(String long_from) {
        this.long_from = long_from;
    }

    public String getAddress_from() {
        return address_from;
    }

    public void setAddress_from(String address_from) {
        this.address_from = address_from;
    }

    public String getLat_to() {
        return lat_to;
    }

    public void setLat_to(String lat_to) {
        this.lat_to = lat_to;
    }

    public String getLong_to() {
        return long_to;
    }

    public void setLong_to(String long_to) {
        this.long_to = long_to;
    }

    public String getAddress_to() {
        return address_to;
    }

    public void setAddress_to(String address_to) {
        this.address_to = address_to;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCity_to() {
        return city_to;
    }

    public void setCity_to(String city_to) {
        this.city_to = city_to;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getCity_from() {
        return city_from;
    }

    public void setCity_from(String city_from) {
        this.city_from = city_from;
    }


    public String getLongtiude() {
        return longtiude;
    }

    public void setLongtiude(String longtiude) {
        this.longtiude = longtiude;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }
}
