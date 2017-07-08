package com.kigoss.controller.backend;

import com.kigoss.common.Constant;
import com.kigoss.common.ResponseCode;
import com.kigoss.common.ServerResponse;
import com.kigoss.pojo.User;
import com.kigoss.service.ICategoryService;
import com.kigoss.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by kigoss on 2017/7/5.
 */
@Controller
@RequestMapping("/category/manage/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session,String categoryName,
                                              @RequestParam(value = "parentId",
                                                      defaultValue = "0") Integer parentId){
        User user= (User) session.getAttribute(Constant.CURRENT_USER);
        if(user==null){
            return ServerResponse.creatByErrorMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse<String> response = iUserService.checkRoll(user);
        if(response.isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return response;
        }
    }

    @RequestMapping(value = "update_category_name.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> updateategoryName(HttpSession session,String categoryName,Integer id){
        User user= (User) session.getAttribute(Constant.CURRENT_USER);
        if(user==null){
            return ServerResponse.creatByErrorMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse<String> response = iUserService.checkRoll(user);
        if(response.isSuccess()){
            return iCategoryService.updateategoryName(categoryName,id);
        }else {
            return response;
        }
    }

    @RequestMapping(value = "get_parellel_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getParellelCategory(HttpSession session, Integer categoryId){
        User user= (User) session.getAttribute(Constant.CURRENT_USER);
        if(user==null){
            return ServerResponse.creatByErrorMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        ServerResponse<String> response = iUserService.checkRoll(user);
        if(response.isSuccess()){
            return iCategoryService.getParellelCategory(categoryId);
        }else {
            return response;
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return ServerResponse.creatByErrorMassage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        ServerResponse<String> response = iUserService.checkRoll(user);
        if(response.isSuccess()){
            //查询当前节点的id和递归子节点的id
//            0->10000->100000
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else{
            return response;
        }
    }
}
