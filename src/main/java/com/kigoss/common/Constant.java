package com.kigoss.common;

/**
 * Created by kigoss on 2017/7/4.
 */
public class Constant {
    public static final String CURRENT_USER ="current_user";
    public static final String EMAIL="email";
    public static final String USER_NAME="user_name";
    public static final String PHONE_NUMBER="phone_number";

    public interface Role{
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;//管理员
    }
}
