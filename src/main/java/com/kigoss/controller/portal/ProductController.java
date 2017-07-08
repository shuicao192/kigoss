package com.kigoss.controller.portal;

import com.kigoss.common.ServerResponse;
import com.kigoss.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by kigoss on 2017/7/8.
 */
@Controller
@RequestMapping(value = "/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listProducts(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                                       @RequestParam(value = "keyword",required = false) String keyword,
                                       @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                       @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "orderBy",defaultValue = "") String orderBy){


        return iProductService.listProductsByOrder(categoryId,keyword,pageNum,pageSize,orderBy);
    }

    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse productDetail(Integer productId){
        return iProductService.productDetail(productId);
    }
}
