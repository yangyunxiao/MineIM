package com.xiao.common;


public class Common {

    public interface Constance {

        //手机号码的正则
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        String HOST = "http://localhost:8080/";

        String API_URL = HOST + "api/";
    }
}
