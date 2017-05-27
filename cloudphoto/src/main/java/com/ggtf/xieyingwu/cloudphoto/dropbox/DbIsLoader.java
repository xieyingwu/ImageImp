package com.ggtf.xieyingwu.cloudphoto.dropbox;

import android.content.Context;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

/**
 * Created by xieyingwu on 2017/5/19.
 */

public class DbIsLoader extends BaseGlideUrlLoader {

    public DbIsLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(Object model, int width, int height) {
        return null;
    }
}
