package com.example.razvan.googlemapsdemo;

import com.google.android.gms.maps.model.LatLng;
import com.example.razvan.googlemapsdemo.WeatherData;

/**
 * Created by razvan on 23.05.2015.
 */
public class CityData {

    LatLng coordonates;
    String cityName;
    String countryCode;
    WeatherData cityWeatherData;


    public CityData(){



    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public WeatherData getCityWeatherData() {
        return cityWeatherData;
    }

    public void setCityWeatherData(WeatherData cityWeatherData) {
        this.cityWeatherData = cityWeatherData;
    }

    public LatLng getCoordonates() {
        return coordonates;
    }

    public void setCoordonates(LatLng coordonates) {
        this.coordonates = coordonates;
    }

    public CityData(String cityName,String countryCode,WeatherData cityWeatherData,LatLng coordonates){

        this.coordonates=coordonates;
        this.cityName=cityName;
        this.countryCode=countryCode;
        this.cityWeatherData=cityWeatherData;


    }
}
