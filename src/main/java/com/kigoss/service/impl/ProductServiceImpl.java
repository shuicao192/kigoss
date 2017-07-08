package com.kigoss.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.kigoss.common.Constant;
import com.kigoss.common.ResponseCode;
import com.kigoss.common.ServerResponse;
import com.kigoss.dao.CategoryMapper;
import com.kigoss.dao.ProductMapper;
import com.kigoss.pojo.Category;
import com.kigoss.pojo.Product;
import com.kigoss.service.ICategoryService;
import com.kigoss.service.IProductService;
import com.kigoss.util.DataTimeUtil;
import com.kigoss.util.PropertiesUtil;
import com.kigoss.vo.ProductDetailVo;
import com.kigoss.vo.ProductListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kigoss on 2017/7/6.
 */
@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse selectAllProduct(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectAllProduct();
        List<ProductListVo> productListVos= Lists.newArrayList();
        for (Product product : products) {
            ProductListVo productListVo=assembleProductListVo(product);
            productListVos.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVos);
        return ServerResponse.creatBySuccessData(pageResult);
    }

    @Override
    public ServerResponse searchProduct(String productName, String productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.searchProduct(productName,productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.creatBySuccessData(pageResult);
    }

    @Override
    public ServerResponse productDetail(Integer productId) {
        if(productId==null){
            return ServerResponse.creatByErrorMassage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.creatByErrorMassage("查询结果错误");
        }
        return ServerResponse.creatBySuccessData(assembleProductDetailVo(product));
    }

    @Override
    public ServerResponse updataOrAddProduct(Product product) {
        if(StringUtils.isBlank(product.getName())){
            return ServerResponse.creatByErrorMassage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Integer id = product.getId();
        int count=0;
        if(id==null){
            count = productMapper.updateByPrimaryKeySelective(product);
            if(count==0){
                return ServerResponse.creatByErrorMassage("更新商品失败");
            }
            return ServerResponse.creatBySuccessMessage("更新商品成功");
        }else{
            count=productMapper.insert(product);
            if(count==0){
                return ServerResponse.creatByErrorMassage("添加商品失败");
            }
            return ServerResponse.creatBySuccessMessage("添加商品成功");
        }
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if(productId==null){
            return ServerResponse.creatByErrorMassage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        productMapper.updateStatusById(productId,status);
        return null;
    }

    @Override
    public ServerResponse listProductsByOrder(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        if(categoryId==null&&StringUtils.isBlank(keyword)){
            return ServerResponse.creatByErrorMassage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Integer> ids= Lists.newArrayList();
        if(categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&StringUtils.isBlank(keyword)){
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.creatBySuccessData(pageInfo);
            }
             ids = (List<Integer>) iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,ids.size()==0?null:ids);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.creatBySuccessData(pageInfo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo=new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setCreateTime(DataTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DataTimeUtil.dateToStr(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setStock(product.getStock());
        return productDetailVo;
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo=new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        return productListVo;
    }
}
