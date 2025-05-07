package klo0812.mlaserna.weatherweatherlang.models.view

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import klo0812.mlaserna.weatherweatherlang.composables.controllers.MainScreens

class DashboardViewModel(
    private val navController: NavController?,
    private val saveStateHandle: SavedStateHandle,
): ViewModel() {

    companion object {
        val TAG: String? = DashboardViewModel::class.simpleName

        fun Factory(navController: NavController?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DashboardViewModel(
                    navController = navController,
                    saveStateHandle = createSavedStateHandle())
            }
        }
    }

    fun logOut() {
        Log.w(TAG, "Logging out.")
        navController?.navigate(MainScreens.LoginPage.route) {
            popUpTo(MainScreens.DashboardPage.route) {
                inclusive = true
            }
        }
    }

}