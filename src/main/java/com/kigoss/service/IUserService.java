package com.kigoss.service;

import com.kigoss.common.ServerResponse;
import com.kigoss.pojo.User;

/**
 * Created by kigoss on 2017/7/4.
 */
public interface IUserService {

    ServerResponse<User> login(String userName, String password);

    ServerResponse<String> registor(User user);

    ServerResponse<String> checkVilid(String str,String type);

    ServerResponse<String> selectQustion(String username);

    ServerResponse<String> checkQustionAndAnswer(String userName, String question, String answer);

    ServerResponse<String> updatePassword(String userName, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);

    ServerResponse<User> updateUserInfo(User user);

    ServerResponse<User> getUserInfo(Integer id);
}
