package com.example.razvan.googlemapsdemo;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by razvan on 07.04.2015.
 */
public class TemperatureService extends AsyncTask<String, Void, String> {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat=";


    TextView txtTemperature,txtWindSpeed,txtHumidity,txtPressure;
    ImageView imgWeather;
    Context context;
    HttpURLConnection con = null;
    InputStream is = null;
    WeatherData weatherData;

    public TemperatureService(ImageView imgWeather, Context context,TextView ...texts) {
        this.txtTemperature = texts[0];
        this.txtWindSpeed=texts[1];
        this.txtHumidity=texts[2];
        this.txtPressure=texts[3];
        this.imgWeather = imgWeather;
        this.context = context;
        weatherData=new WeatherData();
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            weatherData = JSONParser.parseWeather(new JSONObject(s));
            txtTemperature.setText(Math.round(Double.valueOf(weatherData.getTemp()) - 273.15) + "");
            setImageWeather(weatherData.getIcon(), context);
            txtWindSpeed.setText("Wind speed : "+weatherData.getWindSpeed() +" m/s");
            txtHumidity.setText("Humidity : "+weatherData.getHumidity() +"%");
            txtPressure.setText("Pressure : "+weatherData.getPressure()+" hPa");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception ex) {


        }

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            con = (HttpURLConnection) (new URL(BASE_URL + params[0]+"&lon="+params[1]+"&APPID=c61794aecc9f7cfa7a787bb71a03f3ed")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }


        return null;
    }

    private void setImageWeather(String code, Context context) {


        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("ic_"+code, "drawable",
                context.getPackageName());
        imgWeather.setImageDrawable(resources.getDrawable(resourceId));

    }


}

