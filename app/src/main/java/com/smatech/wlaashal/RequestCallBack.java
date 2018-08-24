package com.smatech.wlaashal;

/**
 * Created by mohanad on 31/07/17.
 */

/**
 * RequestCallBack interface to be notified if some thing happen
 */
public interface RequestCallBack {

    /** success is called when the user successfully login or register .. */
    void success(String response);

    /** error is called when some error happen*/
    void error(Exception exc);
}
