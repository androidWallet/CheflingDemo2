package com.mguru2.cheflingtest;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mguru2.adapter.ForecastAdapter;
import com.mguru2.utils.MethodUtils;
import com.mguru2.utils.NetworkHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Sk Maniruddin on 23-10-2016.
 */

public class MainActivity extends AppCompatActivity {

    private LinearLayout viewGroupLayout;
    private Typeface typeface;
    private TextView cityTextView, countryTextView, dateTimeTextView, tempTextView, conditionTextView, retryTextView;
    private ListView forecastListView;
    private ProgressDialog progressDialog;
    private ArrayList<HashMap<String, Object>> forecastArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Get Resource ID from XML
         */
        viewGroupLayout = (LinearLayout) findViewById(R.id.view_group_layout);
        cityTextView = (TextView) findViewById(R.id.city_text_view);
        countryTextView = (TextView) findViewById(R.id.country_text_view);
        dateTimeTextView = (TextView) findViewById(R.id.date_time_text_view);
        tempTextView = (TextView) findViewById(R.id.temp_text_view);
        conditionTextView = (TextView) findViewById(R.id.condition_text_view);
        forecastListView = (ListView) findViewById(R.id.forecast_list_view);
        retryTextView = (TextView) findViewById(R.id.retry_text_view);

        /**
         * Apply font
         */
        typeface = Typeface.createFromAsset(getAssets(), "fonts/helvetica_neue_lt_com_55_roman.ttf");
        changeTypeface(viewGroupLayout);

        /**
         * Check Network availability
         */
        if (NetworkHelper.isNetworkAvailable(MainActivity.this)) {
            getWeatherForecastData();
            forecastListView.setVisibility(View.VISIBLE);
            retryTextView.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(MainActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            forecastListView.setVisibility(View.INVISIBLE);
            retryTextView.setVisibility(View.VISIBLE);
        }

        /**
         * Retry button click event
         */
        retryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkHelper.isNetworkAvailable(MainActivity.this)) {
                    getWeatherForecastData();
                    forecastListView.setVisibility(View.VISIBLE);
                    retryTextView.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    forecastListView.setVisibility(View.INVISIBLE);
                    retryTextView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    /**
     * Method used for apply the font throughout the screen
     *
     * @param vGroup
     */
    private void changeTypeface(ViewGroup vGroup) {
        for (int i = 0; i < vGroup.getChildCount(); i++) {
            View v = vGroup.getChildAt(i);
            if (v instanceof ImageView || v instanceof ImageButton || v instanceof ListView)
                continue;
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeface);
            } else if (v instanceof RadioButton) {
                ((RadioButton) v).setTypeface(typeface);
            } else if (v instanceof DrawerLayout || v instanceof FrameLayout
                    || v instanceof LinearLayout || v instanceof RelativeLayout
                    || v instanceof RadioGroup || v instanceof ViewGroup) {
                changeTypeface((ViewGroup) v);
            }
        }
    }

    private void getWeatherForecastData() {

        /**
         * Progress Dialog
         */
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        /**
         * Call API by using retrofit
         */
        MethodUtils.getApiService().getForeCastData(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject queryObject = jsonObject.getJSONObject("query");
                    JSONObject resultsObject = queryObject.getJSONObject("results");
                    JSONObject channelObject = resultsObject.getJSONObject("channel");
                    JSONObject unitsObject = channelObject.getJSONObject("units");
                    JSONObject locationObject = channelObject.getJSONObject("location");
                    JSONObject itemObject = channelObject.getJSONObject("item");
                    JSONObject conditionObject = itemObject.getJSONObject("condition");
                    JSONArray forecastArray = itemObject.getJSONArray("forecast");
                    int size = forecastArray.length();
                    if (size > 0) {
                        forecastArrayList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            JSONObject forecastObj = forecastArray.getJSONObject(i);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("day", forecastObj.getString("day"));
                            hashMap.put("date", forecastObj.getString("date"));
                            hashMap.put("high", forecastObj.getString("high"));
                            hashMap.put("low", forecastObj.getString("low"));
                            hashMap.put("text", forecastObj.getString("text"));
                            forecastArrayList.add(hashMap);
                        }
                    }

                    tempTextView.setText(conditionObject.getString("temp") + ((char) 0x00B0) + " " + unitsObject.getString("temperature"));
                    conditionTextView.setText(conditionObject.getString("text"));

                    cityTextView.setText(locationObject.getString("city"));
                    countryTextView.setText(locationObject.getString("country"));

                    ForecastAdapter forecastAdapter = new ForecastAdapter(MainActivity.this, forecastArrayList);
                    forecastListView.setAdapter(forecastAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });


    }

}
