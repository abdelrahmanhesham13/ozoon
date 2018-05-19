package com.nadernabil216.wlaashal.Model.Objects;

/**
 * Created by NaderNabil216@gmail.com on 5/12/2018.
 */
public class Notification {
    private String id ;
    private String body ;
    private String date ;
    private String user_name ;
    private String user_image ;

    public Notification() {
    }

    public Notification(String id, String body, String date, String user_name, String user_image) {
        this.id = id;
        this.body = body;
        this.date = date;
        this.user_name = user_name;
        this.user_image = user_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
