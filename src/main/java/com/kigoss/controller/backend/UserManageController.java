package com.kigoss.controller.backend;

import com.kigoss.common.Constant;
import com.kigoss.common.ServerResponse;
import com.kigoss.pojo.User;
import com.kigoss.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by kigoss on 2017/7/5.
 */
@Controller
@RequestMapping(value = "/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName,String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(userName, password);
        if(response.isSuccess()){
            User user=response.getData();
            if(user.getRole()==Constant.Role.ROLE_ADMIN){
                session.setAttribute(Constant.CURRENT_USER,user);
                return response;
            }else{
                return ServerResponse.creatByErrorMassage("不是管理员，无法登陆");
            }
        }
        return response;
    }

}
