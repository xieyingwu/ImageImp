package com.ggtf.xieyingwu.cloudphoto.instagram;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ggtf.xieyingwu.cloudphoto.instagram.model.IgAccessToken;
import com.ggtf.xieyingwu.cloudphoto.instagram.model.IgData;
import com.ggtf.xieyingwu.cloudphoto.utils.FileUtil;
import com.ggtf.xieyingwu.cloudphoto.utils.GsonUtil;
import com.ggtf.xieyingwu.cloudphoto.utils.NetUtil;
import com.ggtf.xieyingwu.cloudphoto.utils.SpUtil;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class IgRequest {
    static final String TAG = IgRequest.class.getName();
    static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    static final String API_URL = "https://api.instagram.com/v1";
    static final String AUTH_URL_SERVICE = AUTH_URL + "?client_id=" + IgConstant.CLIENT_ID +
            "&redirect_uri=" + IgConstant.REDIRECT_URL +
            "&response_type=code";
    static final String AUTH_URL_CLIENT = AUTH_URL + "?client_id=" + IgConstant.CLIENT_ID +
            "&redirect_uri=" + IgConstant.REDIRECT_URL +
            "&response_type=token";
    static final int WHAT_ACCESS_TOKE_SUC = 100;
    static final int WHAT_ACCESS_TOKE_FAIL = 101;
    ExecutorService executorService;
    Context context;
    private IgAccessToken igAccessToken;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            handleByWhat(what);
        }
    };

    public IgRequest(Context context) {
        this.context = context;
        executorService = Executors.newFixedThreadPool(10);
    }

    private void handleByWhat(int what) {
        switch (what) {
            case WHAT_ACCESS_TOKE_SUC:
                toGetUserPhotos();
                toGetUserInfo();
                break;
        }
    }

    private void toGetUserInfo() {
        final String userUrl = API_URL + "/users/self/?access_token=" + igAccessToken.access_token;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String userJsonStr = NetUtil.connectByGet(userUrl);
                Log.w(TAG, "userJsonStr = " + userJsonStr);
                FileUtil.writeContent("userJsonStr", userJsonStr);
            }
        });
    }

    private void toGetUserPhotos() {
        int max_id = 0;
        int min_id = 0;
        int count = Integer.MAX_VALUE >> 2;
        final String photoUrl = API_URL + "/users/self/media/recent/?access_token=" + igAccessToken.access_token
                + "&max_id=" + max_id + "&min_id=" + min_id + "&count=" + count
                + "&scope=" + IgConstant.SCOPE_BASIC;
        Log.w(TAG, "photoUrl = " + photoUrl);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String mediaJsonStr = NetUtil.connectByGet(photoUrl);
                Log.w(TAG, "mediaJsonStr = " + mediaJsonStr);
                FileUtil.writeContent("mediaJsonStr", mediaJsonStr);
                IgData igData = GsonUtil.json2Object(mediaJsonStr, IgData.class);
                if (igData != null) {
                    boolean ok = igData.isOk();
                    Log.w(TAG, "ok = " + ok);
                    boolean hadNextPage = igData.hadNextPage();
                    Log.w(TAG, "hadNextPage = " + hadNextPage);
                }
            }
        });
    }

    public void directToAuthorization() {
        IgDialog igDialog = new IgDialog(context, AUTH_URL_SERVICE, new IgDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                Log.w(TAG, "code = " + code);
                SpUtil.saveStr(IgConstant.IG_SP_KEY_CODE, code);
                Log.w(TAG, "获取到Code后获取AccessToken------");
                accessToken(code);
            }

            @Override
            public void onError(String error) {
                Log.w(TAG, "error = " + error);
            }
        });
        igDialog.show();
    }

    private void accessToken(final String code) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> params = createParams(code);
                String accessTokenJsonStr = NetUtil.connectByPost(TOKEN_URL, params);
                Log.w(TAG, "accessTokenJsonStr = " + accessTokenJsonStr);
                igAccessToken = GsonUtil.json2Object(accessTokenJsonStr, IgAccessToken.class);
                if (igAccessToken != null) {
                    SpUtil.saveStr(IgConstant.IG_SP_KEY_ACCESS_TOKEN, igAccessToken.access_token);
                    myHandler.sendEmptyMessage(WHAT_ACCESS_TOKE_SUC);
                } else {
                    myHandler.sendEmptyMessage(WHAT_ACCESS_TOKE_FAIL);
                }
            }
        });
    }


    private HashMap<String, String> createParams(String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", IgConstant.CLIENT_ID);
        params.put("client_secret", IgConstant.CLIENT_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", IgConstant.REDIRECT_URL);
        params.put("code", code);
        return params;
    }

}
