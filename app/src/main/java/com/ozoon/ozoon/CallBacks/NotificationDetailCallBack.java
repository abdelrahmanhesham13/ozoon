package com.ozoon.ozoon.CallBacks;

import com.ozoon.ozoon.Model.Responses.NotificationDetailResponse;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public interface NotificationDetailCallBack extends Failures {
    void OnSuccess(NotificationDetailResponse NotificationDetailResponse);
}
