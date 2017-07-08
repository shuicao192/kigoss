package com.kigoss.service;

import com.kigoss.common.ServerResponse;
import com.kigoss.pojo.Product;

/**
 * Created by kigoss on 2017/7/6.
 */
public interface IProductService {
    ServerResponse selectAllProduct(Integer pageNum, Integer pageSize);

    ServerResponse searchProduct(String productName, String productId, Integer pageNum, Integer pageSize);

    ServerResponse productDetail(Integer productId);

    ServerResponse updataOrAddProduct(Product product);

    ServerResponse setSaleStatus(Integer productId, Integer status);

    ServerResponse listProductsByOrder(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
