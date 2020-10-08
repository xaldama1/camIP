package org.xas.uned.camip.utils;

public class Constants {

    private static String userToken = null;

    public static void setUserToken(String token){
        Constants.userToken = userToken;
    }

    public static String getUserToken(){
        return Constants.userToken;
    }

}
