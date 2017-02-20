package com.example.razvan.googlemapsdemo;

/**
 * Created by razvan on 14.05.2015.
 */
public class WeatherData {

    String windSpeed,temp,humidity,icon,pressure;

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public WeatherData(String ...data){

        windSpeed=data[0];
        temp=data[1];
        humidity=data[2];
        icon=data[3];
        pressure=data[4];

    }

    public WeatherData(){};

}
