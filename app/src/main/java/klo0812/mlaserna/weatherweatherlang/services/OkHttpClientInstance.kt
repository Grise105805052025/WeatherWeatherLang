package klo0812.mlaserna.weatherweatherlang.services

import android.content.Context
import androidx.navigation.NavController
import klo0812.mlaserna.weatherweatherlang.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpClientInstance {
    fun createOkHttpClient(
        context: Context,
        navController: NavController?
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = AuthInterceptor(context, navController)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }
}