package klo0812.mlaserna.weatherweatherlang.services.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationManager {
    fun listenToLocation(duration: Long): Flow<Location>
    fun hasLocationPermission(): Boolean
}