package com.namrata.weather.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key") key : String,
        @Query("q") q :String
    ): Response<WeatherModel>
}