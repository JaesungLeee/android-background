package com.jslee.jobscheduler_demo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRequest {

    @GET("data/2.5/weather")
    fun getWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longtitude: String,
        @Query("appid") appid: String
    ) : Call<WeatherResponseDTO>
}