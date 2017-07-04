package com.kigoss.dao;

import com.kigoss.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String userName);

    User selectLogin(@Param("userName") String userName, @Param("password") String password);

    int checkEmail(String email);

    int checkPhoneNumber(String phone);

    String selectQustion(String userName);

    int checkAnswer(@Param("userName") String userName, @Param("answer") String answer);

    int updatePassword(@Param("userName") String userName,@Param("passwordCode") String passwordCode);

    int checkUserPassword(@Param("password") String password,@Param("userId") Integer userId);

    int checkEmailById(@Param("id") Integer id, @Param("email") String email);
}