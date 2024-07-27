package com.namrata.weather.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.namrata.weather.api.NetworkResponse
import com.namrata.weather.api.WeatherModel
import com.namrata.weather.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(paddingValues: PaddingValues,weatherViewModel: WeatherViewModel) {
    var city by rememberSaveable {
        mutableStateOf("")
    }

    val weatherResult = weatherViewModel.weatherResult.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
       modifier = Modifier
           .padding(paddingValues)
           .fillMaxWidth(),
       horizontalAlignment = Alignment.CenterHorizontally
   ){
       Row(
           Modifier
               .fillMaxWidth()
               .padding(8.dp),
           horizontalArrangement = Arrangement.SpaceEvenly,
           verticalAlignment = Alignment.CenterVertically
       ){
              OutlinedTextField(
              modifier = Modifier.weight(1f),
              value=city,
              onValueChange = {
                  city =it
              },
              label = {Text("City Name")})
           IconButton(onClick = {
               weatherViewModel.getWeather(city)
               keyboardController?.hide()
           }) {
               Icon(imageVector = Icons.Default.Search ,
                   contentDescription = "SEARCH")
           }
       }
        when(val result=weatherResult.value){
            is NetworkResponse.Error ->
                Text(text = if(result.message==""){
                        "Result Not found"
                    }else{
                        result.message
                    }
                )
            NetworkResponse.Loading ->
                CircularProgressIndicator()
            is NetworkResponse.Success ->
                WeatherDetails(result.data)
            null->{}
        }
   }
}

@Composable
fun WeatherDetails(data:WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(imageVector = Icons.Default.LocationOn , 
                contentDescription = "LOCATION", 
                Modifier.size(20.dp)
            )
            Text(text = data.location.name+", ",
                //Modifier.weight(0.2f),
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                )
            Text(text = data.location.country,
             //   Modifier.weight(0.5f),
                fontSize = 20.sp, color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = " ${data.current.temp_c} ° c",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "Condition icon"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("Humidity",data.current.humidity)
                    WeatherKeyVal("Wind Speed",data.current.wind_kph+" km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("UV",data.current.uv)
                    WeatherKeyVal("Precipitation",data.current.precip_mm+" mm")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("Local Time",data.location.localtime.split(" ")[1])
                    WeatherKeyVal("Local Date",data.location.localtime.split(" ")[0])
                }
            }
        }
    }
}
@Composable
fun WeatherKeyVal(key : String, value : String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}