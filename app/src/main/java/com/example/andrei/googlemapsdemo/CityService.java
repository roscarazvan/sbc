package com.example.razvan.googlemapsdemo;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
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
public class CityService extends AsyncTask<String, Void, String> {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/find?mode=json&type=like&q=";


    ArrayList<CityData> cities;
    HttpURLConnection con = null;
    InputStream is = null;
    OnAsynckComplete onAsynckComplete;


    public CityService(ArrayList<CityData> cities,OnAsynckComplete onAsynckComplete) {

        this.cities=cities;
        this.onAsynckComplete=onAsynckComplete;
    }

    @Override
    protected void onPostExecute(String s) {

        try {
            JSONArray jsonArray=new JSONObject(s).optJSONArray("list");
            for(int i=0;i<jsonArray.length();i++){
                cities.add(JSONParser.parseCityData(jsonArray.getJSONObject(i)));
            }
            onAsynckComplete.OnTaskComplete();

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception ex) {


        }

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            con = (HttpURLConnection) (new URL(BASE_URL + params[0]+"&cnt=100&&APPID=c61794aecc9f7cfa7a787bb71a03f3ed")).openConnection();
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




}

