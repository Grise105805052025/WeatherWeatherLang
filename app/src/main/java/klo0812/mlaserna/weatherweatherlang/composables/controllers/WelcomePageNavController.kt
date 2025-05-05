package klo0812.mlaserna.weatherweatherlang.composables.controllers
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import klo0812.mlaserna.weatherweatherlang.composables.pages.welcome.LoginContent
import klo0812.mlaserna.weatherweatherlang.composables.pages.welcome.RegistrationContent

@Composable
fun WelcomePageNavController(
    mainNavController: NavController? = null,
    startDestination: String = WelcomeScreens.LoginContent.route
) {
    val welcomePageNavController = rememberNavController()

    NavHost(
        navController = welcomePageNavController,
        startDestination = startDestination
    ) {
        composable (route = WelcomeScreens.LoginContent.route) {
            LoginContent(mainNavController, welcomePageNavController)
        }
        composable (route = WelcomeScreens.RegistrationContent.route) {
            RegistrationContent(mainNavController, welcomePageNavController)
        }
    }
}

sealed class WelcomeScreens(val route: String) {
    object LoginContent: WelcomeScreens("login_content")
    object RegistrationContent: WelcomeScreens("registration_content")
}
