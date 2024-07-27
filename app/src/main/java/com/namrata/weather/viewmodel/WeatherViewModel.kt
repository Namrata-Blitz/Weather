package com.namrata.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namrata.weather.api.Constant
import com.namrata.weather.api.NetworkResponse
import com.namrata.weather.api.RetrofitInstance
import com.namrata.weather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    private val weatherApi=RetrofitInstance.weatherAPI
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult:LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getWeather(city:String){
        _weatherResult.value=NetworkResponse.Loading
        viewModelScope.launch {
            try{
                val response=weatherApi.getWeather(Constant.apiKey,city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value=NetworkResponse.Success(it)
                    }
                }else{
                    _weatherResult.value=NetworkResponse.Error(response.message())
                }
            }catch (_:Exception){
                _weatherResult.value=NetworkResponse.Error("Failed to load data")
            }

        }
    }
}