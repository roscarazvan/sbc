package com.example.razvan.googlemapsdemo;



import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razvan on 13.03.2015.
 */
public class JSONParser {





    public static WeatherData parseWeather(JSONObject object) {

        JSONObject temp = object.optJSONObject("main");
        String tempValue = temp.optString("temp");
        String humidity=temp.optString("humidity");
        JSONObject wind=object.optJSONObject("wind");
        String speed=wind.optString("speed");
        String description="";
        String pressure=temp.optString("pressure");
        JSONArray jsonArray = object.optJSONArray("weather");

        for (int i = 0; i < jsonArray.length(); i++) {
            description = jsonArray.optJSONObject(i).optString("icon");
        }
        return new WeatherData(speed,tempValue,humidity,description,pressure);

    }

    public static CityData parseCityData(JSONObject jsonObject){

        String cityName=jsonObject.optString("name");
        String countryCode=jsonObject.optJSONObject("sys").optString("country");
        LatLng coordonates;
        JSONObject jsonObject1=jsonObject.optJSONObject("coord");
        double lat=jsonObject1.optDouble("lat");
        double lon=jsonObject1.optDouble("lon");
        coordonates=new LatLng(lat,lon);
        WeatherData weatherData=parseWeather(jsonObject);
        return new CityData(cityName,countryCode,weatherData,coordonates);

    }




}
