package com.example.razvan.googlemapsdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.ListFragment;
import android.support.v4.app.Fragment;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.List;


public class MainActivity extends FragmentActivity {

    static final LatLng currentLocation = new LatLng(21, 57);
    private GoogleMap googleMap;
    ImageView weatherIcon;
    private TextView temperature, windSpeed, humidity, pressure;
    ImageView img, imgRefresh;
    EditText editText;
    LocationManager mLocationManager;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                addIconToMapCurrentLocation(new LatLng(latitude, longitude));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = mLocationManager.getBestProvider(criteria, true);
//        location = mLocationManager.getLastKnownLocation(provider);
        location=getLastKnownLocation();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,
                0, mLocationListener);
        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            if (location != null) {
                addIconToMapCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();

    }

    private void init() {


        weatherIcon = (ImageView) findViewById(R.id.img_weather_icon);
        temperature = (TextView) findViewById(R.id.txt_temperature);
        windSpeed = (TextView) findViewById(R.id.txt_wind_speed);
        humidity = (TextView) findViewById(R.id.txt_humidity);
        pressure = (TextView) findViewById(R.id.txt_pressure);
        img = (ImageView) findViewById(R.id.img_search);
        editText = (EditText) findViewById(R.id.edt_search);
        imgRefresh = (ImageView) findViewById(R.id.img_refresh);
        linkUI();

    }

    private void linkUI() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, CityList.newInstance(editText.getText().toString())).addToBackStack(null).commit();
            }
        });
        if (location != null) {
            new TemperatureService(weatherIcon, this, temperature, windSpeed, humidity, pressure).execute(location.getLatitude() + "", location.getLongitude() + "");
        }
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.clear();
                if (location != null) {
                    addIconToMapCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    public void addIconToMapOneSelection(CityData cityData) {


        addIconToMap(cityData);
        zoomInCitySelection(cityData);


    }

    public void addIconToMap(CityData cityData) {

        Marker TP = googleMap.addMarker(new MarkerOptions().
                position(cityData.getCoordonates()).title(setSelectedCityData(cityData)));
        if (location != null) {
            Polyline line = googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(location.getLatitude(), location.getLongitude()), cityData.getCoordonates())
                    .width(5)
                    .geodesic(true)
                    .color(Color.RED));
        }
    }

    public void addIconToMapCurrentLocation(LatLng coordonates) {


        Marker TP = googleMap.addMarker(new MarkerOptions().
                position(coordonates).title("Current location"));


    }

    private void zoomInCitySelection(CityData cityData) {
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(cityData.getCoordonates());
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
    }


    private String setSelectedCityData(CityData city) {
        WeatherData weatherData = city.getCityWeatherData();
        String cityName = "City : " + city.getCityName() + "\n";
        String cityHumidity = "Humidity :" + weatherData.getHumidity() + "%\n";
        String cityWindSpeed = "Wind speed : " + weatherData.getWindSpeed() + "m/s\n";
        String pressure = "Pressure : " + weatherData.getPressure() + "hPa\n";
        if (location != null) {
            return cityName + cityHumidity + cityWindSpeed + pressure + "Distance : " + calculationByDistance(new LatLng(location.getLatitude(), location.getLongitude()), city.getCoordonates()) + "Km";
        } else {
            return cityName + cityHumidity + cityWindSpeed + pressure;
        }
    }

    public String calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        String distance=Double.valueOf(km).toString();
        int indexOfDot=distance.indexOf(".");

            return distance.substring(0,indexOfDot+3);



    }


    private void closeKeyboard() {


        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }
    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);


            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }


}
