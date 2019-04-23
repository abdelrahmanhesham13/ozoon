package com.ozoon.ozoon.Model.Responses;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public class SuccessResponse {
    private boolean status ;
    private String message ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
