package com.smatech.wlaashal.CallBacks;

import com.smatech.wlaashal.Model.Objects.Product;

/**
 * Created by NaderNabil216@gmail.com on 5/16/2018.
 */
public interface GetProductCallBack extends Failures {
    void OnSuccess(Product product);
}
