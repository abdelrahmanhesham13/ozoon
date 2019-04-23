package com.ozoon.ozoon.Model.Responses;

import com.ozoon.ozoon.Model.Objects.Request;
import com.ozoon.ozoon.Model.Objects.User;

public class NotificationDetailResponse {

    Request request;
    User user;
    User taxi;
    User delivery;
    boolean status;

    public NotificationDetailResponse(Request request, User user, boolean status) {
        this.request = request;
        this.user = user;
        this.status = status;
    }

    public NotificationDetailResponse(Request request, User user, User taxi, boolean status) {
        this.request = request;
        this.user = user;
        this.taxi = taxi;
        this.status = status;
    }


    public NotificationDetailResponse(Request request, User user, User taxi, User delivery, boolean status) {
        this.request = request;
        this.user = user;
        this.taxi = taxi;
        this.delivery = delivery;
        this.status = status;
    }


    public User getDelivery() {
        return delivery;
    }

    public User getTaxi() {
        return taxi;
    }

    public Request getRequest() {
        return request;
    }

    public User getUser() {
        return user;
    }

    public boolean isStatus() {
        return status;
    }
}
