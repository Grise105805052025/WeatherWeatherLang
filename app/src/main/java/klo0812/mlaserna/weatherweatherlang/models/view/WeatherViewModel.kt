package klo0812.mlaserna.weatherweatherlang.models.view

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import klo0812.mlaserna.weatherweatherlang.app.WWLApplication
import klo0812.mlaserna.weatherweatherlang.constants.AppConstants
import klo0812.mlaserna.weatherweatherlang.database.entities.UserEntity
import klo0812.mlaserna.weatherweatherlang.database.entities.WeatherEntity
import klo0812.mlaserna.weatherweatherlang.models.response.impl.WeatherResponseModel
import klo0812.mlaserna.weatherweatherlang.services.impl.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val saveStateHandle: SavedStateHandle,
    private val repository: WeatherRepository?
): ViewModel() {

    companion object {
        val TAG: String? = WeatherViewModel::class.simpleName

        fun Factory(
            context: Context,
            navController: NavController?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WeatherViewModel(
                    saveStateHandle = createSavedStateHandle(),
                    repository = WeatherRepository(
                        context,
                        navController,
                        AppConstants.WEATHER_API_URL
                    )
                )
            }
        }

        fun Factory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WeatherViewModel(
                    saveStateHandle = createSavedStateHandle(),
                    repository = null)
            }
        }

        const val IS_REFRESHING_KEY = "isRefreshing"
        const val LOCATION_KEY = "location"
        const val SINGLE_DATA = "singleData"
//        const val LIST_DATA = "listData"
    }

    private val _singleDataId = MutableStateFlow({
        // Still have to check if this works
//        val id = saveStateHandle.get<String>(SINGLE_DATA)
//        viewModelScope.launch {
//            if (id != null) {
//                _singleData.value = repository?.getEntity(id)
//            }
//        }
//        return@MutableStateFlow id
    }
    )

    private val _singleData = MutableStateFlow<WeatherEntity?>(null)
    val singleData = _singleData.asStateFlow()
    fun updateSingleData(data: WeatherEntity) {
        _singleData.value = data
        saveStateHandle[SINGLE_DATA] = data.id
    }

//    private val _listData = MutableStateFlow<List<WeatherEntity>>(emptyList())
//    val listData = _listData.asStateFlow()
//    fun updateListData(data: List<WeatherEntity>) {
//        _listData.value = data
//        saveStateHandle[LIST_DATA] = data
//    }

    private val _isRefreshing = MutableStateFlow(
        saveStateHandle.get<Boolean>(IS_REFRESHING_KEY)
    )
    val isRefreshing: StateFlow<Boolean?> = _isRefreshing.asStateFlow()
    fun updateRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
        saveStateHandle[IS_REFRESHING_KEY] = isRefreshing
    }

    private val _location = MutableStateFlow(
        saveStateHandle.get<Location>(LOCATION_KEY)
    )
    val location = _location.asStateFlow()
    fun updateLocation(location: Location) {
        _location.value = location
        saveStateHandle[LOCATION_KEY] = location
    }

    fun refresh() {
        updateRefreshing(true)
        fetchAndSaveCurrentWeather()
    }

    // simple periodic check for new weather data under a short time frame
    fun keepFetchingNewWeather() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        viewModelScope.launch {
            while (currentUser != null) {
                fetchAndSaveCurrentWeather()
                delay(1000)
            }
        }
    }

    fun fetchAndSaveCurrentWeather() {
        val latitude: Double? = location.value?.latitude
        val longitude: Double? = location.value?.longitude
        val firebaseAuth = WWLApplication.getFirebaseAuth()
        val currentUser = firebaseAuth.currentUser
        Log.d(TAG, "Fetching new weather data with location: $latitude:$longitude")
        if (repository != null && latitude != null && longitude != null && currentUser != null) {
            viewModelScope.launch {
                val response: WeatherResponseModel? = repository.getData(
                    latitude,
                    longitude,
                    AppConstants.WEATHER_API_KEY)
                val userEntity = UserEntity(currentUser.uid)
                if (response!!.cod == 200) {
                    val data = repository.convertData(userEntity, response)
                    updateSingleData(data)
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.storeData(data)
                    }
                }
                updateRefreshing(false)
            }
        } else {
            Log.w(TAG, "Unable to fetch new data. Repository is null. Wrong factory call used.")
            updateRefreshing(false)
        }
    }

}