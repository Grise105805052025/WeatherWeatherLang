package klo0812.mlaserna.weatherweatherlang.composables.pages.dashboard

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import klo0812.mlaserna.weatherweatherlang.app.WWLApplication
import klo0812.mlaserna.weatherweatherlang.composables.components.DayNightCycleAnimation
import klo0812.mlaserna.weatherweatherlang.composables.components.DigitalClock
import klo0812.mlaserna.weatherweatherlang.composables.components.GradientBackground
import klo0812.mlaserna.weatherweatherlang.composables.components.WeatherItem
import klo0812.mlaserna.weatherweatherlang.composables.components.button_shape
import klo0812.mlaserna.weatherweatherlang.database.AppDataBase
import klo0812.mlaserna.weatherweatherlang.database.entities.WeatherEntity
import klo0812.mlaserna.weatherweatherlang.models.view.DashboardViewModel
import klo0812.mlaserna.weatherweatherlang.models.view.WeatherViewModel
import klo0812.mlaserna.weatherweatherlang.services.location.LocationService
import klo0812.mlaserna.weatherweatherlang.ui.dynamic.rememberDynamicColorScheme
import klo0812.mlaserna.weatherweatherlang.ui.font.digitalClockFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun DashboardPage(
    navController: NavController?,
    viewModel: DashboardViewModel? = null,
    weatherViewModel: WeatherViewModel? = null
) {
    val context = LocalContext.current
    val viewModel: DashboardViewModel = viewModel ?: viewModel(
        factory = DashboardViewModel.Factory(navController)
    )
    val weatherViewModel: WeatherViewModel = weatherViewModel ?: viewModel(
        factory = WeatherViewModel.Factory(
            context,
            navController
        ))

    val data by weatherViewModel.singleData.collectAsState()
    val isRefreshing by weatherViewModel.isRefreshing.collectAsState() // keep for now
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val locationService = remember { LocationService(context) }
    val hasLocationPermission = remember {
        mutableStateOf(locationService.hasLocationPermission())
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    hasLocationPermission.value = true
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    hasLocationPermission.value = true
                } else -> {
                viewModel.logOut()
            }
            }
        }
    )

    // Start checking location data after permissions have been allowed
    LaunchedEffect(hasLocationPermission.value) {
        if (hasLocationPermission.value) {
            startLocationUpdates(
                weatherViewModel,
                locationService
            )
        }
    }

    var currentColorScheme by remember {
        mutableStateOf(rememberDynamicColorScheme())
    }

    // Change background color based on time of day
    LaunchedEffect(Unit) {
        while (true) {
            currentColorScheme = rememberDynamicColorScheme(LocalTime.now())
            delay(60000) // check every minute
        }
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Latest", "History")

    GradientBackground(
        modifier = Modifier
            .navigationBarsPadding(),
        colors = listOf(
            currentColorScheme.startColor,
            currentColorScheme.endColor
        )
    ) {

        if (showDialog) {
            AlertDialog(
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = {
                    showDialog = false
                },
                shape = RoundedCornerShape(4.dp),
                text = {
                    Text(
                        text = "Do you want to logout?",
                        style = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    )
                       },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.logOut()
                            showDialog = false
                        }
                    ) {
                        Text(
                            text = "Yes",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(
                            text = "No",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            )
        }
        Surface (color = Color.Transparent) {
            DayNightCycleAnimation(startingAngle = 90.0f)
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.0f)
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    AnimatedVisibility(
                        visible = !hasLocationPermission.value,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = Color.LightGray.copy(alpha = 0.4f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Hey there! In order for us to get an accurate reading on the weather, we would like to request access to some location permissions."
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = button_shape,
                                    onClick = {
                                        if (!hasLocationPermission.value) {
                                            requestPermissionLauncher.launch(
                                                arrayOf(
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                                )
                                            )
                                        }
                                    }
                                ) {
                                    Text("Proceed with authorization")
                                }
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = hasLocationPermission.value,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                    ) {
                        Column {
                            Box (modifier = Modifier.weight(0.33f)) { }
                            Column (modifier = Modifier.weight(0.67f)) {
                                TabRow(
                                    selectedTabIndex = selectedTabIndex) {
                                    tabs.forEachIndexed {
                                            index, title ->
                                        Tab(
                                            text = {
                                                Text(title)
                                            },
                                            selected = selectedTabIndex == index,
                                            onClick = {
                                                selectedTabIndex = index
                                            }
                                        )
                                    }
                                }

                                when (selectedTabIndex) {
                                    0 -> LandingPageContent(weatherEntity = data)
                                    1 -> WeatherListContent()
                                }
                            }
                        }
                    }
                }
            }
            Row (
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                DigitalClock(
                    textColor = Color.White,
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = digitalClockFamily,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        showDialog = true
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Logout!",
                        tint = Color.White
                    )
                }
            }
        }
    }

}

fun startLocationUpdates(
    weatherViewModel: WeatherViewModel,
    locationService: LocationService
) {
    CoroutineScope(IO).launch{
        weatherViewModel.updateRefreshing(true)
        // Only use this call for testing quick live changes
        // weatherViewModel.keepFetchingNewWeather()
        // routinely checks for new location and updates weather automatically
        locationService.listenToLocation(30000).collectLatest {
            weatherViewModel.updateLocation(it)
            weatherViewModel.refresh()
        }
    }
}

//TODO: Add preview
@Composable
fun LandingPageContent(
    weatherEntity: WeatherEntity?
) {

    var weatherData by remember {
        mutableStateOf(weatherEntity)
    }

    LaunchedEffect(weatherEntity) {
        Log.d("LandingPageContent", "Weather data has changed: $weatherEntity")
        weatherData = weatherEntity
    }

    Surface(
        color = Color.Transparent
    ) {
        Column {
            Spacer(Modifier.height(8.dp))
            if (weatherData == null)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = Color.LightGray.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                        )
                    }
                }
            else {
                WeatherItem(
                    showDetails = true,
                    weatherEntity = weatherData
                )
            }
        }
    }
}

//TODO: Add preview
@Composable
fun WeatherListContent() {
    val context = LocalContext.current
    val currentUser = WWLApplication.getFirebaseAuth().currentUser

    var weatherList by rememberSaveable {
        mutableStateOf(emptyList<WeatherEntity>())
    }

    LaunchedEffect(Unit) {
        CoroutineScope(IO).launch {
            weatherList = AppDataBase.getInstance(context).weatherDao().getAll(currentUser!!.uid).reversed()
        }
    }

    LazyColumn(
        modifier = Modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weatherList) { weather ->
            WeatherItem(weatherEntity = weather)
        }
    }
}

@Preview
@Composable
fun WelcomePagePreview() {
    //TODO: Find a way to supply navController properly
    DashboardPage(null)
}