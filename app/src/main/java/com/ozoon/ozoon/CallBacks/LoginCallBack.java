package com.ozoon.ozoon.CallBacks;

import com.ozoon.ozoon.Model.Objects.User;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public interface LoginCallBack extends Failures {
    void OnSuccess(User user);
}
