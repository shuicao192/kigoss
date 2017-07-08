package com.kigoss.controller.backend;

import com.kigoss.common.Constant;
import com.kigoss.common.ResponseCode;
import com.kigoss.common.ServerResponse;
import com.kigoss.pojo.User;
import com.kigoss.service.IProductService;
import com.kigoss.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by kigoss on 2017/7/6.
 */
@Controller
@RequestMapping(value = "/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "get_all_products.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse selectAllProduct(HttpSession session,
                                           @RequestParam(value = "pageNum",defaultValue ="1") Integer pageNum,
                                           @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user= (User) session.getAttribute(Constant.CURRENT_USER);
        if(user==null){
            return ServerResponse.creatByErrorMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse<String> response = iUserService.checkRoll(user);
        if(response.isSuccess()){
            return iProductService.selectAllProduct(pageNum,pageSize);
        }else {
            return response;
        }
    }

//    http://localhost:8080/manage/product/search.do?productName=p
//    http://localhost:8080/manage/product/search.do?productId=1
    @RequestMapping(value = "search_product.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchProduct(HttpSession session,String productName,String productId,
                                        @RequestParam(value = "pageNum",defaultValue ="1") Integer pageNum,
                                        @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user= (User) session.getAttribute(Constant.CURRENT_USER);
        if(user==null){
            return ServerResponse.creatByErrorMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse<String> response = iUserService.checkRoll(user);
        if(response.isSuccess()){
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return response;
        }
    }
}
