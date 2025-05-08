package klo0812.mlaserna.weatherweatherlang.models.view

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import klo0812.mlaserna.weatherweatherlang.database.entities.WeatherEntity
import klo0812.mlaserna.weatherweatherlang.models.view.WeatherViewModel.Companion.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherItemModel(
    val context: Context,
    val data: WeatherEntity?
) {
    var cityName: String? by mutableStateOf("Unknown City")
        private set

    internal fun initTime(): String {
        return if (data != null) {
            SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(data.timestamp)
        } else {
            ""
        }
    }

    internal fun initDescription(): String {
        val description = data?.weather?.description
        return if (description != null) {
            description[0].uppercaseChar() + description.substring(1)
        } else {
            "Cannot find description."
        }
    }

    internal fun initTemperature(): String {
        val df = DecimalFormat("#.00")
        return "${df.format(data?.main?.temp)}°C"
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            findNearestCity(
                context,
                data?.coor?.lat,
                data?.coor?.lon
            )?.let {
                cityName = it
            }
        }
    }

    suspend fun findNearestCity(context: Context, latitude: Double?, longitude: Double?): String? {
        return withContext(Dispatchers.IO) {
            if (!Geocoder.isPresent()) {
                Log.e(TAG, "No Geocoder in this device. Unable to fetch for exact location.")
                return@withContext null
            } else if (latitude == null || longitude == null) {
                Log.e(TAG, "You must supply proper coordinates.")
                return@withContext null
            }

            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 5)
                if (addresses != null && addresses.isNotEmpty()) {
                    for (address in addresses) {
                        val city = address.locality
                        if (city != null) {
                            return@withContext city
                        }
                    }
                    Log.e(TAG, "No city data found.")
                    return@withContext null
                } else {
                    Log.e(TAG, "No valid addresses found in latitude: $latitude and longitude: ${longitude}.")
                    return@withContext null
                }
            } catch (e: IOException) {
                Log.e(TAG, "Something went wrong with Geocoder.", e)
                return@withContext null
            } catch (e: IllegalArgumentException) {
                Log.e("Geocoder", "Invalid latitude: $latitude or longitude: ${longitude}.", e)
                return@withContext null
            }
        }
    }
}