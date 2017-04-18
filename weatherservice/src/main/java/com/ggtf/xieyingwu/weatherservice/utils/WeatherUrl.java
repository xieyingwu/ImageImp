package com.ggtf.xieyingwu.weatherservice.utils;

import com.ggtf.xieyingwu.weatherservice.Config;

/**
 * Created by xieyingwu on 2017/3/20.
 */

public class WeatherUrl {
    private WeatherUrl() {
    }

    public static String urlByOpenWeatherMap(Config.WeatherType type, long cityId, boolean isExtend, Config.WeatherExtend extend) {
        StringBuilder sb = new StringBuilder(Config.OPEN_WEATHER_MAP_BASE_URL);
        sb.append(type.name());
        if (isExtend) {
            sb.append("/").append(extend.name());
        }
        sb.append("?");
        sb.append("id=").append(cityId).append("&");
        sb.append("cnt=").append(7).append("&");
        sb.append("lang=").append("zh_cn").append("&");
        sb.append("units=").append("metric").append("&"); /*摄氏度*/
//        sb.append("units=").append("imperial").append("&");/*华氏度*/
        sb.append("APPID=").append(Config.OPEN_WEATHER_MAP_API_KEY);
        return sb.toString();
    }

}
