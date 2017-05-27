package com.ggtf.xieyingwu.cloudphoto.instagram.model;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by xieyingwu on 2017/5/17.
 */

public class IgData {
    public Pagination pagination;
    public Meta meta;
    public List<BaseData> data;

    public boolean isOk() {
        return meta != null && meta.isOk();
    }

    public boolean hadNextPage() {
        return pagination != null && !TextUtils.isEmpty(pagination.next_max_id) && !TextUtils.isEmpty(pagination.next_url);
    }
}
