package com.ggtf.xieyingwu.cloudphoto.instagram.model;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class Meta {
    private static final int CODE_SUC = 200;
    private static final String ERROR_TYPE_PERIOD = "OAuthAccessTokenException";
    public int code;
    public String error_type;
    public String error_message;

    public boolean isOk() {
        return code == CODE_SUC;
    }

    public boolean isAccessTokenPeriod() {
        return ERROR_TYPE_PERIOD.equals(error_type);
    }
}
