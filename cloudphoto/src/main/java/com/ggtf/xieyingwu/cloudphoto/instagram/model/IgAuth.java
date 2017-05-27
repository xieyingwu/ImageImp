package com.ggtf.xieyingwu.cloudphoto.instagram.model;

/**
 * Created by xieyingwu on 2017/5/18.
 */

public class IgAuth {
    public String code;
    public String error;
    public String error_reason;
    public String error_description;
    public boolean isAuthSuc;

    private IgAuth() {
    }

    public static IgAuth initIgAuth(String redirectUri) {
        IgAuth igAuth = new IgAuth();
        int beginIndex = redirectUri.indexOf("?");
        String value = redirectUri.substring(beginIndex + 1);
        if (value.contains("&")) {
            igAuth.isAuthSuc = false;
            String[] split = value.split("&");
            for (String str : split) {
                if (str.startsWith("error=")) {
                    igAuth.error = str.replace("error=", "").trim();
                    continue;
                }
                if (str.startsWith("error_reason=")) {
                    igAuth.error_reason = str.replace("error_reason=", "").trim();
                    continue;
                }
                if (str.startsWith("error_description=")) {
                    igAuth.error_description = str.replace("error_description=", "").trim();
                }
            }
        } else {
            igAuth.isAuthSuc = true;
            igAuth.code = value.replace("code=", "").trim();
        }
        return igAuth;
    }
}
