package com.ggtf.xieyingwu.weatherservice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.ggtf.xieyingwu.weatherservice.utils.Net;
import com.ggtf.xieyingwu.weatherservice.utils.WeatherUrl;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.DayForecast;
import com.survivingwithandroid.weather.lib.model.WeatherForecast;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public static final String TAG = "weather";
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.image_weather);
        initListView();
        weatherLibTest();
    }

    private void weatherLibTest() {
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.numDays = 6;
        config.ApiKey = Config.OPEN_WEATHER_MAP_API_KEY;
        try {
            WeatherClient client = builder.attach(this)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(WeatherDefaultClient.class)
                    .config(config)
                    .build();
            String cityId = "1790885";
            WeatherRequest request = new WeatherRequest(cityId);
            client.getForecastWeather(request, new WeatherClient.ForecastWeatherEventListener() {
                @Override
                public void onWeatherRetrieved(WeatherForecast forecast) {
                    List<DayForecast> dayForecastList = forecast.getForecast();
                    if (dayForecastList != null && dayForecastList.size() > 0) {
                        for (DayForecast dayForecast : dayForecastList) {
                            float day = dayForecast.forecastTemp.day;
                            Log.e(TAG, "day = " + day);
                        }
                    }
                }

                @Override
                public void onWeatherError(WeatherLibException wle) {

                }

                @Override
                public void onConnectionError(Throwable t) {

                }
            });

        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }
    }

    private void initListView() {
        ListView dayListView = (ListView) findViewById(R.id.weatherListDay);
        ListView nightListView = (ListView) findViewById(R.id.weatherListNight);
        List<String> dayImgIcons = new ArrayList<>();
        List<String> nightImgIcons = new ArrayList<>();
        initIcons(dayImgIcons, nightImgIcons);
        WeatherAdapter dayAdapter = new WeatherAdapter(dayImgIcons, this);
        WeatherAdapter nightAdapter = new WeatherAdapter(nightImgIcons, this);
        dayListView.setAdapter(dayAdapter);
        nightListView.setAdapter(nightAdapter);
    }

    private void initIcons(List<String> dayImgIcons, List<String> nightImgIcons) {
        for (int i = 1; i <= 13; i++) {
            if (i >= 5 && i <= 8) continue;
            if (i == 12) continue;
            if (i / 10 == 0) {
                dayImgIcons.add("0" + i + "d");
                dayImgIcons.add("0" + i + "n");
            } else {
                dayImgIcons.add(i + "d");
                nightImgIcons.add(i + "n");
            }
        }
        dayImgIcons.add("50d");
        nightImgIcons.add("50n");
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.weather:
                weatherInfo();
                break;
        }
    }

    private void weatherInfo() {
        long cityId = 1790885;
        final String urlByOpenWeatherMap = WeatherUrl.urlByOpenWeatherMap(Config.WeatherType.forecast, cityId, true, Config.WeatherExtend.daily);
        Log.e(TAG, "weatherUrl = " + urlByOpenWeatherMap);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Net.linkUrl(urlByOpenWeatherMap, new Net.HttpResult() {
                    @Override
                    public void error(String json) {
                        Log.e(TAG, "error json = " + json);
                    }

                    @Override
                    public void successful(String json) {
                        Log.e(TAG, "successful json = " + json);
//                        parseWeatherJson(json);
                    }
                });
            }
        }).start();

        final String urlWeatherCurrent = WeatherUrl.urlByOpenWeatherMap(Config.WeatherType.weather, cityId, false, null);
        Log.e(TAG, "currentWeatherUrl = " + urlWeatherCurrent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Net.linkUrl(urlWeatherCurrent, new Net.HttpResult() {
                    @Override
                    public void error(String json) {
                        Log.e(TAG, " current error json = " + json);
                    }

                    @Override
                    public void successful(String json) {
                        Log.e(TAG, " current successful json = " + json);

                    }
                });
            }
        }).start();

    }

    private void parseWeatherJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray list = jsonObject.getJSONArray("list");
            if (list != null && list.length() > 0) {
                JSONObject subJsonObject = list.getJSONObject(0);
                JSONArray weathers = subJsonObject.getJSONArray("weather");
                if (weathers != null && weathers.length() > 0) {
                    JSONObject weather = weathers.getJSONObject(0);
                    final String icon = weather.getString("icon");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String imgUrl = Config.OPEN_WEATHER_MAP_BASE_IMAGE_URL + icon + ".png";
                            Log.e(TAG, "imgUrl = " + imgUrl);
                            Glide.with(MainActivity.this).load(imgUrl).into(img);

                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
