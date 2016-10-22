package com.mguru2.webservice;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Sk Maniruddin on 23-10-2016.
 */
public interface WebService {

    @GET("/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22chicago%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
    void getForeCastData(Callback<String> callback);
}

