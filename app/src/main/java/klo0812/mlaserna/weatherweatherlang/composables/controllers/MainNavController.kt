package klo0812.mlaserna.weatherweatherlang.composables.controllers
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import klo0812.mlaserna.weatherweatherlang.composables.pages.dashboard.DashboardPage
import klo0812.mlaserna.weatherweatherlang.composables.pages.welcome.WelcomePage

@Composable
fun MainNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainScreens.LoginPage.route) {
        composable (route = MainScreens.LoginPage.route) {
            WelcomePage(navController)
        }
        composable (route = MainScreens.DashboardPage.route) {
            DashboardPage(navController)
        }
    }
}

sealed class MainScreens(val route: String) {
    object LoginPage: MainScreens("login_page")
    object DashboardPage: MainScreens("dashboard_page")
}