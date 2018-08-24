package com.smatech.wlaashal.Model.Responses;

import com.smatech.wlaashal.Model.Objects.Notification;

import java.util.ArrayList;

public class NotificationResponse {

    ArrayList<Notification> requests;
    boolean status;
    int count;


    public NotificationResponse(ArrayList<Notification> requests, boolean status, int count) {
        this.requests = requests;
        this.status = status;
        this.count = count;
    }


    public ArrayList<Notification> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Notification> requests) {
        this.requests = requests;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
