package klo0812.mlaserna.weatherweatherlang.composables.pages.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import klo0812.mlaserna.weatherweatherlang.composables.components.CloudBackground
import klo0812.mlaserna.weatherweatherlang.composables.controllers.WelcomeScreens
import klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearLight
import klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearMedium

@Composable
fun DashboardPage(
    navController: NavController?,
    startDestination: String = WelcomeScreens.LoginContent.route
) {
    var colors = listOf(
        SkyClearLight,
        SkyClearMedium
    )
    CloudBackground(
        modifier = Modifier
            .navigationBarsPadding(),
        colors = colors
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {

            }
        }
    }
}

@Preview
@Composable
fun WelcomePagePreview() {
    DashboardPage(navController = null)
}