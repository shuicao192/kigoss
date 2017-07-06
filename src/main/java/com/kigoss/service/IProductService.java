package com.kigoss.service;

import com.kigoss.common.ServerResponse;

/**
 * Created by kigoss on 2017/7/6.
 */
public interface IProductService {
    ServerResponse selectAllProduct(Integer pageNum, Integer pageSize);

    ServerResponse searchProduct(String productName, String productId, Integer pageNum, Integer pageSize);
}
