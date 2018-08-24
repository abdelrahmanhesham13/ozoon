package com.smatech.wlaashal.CallBacks;

import com.smatech.wlaashal.Model.Objects.Notification;
import com.smatech.wlaashal.Model.Responses.NotificationDetailResponse;
import com.smatech.wlaashal.Model.Responses.NotificationResponse;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public interface NotificationDetailCallBack extends Failures {
    void OnSuccess(NotificationDetailResponse NotificationDetailResponse);
}
