package com.kigoss.dao;

import com.kigoss.pojo.Category;

import java.util.List;
import java.util.Set;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectChildrenByPrimaryKey(Integer categoryId);
}