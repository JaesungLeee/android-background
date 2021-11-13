package com.jslee.jobscheduler_demo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherClient {

    const val BASE_URL = "http://api.openweathermap.org/"

    val retrofitInstance: WeatherRequest by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WeatherRequest::class.java)
    }
}