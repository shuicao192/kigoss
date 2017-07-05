package com.kigoss.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kigoss.common.ServerResponse;
import com.kigoss.dao.CategoryMapper;
import com.kigoss.pojo.Category;
import com.kigoss.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by kigoss on 2017/7/5.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if(parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.creatByErrorMassage("商品参数错误");
        }
        Category category=new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int count = categoryMapper.insert(category);
        if(count>0){
            return ServerResponse.creatBySuccessMessage("添加分类成功");
        }
        return ServerResponse.creatByErrorMassage("添加分类失败");
    }

    @Override
    public ServerResponse<String> updateategoryName(String categoryName, Integer id) {
        if(id==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.creatByErrorMassage("商品参数错误");
        }
        Category category=new Category();
        category.setId(id);
        category.setName(categoryName);
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if(count>0){
            return ServerResponse.creatBySuccessMessage("修改分类成功");
        }
        return ServerResponse.creatByErrorMassage("修改分类失败");
    }

    @Override
    public ServerResponse<List<Category>> getParellelCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.creatByErrorMassage("商品分类错误");
        }
        List<Category> categories = categoryMapper.selectChildrenByPrimaryKey(categoryId);
        if(CollectionUtils.isEmpty(categories)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.creatBySuccessData(categories);
    }

    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> set= Sets.newHashSet();
        Set<Category> categories = findChildId(set, categoryId);
        List<Integer> ids= Lists.newArrayList();
        if(categoryId!=null){
            for (Category category : categories) {
                ids.add(category.getId());
            }
        }
        return ServerResponse.creatBySuccessData(ids);
    }

    private Set<Category> findChildId(Set<Category> set, Integer categoryId) {
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            set.add(category);
        }
        List<Category> categories=categoryMapper.selectChildrenByPrimaryKey(categoryId);
        for (Category categoryItem : categories) {
            findChildId(set,categoryItem.getId());
        }
        return set;
    }
}
