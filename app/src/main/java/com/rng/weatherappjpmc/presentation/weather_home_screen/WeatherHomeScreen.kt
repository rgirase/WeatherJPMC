@file:OptIn(ExperimentalGlideComposeApi::class)

package com.rng.weatherappjpmc.presentation.weather_home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rng.weatherappjpmc.data.model.WeatherResponse
import com.rng.weatherappjpmc.presentation.viewmodel.WeatherViewModel
import com.rng.weatherappjpmc.ui.theme.DarkBlue
import com.rng.weatherappjpmc.ui.theme.DeepBlue
import com.rng.weatherappjpmc.utils.Utils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherHomeScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val searchText = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),

            ) {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter
            ) {
                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(text = "Search... zipcode or city", color = Color.White)
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.onEvent(WeatherHomeScreenEvents.OnSearchQueryChange(searchText.value))
                        focusManager.clearFocus()
                    }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
            }

            if (viewModel.weatherState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            if (viewModel.weatherState.weatherResponse != null) {
                viewModel.weatherState.weatherResponse?.let { weatherResponse ->
                    WeatherCard(
                        weatherResponse = weatherResponse
                    )

                }
            }

            if (viewModel.weatherState.errorMessage?.isNotEmpty() == true) {
                viewModel.weatherState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

        }

    }


}

@Composable
fun WeatherCard(weatherResponse: WeatherResponse) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .background(DeepBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Today",
                modifier = Modifier.align(Alignment.Start),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = weatherResponse.name + "," + weatherResponse.sys.country,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                val iconId = weatherResponse.weather[0].icon
                GlideImage(
                    model = "https://openweathermap.org/img/wn/${iconId}@2x.png",
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = Utils.convertTemperatureToCelsius(weatherResponse.main.temp)
                        .toString() + "°C",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Feels like " + Utils.convertTemperatureToCelsius(weatherResponse.main.feels_like)
                        .toString() + "°C.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                Text(
                    text = "${weatherResponse.weather[0].main}.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                Text(
                    text = weatherResponse.weather[0].description.capitalize(Locale.current),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Temp max: " + Utils.convertTemperatureToCelsius(weatherResponse.main.temp_max)
                        .toString() + "°C",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Temp min: " + Utils.convertTemperatureToCelsius(weatherResponse.main.temp_min)
                        .toString() + "°C",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Humidity: " + weatherResponse.main.humidity.toString() + "%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Visibility: " + Utils.convertToDecimal(weatherResponse.visibility) + "km",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
        }

    }
}
