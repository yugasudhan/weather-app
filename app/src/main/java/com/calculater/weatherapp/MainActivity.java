package com.calculater.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText etCity;

    TextView tempV, feelLikeV, humidityV, windV, cloudV, pressureV;

    TextView tempT,feellikeT,humidityT,windT,cloudT,pressureT;

    TextView cityname,weather;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "9b0f843298c5c1939885681130aca616";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        tempV = findViewById(R.id.tempV);
        feelLikeV = findViewById(R.id.feelLikeV);
        humidityV = findViewById(R.id.humidityV);
        windV = findViewById(R.id.windV);
        cloudV = findViewById(R.id.cloudV);
        pressureV = findViewById(R.id.pressureV);



        tempT = findViewById(R.id.tempT);
        feellikeT = findViewById(R.id.feelLikeT);
        humidityT = findViewById(R.id.humidityT);
        windT = findViewById(R.id.windT);
        cloudT = findViewById(R.id.cloudT);
        pressureT = findViewById(R.id.pressureT);

        cityname = findViewById(R.id.cityName);
        weather = findViewById(R.id.desc);




    }

    public void getWeatherDetails(View view) {

        String tempUrl = "";
        String city = etCity.getText().toString().trim();


        if(city.equals("")){
            weather.setText("City field can not be empty!");

            tempV.setText("");
            feelLikeV.setText("");
            humidityV.setText("");
            windV.setText("");
            cloudV.setText("");
            pressureV.setText("");

            tempT.setVisibility(View.INVISIBLE);
            feellikeT.setVisibility(View.INVISIBLE);
            humidityT.setVisibility(View.INVISIBLE);
            windT.setVisibility(View.INVISIBLE);
            cloudT.setVisibility(View.INVISIBLE);
            pressureT.setVisibility(View.INVISIBLE);

            cityname.setText("");


        }else {

                tempUrl = url + "?q=" + city + "&appid=" + appid;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");



                        tempV.setText(df.format(temp) + " °C");
                        feelLikeV.setText(df.format(feelsLike) + " °C");
                        humidityV.setText(humidity + "%");
                        windV.setText(wind + "m/s (meters per second)");
                        cloudV.setText(clouds + "%");
                        pressureV.setText(pressure + " hPa");

                        tempT.setVisibility(View.VISIBLE);
                        feellikeT.setVisibility(View.VISIBLE);
                        humidityT.setVisibility(View.VISIBLE);
                        windT.setVisibility(View.VISIBLE);
                        cloudT.setVisibility(View.VISIBLE);
                        pressureT.setVisibility(View.VISIBLE);

                        cityname.setText(cityName);
                        weather.setText(description);

                        etCity.setText("");


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
