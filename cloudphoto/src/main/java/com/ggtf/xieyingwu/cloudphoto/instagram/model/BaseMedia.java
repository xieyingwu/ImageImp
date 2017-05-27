package com.ggtf.xieyingwu.cloudphoto.instagram.model;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class BaseMedia {
    private static final String TYPE_IMAGE = "image";
    private static final String TYPE_VIDEO = "video";
    public String type;
    public IgDetails images;
    public IgDetails videos;

    public boolean isImage() {
        return TYPE_IMAGE.equals(type);
    }

    public boolean isVideo() {
        return TYPE_VIDEO.equals(type);
    }
}
