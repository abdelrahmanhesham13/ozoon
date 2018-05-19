package com.nadernabil216.wlaashal.Model.Responses;

import com.nadernabil216.wlaashal.Model.Objects.SubCategory;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/16/2018.
 */
public class SubCategoriesResponse extends SuccessResponse {
    
    ArrayList<SubCategory> subcategories;

    public ArrayList<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }
}
