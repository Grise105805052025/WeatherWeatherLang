package klo0812.mlaserna.weatherweatherlang.composables.services

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import klo0812.mlaserna.weatherweatherlang.composables.controllers.MainScreens
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val auth: FirebaseAuth,
    private val context: Context,
    private val mainNavController: NavController
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentUser = auth.currentUser
        var request = chain.request()

        if (currentUser != null) {
            // User is authenticated
            val idToken = try {
                // Get the ID token synchronously (use await() to block until it's available)
                kotlinx.coroutines.runBlocking {
                    currentUser.getIdToken(true).await().token
                }
            } catch (e: Exception) {
                // Handle the error (e.g., user might have been signed out, token request failed)
                // Redirect the user to login screen
                Toast.makeText(context, "Authentication error. Please log in again.", Toast.LENGTH_LONG).show()
                mainNavController.navigate(MainScreens.LoginPage.route)
                return chain.proceed(request)
            }

            // Add the Authorization header to the request
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $idToken")
                .build()
        } else {
            // No user is signed in
            Toast.makeText(context, "Please sign in to access this service.", Toast.LENGTH_LONG).show()
            mainNavController.navigate(MainScreens.LoginPage.route)
        }

        return chain.proceed(request)
    }
}