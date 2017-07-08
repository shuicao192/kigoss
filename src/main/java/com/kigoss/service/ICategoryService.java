package com.kigoss.service;

import com.kigoss.common.ServerResponse;
import com.kigoss.pojo.Category;

import java.util.List;

/**
 * Created by kigoss on 2017/7/5.
 */
public interface ICategoryService {
    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> updateategoryName(String categoryName, Integer id);

    ServerResponse<List<Category>> getParellelCategory(Integer categoryId);

    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
