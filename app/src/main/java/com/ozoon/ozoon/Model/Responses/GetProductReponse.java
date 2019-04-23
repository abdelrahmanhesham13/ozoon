package com.ozoon.ozoon.Model.Responses;

import com.ozoon.ozoon.Model.Objects.Product;

/**
 * Created by NaderNabil216@gmail.com on 5/16/2018.
 */
public class GetProductReponse extends SuccessResponse {
    private String base_url ;
    private Product product ;

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
