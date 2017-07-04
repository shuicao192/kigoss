package com.kigoss.service.impl;

import com.kigoss.common.Constant;
import com.kigoss.common.ServerResponse;
import com.kigoss.common.TokenCache;
import com.kigoss.dao.UserMapper;
import com.kigoss.pojo.User;
import com.kigoss.service.IUserService;
import com.kigoss.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by kigoss on 2017/7/4.
 */
@Service(value = "iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String userName, String password) {
        int resultCount = userMapper.checkUserName(userName);
        if(resultCount == 0 ){
            return ServerResponse.creatByErrorMassage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(userName,md5Password);
        if(user == null){
            return ServerResponse.creatByErrorMassage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.creatBySuccessData("登录成功",user);
    }

    @Override
    public ServerResponse<String> registor(User user) {
        ServerResponse<String> serverResponse = checkVilid(user.getUsername(), Constant.USER_NAME);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        serverResponse=checkVilid(user.getEmail(),Constant.EMAIL);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        serverResponse=checkVilid(user.getPhone(),Constant.PHONE_NUMBER);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setRole(Constant.Role.ROLE_CUSTOMER);
        int insert = userMapper.insert(user);
        if(insert==0){
            return ServerResponse.creatByErrorMassage("注册失败");
        }
        return ServerResponse.creatBySuccessData("注册成功");
    }

    public ServerResponse<String> checkVilid(String str,String type){
        if(StringUtils.isNoneBlank(str)){
            int count=0;
            switch (type){
                case Constant.EMAIL:
                    count=userMapper.checkEmail(str);
                    break;
                case Constant.USER_NAME:
                    count=userMapper.checkUserName(str);
                    break;
                case Constant.PHONE_NUMBER:
                    count=userMapper.checkPhoneNumber(str);
                    break;
                default:
                    break;
            }
            if(count>0){
                switch (type){
                    case Constant.EMAIL:
                        return ServerResponse.creatByErrorMassage("邮箱已使用");
                    case Constant.USER_NAME:
                        return ServerResponse.creatByErrorMassage("用户名已存在");
                    case Constant.PHONE_NUMBER:
                        return ServerResponse.creatByErrorMassage("手机号码已使用");
                    default:
                        break;
                }
            }else {
                return ServerResponse.creatBySuccess();
            }
            return ServerResponse.creatByErrorMassage("参数错误");
        }else{
            return ServerResponse.creatByErrorMassage("参数错误");
        }
    }

    @Override
    public ServerResponse<String> selectQustion(String usename) {
        ServerResponse<String> serverResponse = checkVilid(usename, Constant.USER_NAME);
        if(serverResponse.isSuccess()){
            return ServerResponse.creatByErrorMassage("该用户不存在");
        }
        return ServerResponse.creatBySuccessMessage(userMapper.selectQustion(usename));
    }

    @Override
    public ServerResponse<String> checkQustionAndAnswer(String userName, String question, String answer) {
        ServerResponse<String> serverResponse = selectQustion(userName);
        if(serverResponse.isSuccess()){
            if(serverResponse.getMsg().equals(question)){
                int count = userMapper.checkAnswer(userName,answer);
                if(count==0){
                    return ServerResponse.creatByErrorMassage("答案错误");
                }else{
                    String forgetToken= UUID.randomUUID().toString();
                    TokenCache.setKey("token_"+userName,forgetToken);
                    return ServerResponse.creatBySuccessMessage(forgetToken);
                }
            }else{
                return ServerResponse.creatByErrorMassage("提示问题错误");
            }
        }
        return serverResponse;
    }

    @Override
    public ServerResponse<String> updatePassword(String userName, String passwordNew, String forgetToken) {
        if(StringUtils.isNoneBlank(forgetToken)){
            ServerResponse<String> response = checkVilid(userName, Constant.USER_NAME);
            if(response.isSuccess()){
                return ServerResponse.creatByErrorMassage("用戶不存在");
            }
            if(StringUtils.equals(forgetToken,TokenCache.getKey("token_"+userName))){
                String passwordCode=MD5Util.MD5EncodeUtf8(passwordNew);
                int count = userMapper.updatePassword(userName, passwordCode);
                if(count==0){
                    return ServerResponse.creatByErrorMassage("修改密码失败");
                }
                return ServerResponse.creatBySuccessMessage("修改密码成功");
            }else{
                return ServerResponse.creatByErrorMassage("参数错误");
            }
        }
        return ServerResponse.creatByErrorMassage("参数错误");
    }

    @Override
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {
        int count =userMapper.checkUserPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if(count==0){
            return ServerResponse.creatByErrorMassage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        count = userMapper.updateByPrimaryKey(user);
        if(count==0){
            return ServerResponse.creatByErrorMassage("密码重设失败");
        }
        return ServerResponse.creatBySuccessMessage("密码重设成功");
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        int count = userMapper.checkEmailById(user.getId(),user.getEmail());
        if(count>0){
            return ServerResponse.creatByErrorMassage("邮箱已被使用");
        }
        User newUser=new User();
        newUser.setId(user.getId());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setQuestion(user.getQuestion());
        newUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(newUser);
        if(updateCount==0){
            return ServerResponse.creatByErrorMassage("更新用户信息失败");
        }
        return ServerResponse.creatBySuccessMessage("更新用户信息成功");
    }

    @Override
    public ServerResponse<User> getUserInfo(Integer id) {
        User user=userMapper.selectByPrimaryKey(id);
        if(user==null){
            return ServerResponse.creatByErrorMassage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.creatBySuccessData(user);
    }
}
