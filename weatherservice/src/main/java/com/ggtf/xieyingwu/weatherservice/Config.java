package com.ggtf.xieyingwu.weatherservice;

/**
 * Created by xieyingwu on 2017/3/20.
 */

public final class Config {
    /*OpenWeatherMap天气服务对接*/
    public final static String OPEN_WEATHER_MAP_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public final static String OPEN_WEATHER_MAP_BASE_IMAGE_URL = "http://openweathermap.org/img/w/";
    public final static String OPEN_WEATHER_MAP_API_KEY = "801a880dbb25e583f39ed12be4b896aa";

    public enum WeatherType {
        forecast,
        weather
    }

    public enum WeatherExtend {
        daily,
        find
    }


}
