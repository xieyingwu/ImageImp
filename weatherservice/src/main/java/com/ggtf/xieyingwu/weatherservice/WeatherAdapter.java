package com.ggtf.xieyingwu.weatherservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by xieyingwu on 2017/3/20.
 */

public class WeatherAdapter extends BaseAdapter {
    private List<String> imgIcons;
    private Context context;

    public WeatherAdapter(List<String> imgIcons, Context context) {
        this.imgIcons = imgIcons;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgIcons.size();
    }

    @Override
    public Object getItem(int position) {
        return imgIcons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.weather_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.weather_img_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String imgUrl = Config.OPEN_WEATHER_MAP_BASE_IMAGE_URL + imgIcons.get(position) + ".png";
        Glide.with(context).load(imgUrl).dontAnimate().into(holder.img);
        return convertView;
    }

    private class ViewHolder {
        ImageView img;
    }
}
