package com.ozoon.ozoon.Model.Responses;

import com.ozoon.ozoon.Model.Objects.User;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public class LoginResponse extends SuccessResponse {
    private User user ;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
