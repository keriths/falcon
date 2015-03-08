package com.fs.api.demo.impl;

import com.fs.api.demo.product.ProductService;

/**
 * Created by fanshuai on 15-1-14.
 */
public class ProductServiceImpl implements ProductService {
    @Override
    public String produce(String productName) {
        return productName+System.currentTimeMillis();
    }
}
