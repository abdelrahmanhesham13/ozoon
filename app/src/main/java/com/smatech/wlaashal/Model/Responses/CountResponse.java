package com.smatech.wlaashal.Model.Responses;

public class CountResponse {

    boolean status;
    int count;

    public CountResponse(boolean status, int count) {
        this.status = status;
        this.count = count;
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
