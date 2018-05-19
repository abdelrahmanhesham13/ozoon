package com.nadernabil216.wlaashal.Model.Responses;

import com.nadernabil216.wlaashal.Model.Objects.Category;

import java.util.ArrayList;

/**
 * Created by NaderNabil216@gmail.com on 5/15/2018.
 */
public class AllCategoriesResponse extends SuccessResponse {
    private ArrayList<Category> categories ;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
