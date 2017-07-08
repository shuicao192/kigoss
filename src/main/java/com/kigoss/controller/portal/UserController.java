package com.kigoss.controller.portal;

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
 * Created by kigoss on 2017/7/4.
 */
@Controller
@RequestMapping(value = "/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName,String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(userName, password);
        if(response.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Constant.CURRENT_USER);
        return ServerResponse.creatBySuccessMessage("退出登录成功");
    }

    @RequestMapping(value = "registor.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> registor(User user){
        return iUserService.registor(user);
    }

    @RequestMapping(value = "check_vilid.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkVilid(String str,String type){
        return iUserService.checkVilid(str,type);
    }

    @RequestMapping(value = "select_qustion.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> selectQustion(String usename){
        return iUserService.selectQustion(usename);
    }

    @RequestMapping(value = "check_question_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkQustionAndAnswer(String userName, String question, String answer){
        return iUserService.checkQustionAndAnswer(userName,question,answer);
    }

    @RequestMapping(value = "update_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updatePassword(String userName,String passwordNew,String forgetToken){
        return iUserService.updatePassword(userName,passwordNew,forgetToken);
    }

    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String oldPassword,String newPassword){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if(user==null){
            return ServerResponse.creatByErrorMassage("用户未登录");
        }
        return iUserService.resetPassword(oldPassword,newPassword,user);
    }

    @RequestMapping(value = "update_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Constant.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.creatByErrorMassage("用户未登录");
        }
        user.setId(currentUser.getId());
        ServerResponse<User> response = iUserService.updateUserInfo(user);
        if(response.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get_user_info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User currentUser= (User) session.getAttribute(Constant.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.creatByErrorMassage("用户未登录");
        }
        return iUserService.getUserInfo(currentUser.getId());
    }
}
