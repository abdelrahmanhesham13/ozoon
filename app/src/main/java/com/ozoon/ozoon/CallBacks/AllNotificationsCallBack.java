package com.ozoon.ozoon.CallBacks;

import com.ozoon.ozoon.Model.Objects.Notification;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public interface AllNotificationsCallBack extends Failures {
    void OnSuccess(ArrayList<Notification> Notification);
}
