package com.kigoss.dao;

import com.kigoss.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectAllProduct();

    List<Product> searchProduct(@Param(value = "productName") String productName, @Param(value = "productId") String productId);

    int updateStatusById(@Param("productId") Integer productId, @Param("status") Integer status);

    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName,@Param("categoryIdList")List<Integer> categoryIdList);
}