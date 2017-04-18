package com.ggtf.xieyingwu.demotest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by xieyingwu on 2017/3/20.
 */

public class AdapterGrid extends BaseAdapter {
    public List<Integer> resIds;
    private Context context;
    private View.OnClickListener listener;

    public AdapterGrid(List<Integer> resIds, Context context, View.OnClickListener listener) {
        this.resIds = resIds;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return resIds.size();
    }

    @Override
    public Object getItem(int position) {
        return resIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.CENTER);
        image.setImageResource(resIds.get(position));
        image.setOnClickListener(listener);
        return image;
    }
}
