package klo0812.mlaserna.weatherweatherlang.interceptors

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import klo0812.mlaserna.weatherweatherlang.app.WWLApplication
import klo0812.mlaserna.weatherweatherlang.composables.controllers.MainScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context,
    private val navController: NavController?
) : Interceptor {

    //TODO: Properly delete currentUser and do additional checks to prevent multiple executions
    override fun intercept(chain: Interceptor.Chain): Response {
        val firebaseAuth = WWLApplication.getFirebaseAuth()
        val currentUser = firebaseAuth.currentUser
        var request = chain.request()

        if (currentUser != null) {
            // User is authenticated
            val idToken = try {
                // Get the ID token synchronously (use await() to block until it's available)
                runBlocking {
                    currentUser.getIdToken(true).await().token
                }
            } catch (e: Exception) {
                // Handle the error (e.g., user might have been signed out, token request failed)
                // Redirect the user to login screen
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "Authentication error. Please log in again.", Toast.LENGTH_LONG).show()
                    navController?.navigate(MainScreens.LoginPage.route)
                }
                return chain.proceed(request)
            }

            // Add the Authorization header to the request
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $idToken")
                .build()
        } else {
            // No user is signed in
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Authentication error. Please log in again.", Toast.LENGTH_LONG).show()
                navController?.navigate(MainScreens.LoginPage.route)
            }
        }

        return chain.proceed(request)
    }
}