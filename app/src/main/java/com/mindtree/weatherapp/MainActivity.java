package com.mindtree.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mindtree.weatherapp.bean.Datum;
import com.mindtree.weatherapp.bean.TemperatureBean;
import com.thbs.skycons.library.CloudFogView;
import com.thbs.skycons.library.CloudHvRainView;
import com.thbs.skycons.library.CloudMoonView;
import com.thbs.skycons.library.CloudRainView;
import com.thbs.skycons.library.CloudSnowView;
import com.thbs.skycons.library.CloudSunView;
import com.thbs.skycons.library.CloudView;
import com.thbs.skycons.library.MoonView;
import com.thbs.skycons.library.SunView;
import com.thbs.skycons.library.WindView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/*
 * Create a Weather Application, which will show the current weather to a user.
 * The application will display current temperature, humidity and hourly weather updates. We will be
 * using OkHttp client to load the data from the network. The application will use the
 * forecast.io API to fetch current weather updates. The application will leverage Google GSON to
 * map JSON to Java POJO Classes.
 */

public class MainActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private TemperatureBean temperatureBean;

    private TextView currentTemperature, currentTemperatureLine, humidity;

    private Datum datum = new Datum();
    private List<ForecastValues> forecastValuesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HourlyTemperatureAdapter hourlyTemperatureAdapter;

    private RelativeLayout relativeLayout;
    private RelativeLayout.LayoutParams params;

    private String icon = "sunny";
    private final char degree = 0x00B0;
    private String resultFromLocationProvider;
    private static final int ACTIVITY_RESULT_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isNetworkAvailable()){
            Toast.makeText(this, "Please Connect to Internet to use this application", Toast.LENGTH_LONG).show();
            finish();
        }

        currentTemperature = (TextView) findViewById(R.id.currentTemperatureValue);
        currentTemperatureLine = (TextView) findViewById(R.id.currentTemperatureLine);
        humidity = (TextView) findViewById(R.id.humidityValue);

        relativeLayout = (RelativeLayout) findViewById(R.id.currentRelativeLayout);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 300;
        params.height = 300;
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);

        recyclerView = (RecyclerView) findViewById(R.id.hourlyRecyclerView);
        hourlyTemperatureAdapter = new HourlyTemperatureAdapter(forecastValuesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(hourlyTemperatureAdapter);

        Intent intent = new Intent(this, LocationProvider.class);
        startActivityForResult(intent, ACTIVITY_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {

                resultFromLocationProvider = data.getStringExtra("location");
                new NetworkActivity().execute(resultFromLocationProvider);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    class NetworkActivity extends AsyncTask<String, Void, String> {

        private OkHttpClient client = new OkHttpClient();
        private static final String BASE_URL = "https://api.darksky.net/forecast";
        private static final String CLIENT_ID = "3d2c3556754e594035348644daa656f5";
        private static final String QUERY_PARAMETERS = "exclude=minutely, daily, alerts, flags&units=ca";
        private ProgressDialog progDailog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL = BASE_URL + "/" + CLIENT_ID + "/" + params[0] + "?" + QUERY_PARAMETERS;

            Request request = new Request.Builder()
                    .url(URL)
                    .build();
            try {

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        protected void onPostExecute(String results) {
            progDailog.dismiss();
            temperatureBean = gson.fromJson(results, TemperatureBean.class);

            currentTemperature.setText(Math.round(temperatureBean.getCurrently().getTemperature()) + "" + degree);
            currentTemperatureLine.setText("C (Feels Like " +
                    Math.round(temperatureBean.getCurrently().getApparentTemperature()) + degree + "C)");
            humidity.setText(Math.round(temperatureBean.getCurrently().getHumidity() * 100) + "%");
            icon = temperatureBean.getCurrently().getIcon();

            switch (icon) {
                case "clear-day":
                    SunView sunView = new SunView(getApplicationContext());
                    sunView.setBgColor(Color.TRANSPARENT);
                    sunView.setStrokeColor(Color.WHITE);
                    sunView.setLayoutParams(params);
                    relativeLayout.addView(sunView);
                    break;
                case "clear-night":
                    MoonView moonView = new MoonView(getApplicationContext());
                    moonView.setBgColor(Color.TRANSPARENT);
                    moonView.setStrokeColor(Color.WHITE);
                    moonView.setLayoutParams(params);
                    relativeLayout.addView(moonView);
                    break;
                case "rain":
                    CloudRainView cloudRainView = new CloudRainView(getApplicationContext());
                    cloudRainView.setBgColor(Color.TRANSPARENT);
                    cloudRainView.setStrokeColor(Color.WHITE);
                    cloudRainView.setLayoutParams(params);
                    relativeLayout.addView(cloudRainView);
                    break;
                case "snow":
                    CloudSnowView cloudSnowView = new CloudSnowView(getApplicationContext());
                    cloudSnowView.setBgColor(Color.TRANSPARENT);
                    cloudSnowView.setStrokeColor(Color.WHITE);
                    cloudSnowView.setLayoutParams(params);
                    relativeLayout.addView(cloudSnowView);
                    break;
                case "sleet":
                    CloudHvRainView cloudHvRainView = new CloudHvRainView(getApplicationContext());
                    cloudHvRainView.setBgColor(Color.TRANSPARENT);
                    cloudHvRainView.setStrokeColor(Color.WHITE);
                    cloudHvRainView.setLayoutParams(params);
                    relativeLayout.addView(cloudHvRainView);
                    break;
                case "wind":
                    WindView windView = new WindView(getApplicationContext());
                    windView.setBgColor(Color.TRANSPARENT);
                    windView.setStrokeColor(Color.WHITE);
                    windView.setLayoutParams(params);
                    relativeLayout.addView(windView);
                    break;
                case "fog":
                    CloudFogView cloudFogView = new CloudFogView(getApplicationContext());
                    cloudFogView.setBgColor(Color.TRANSPARENT);
                    cloudFogView.setStrokeColor(Color.WHITE);
                    cloudFogView.setLayoutParams(params);
                    relativeLayout.addView(cloudFogView);
                    break;
                case "cloudy":
                    CloudView cloudView = new CloudView(getApplicationContext());
                    cloudView.setBgColor(Color.TRANSPARENT);
                    cloudView.setStrokeColor(Color.WHITE);
                    cloudView.setLayoutParams(params);
                    relativeLayout.addView(cloudView);
                    break;
                case "partly-cloudy-day":
                    CloudSunView cloudSunView = new CloudSunView(getApplicationContext());
                    cloudSunView.setBgColor(Color.TRANSPARENT);
                    cloudSunView.setStrokeColor(Color.WHITE);
                    cloudSunView.setLayoutParams(params);
                    relativeLayout.addView(cloudSunView);
                    break;
                case "partly-cloudy-night":
                    CloudMoonView cloudMoonView = new CloudMoonView(getApplicationContext());
                    cloudMoonView.setBgColor(Color.TRANSPARENT);
                    cloudMoonView.setStrokeColor(Color.WHITE);
                    cloudMoonView.setLayoutParams(params);
                    relativeLayout.addView(cloudMoonView);
                    break;
            }

            for (int i = 0; i < temperatureBean.getHourly().getData().size(); i++) {
                datum = temperatureBean.getHourly().getData().get(i);
                ForecastValues forecastValues = new ForecastValues();
                forecastValues.setTime(datum.getTime());
                forecastValues.setForecastHumidity(datum.getHumidity());
                forecastValues.setForecastSummary(datum.getSummary());
                forecastValues.setForecastTemperature(datum.getTemperature());
                forecastValues.setForecastWindspeed(datum.getWindSpeed());
                forecastValuesList.add(forecastValues);
            }
            hourlyTemperatureAdapter.notifyDataSetChanged();
        }
    }
}

