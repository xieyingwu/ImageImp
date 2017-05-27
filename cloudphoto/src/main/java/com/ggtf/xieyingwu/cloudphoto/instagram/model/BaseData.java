package com.ggtf.xieyingwu.cloudphoto.instagram.model;

import java.util.List;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class BaseData {
    private static final String TYPE_IMAGE = "image";
    private static final String TYPE_VIDEO = "video";
    private static final String TYPE_CAROUSEL = "carousel";
    public String id;
    public String type;
    public IgDetails images;
    public List<BaseMedia> carousel_media;
    public IgDetails videos;

    public boolean isOnlyImage() {
        return TYPE_IMAGE.equals(type);
    }

    public boolean isOnlyVideo() {
        return TYPE_VIDEO.equals(type);
    }

    public boolean isCarousel() {
        return TYPE_CAROUSEL.equals(type);
    }
}
