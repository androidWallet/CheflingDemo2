package com.mguru2.utils;

import com.mguru2.webservice.WebService;

import retrofit.RestAdapter;

/**
 * Created by Sk Maniruddin on 23-10-2016.
 */
public class MethodUtils {
    static WebService apiService = null;

    /**
     * Method to return Rest API service
     */
    public static WebService getApiService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://query.yahooapis.com/v1")
                .setConverter(new StringConverter())
                .build();
        apiService = restAdapter.create(WebService.class);
        return apiService;
    }
}
