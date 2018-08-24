package com.smatech.wlaashal.Model.Objects;

/**
 * Created by NaderNabil216@gmail.com on 5/12/2018.
 */
public class Notification {
    private String id ;
    private String user ;
    private String user_id ;
    private String message ;
    private String status ;
    private String type ;
    private String image ;
    private String date;


    public Notification(String id, String user, String user_id, String message, String status, String type, String image, String date) {
        this.id = id;
        this.user = user;
        this.user_id = user_id;
        this.message = message;
        this.status = status;
        this.type = type;
        this.image = image;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
