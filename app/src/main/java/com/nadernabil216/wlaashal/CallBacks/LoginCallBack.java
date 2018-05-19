package com.nadernabil216.wlaashal.CallBacks;

import com.nadernabil216.wlaashal.Model.Objects.User;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public interface LoginCallBack extends Failures {
    void OnSuccess(User user);
}
