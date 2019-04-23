package com.ozoon.ozoon.CallBacks;

import com.ozoon.ozoon.Model.Objects.Product;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/16/2018.
 */
public interface AllProductsCallBack extends Failures {

    void OnSuccess(ArrayList<Product> products);
}
