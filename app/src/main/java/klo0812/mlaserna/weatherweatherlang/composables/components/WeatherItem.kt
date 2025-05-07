package klo0812.mlaserna.weatherweatherlang.composables.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.crossfade
import klo0812.mlaserna.weatherweatherlang.database.entities.UserEntity
import klo0812.mlaserna.weatherweatherlang.database.entities.WeatherEntity
import klo0812.mlaserna.weatherweatherlang.models.response.impl.Clouds
import klo0812.mlaserna.weatherweatherlang.models.response.impl.Coordinates
import klo0812.mlaserna.weatherweatherlang.models.response.impl.Data
import klo0812.mlaserna.weatherweatherlang.models.response.impl.Rain
import klo0812.mlaserna.weatherweatherlang.models.response.impl.Sys
import klo0812.mlaserna.weatherweatherlang.models.response.impl.Weather
import klo0812.mlaserna.weatherweatherlang.models.response.impl.Wind
import klo0812.mlaserna.weatherweatherlang.models.view.WeatherItemModel
import java.util.Calendar

@Composable
fun WeatherItem(
    isLandingScreen: Boolean = false,
    weatherEntity: WeatherEntity? = null
) {

    val context = LocalContext.current
    var weatherItemModel by remember {
        mutableStateOf<WeatherItemModel?>(null)
    }

    LaunchedEffect(weatherEntity) {
        Log.d("WeatherItem", "Weather data has changed: $weatherEntity")
        weatherItemModel = WeatherItemModel(
            context,
            weatherEntity
        )
    }

    val timestamp by remember(weatherItemModel) {
        derivedStateOf { weatherItemModel?.initTime() }
    }
    val cityName by remember(weatherItemModel) {
        derivedStateOf { weatherItemModel?.cityName }
    }
    val description by remember(weatherItemModel) {
        derivedStateOf { weatherItemModel?.initDescription() }
    }
    val temperature by remember(weatherItemModel) {
        derivedStateOf { weatherItemModel?.initTemperature() }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = if (isLandingScreen) 0.dp else 8.dp,
                    topEnd = if (isLandingScreen) 0.dp else 8.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            )
            .background(color = Color.LightGray.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = 8.dp,
                        top = 8.dp
                    ),
                text = timestamp ?: "",
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp
                ),
                textAlign = TextAlign.End
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Surface (
                    modifier = Modifier.size(40.dp),
                    color = Color.Transparent
                ){
                    val icon = weatherEntity?.weather?.icon
                    if (icon == null) {
                        DefaultIcon()
                    } else {
                        AsyncImage(
                            modifier = Modifier.size(40.dp),
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data("https://openweathermap.org/img/w/${icon}.png")
                                .crossfade(true)
                                .listener(object: ImageRequest.Listener {
                                    override fun onError(request: ImageRequest, result: ErrorResult) {
                                        Log.d("WeatherItem", "Error loading image: ${result.throwable}")
                                    }
                                })
                                .build(),
                            contentDescription = "Weather Icon",
                            error = rememberVectorPainter(
                                Icons.Filled.ErrorOutline
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1.0f),
                ) {
                    Text(
                        text = cityName ?: "",
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = description ?: "",
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp
                        )
                    )
                }
                //TODO: Allow change from celsius to fahrenheit
                Text(
                    text = temperature ?: "",
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ))
            }
        }
    }
}
@Composable
private fun DefaultIcon(
    imageVector: ImageVector = Icons.Outlined.CloudOff
) {
    Image(
        modifier = Modifier
            .size(
                width = 40.dp,
                height = 40.dp
            ),
        imageVector = imageVector,
        contentDescription = "Default icon"
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun WeatherItemPreview() {
    Surface (modifier = Modifier.fillMaxSize()) {
        CloudBackground(
            modifier = Modifier.navigationBarsPadding(),
            colors = listOf(
                klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearLight,
                klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearMedium
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                WeatherItem(
                    weatherEntity = WeatherEntity(
                        id = 0,
                        userEntity = UserEntity(""),
                        timestamp = Calendar.getInstance().timeInMillis,
                        coor = Coordinates(0.0, 0.0),
                        weather = Weather(
                            id = 0,
                            main = "Clouds",
                            description = "broken clouds",
                            icon = "04n"),
                        base = "",
                        main = Data(
                            temp = 31.2,
                            feels_like = 35.2,
                            temp_min = 29.7,
                            temp_max = 33.1,
                            pressure = 1000,
                            humidity = 85,
                            sea_level = 1009,
                            grnd_level = 1011),
                        visibility = 10000,
                        wind = Wind(0.0, 0, 0.0),
                        rain = Rain(0.0),
                        clouds = Clouds(0),
                        dt = System.currentTimeMillis(),
                        sys = Sys(0, 0, "", 0, 0),
                        timezone = 0,
                        name = "",
                        cod = 0
                    )
                )
            }
        }
    }
}