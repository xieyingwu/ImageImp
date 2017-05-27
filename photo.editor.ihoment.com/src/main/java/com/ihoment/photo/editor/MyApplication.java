package com.ihoment.photo.editor;

import android.app.Application;

import com.adobe.creativesdk.aviary.IGoogleClientBilling;
import com.adobe.creativesdk.foundation.AdobeCSDKFoundation;
import com.adobe.creativesdk.foundation.auth.IAdobeAuthClientCredentials;

/**
 * Created by xieyingwu on 2017/5/15.
 */

public class MyApplication extends Application implements IAdobeAuthClientCredentials, IGoogleClientBilling {
    /* Be sure to fill in the two strings below. */
    private static final String CREATIVE_SDK_CLIENT_ID = "014f829bb8cf488591cb71c3b327b858";
    private static final String CREATIVE_SDK_CLIENT_SECRET = "4bc0c10a-2629-41d3-88bc-4e1be3681067";
    private static final String CREATIVE_SDK_REDIRECT_URI = "ams+3f94a8fb5ba9269f5b025e7eedc378ba8220e149://adobeid/014f829bb8cf488591cb71c3b327b858";
    private static final String[] CREATIVE_SDK_SCOPES = {"email", "profile", "address"};

    @Override
    public void onCreate() {
        super.onCreate();
        AdobeCSDKFoundation.initializeCSDKFoundation(getApplicationContext());
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_CLIENT_SECRET;
    }

    @Override
    public String[] getAdditionalScopesList() {
        return CREATIVE_SDK_SCOPES;
    }

    @Override
    public String getRedirectURI() {
        return CREATIVE_SDK_REDIRECT_URI;
    }

    @Override
    public String getBillingKey() {
        return MyApplication.class.getName();
    }
}
