package klo0812.mlaserna.weatherweatherlang.services

import android.content.Context
import androidx.navigation.NavController
import com.google.gson.Gson
import klo0812.mlaserna.weatherweatherlang.database.entities.UserEntity
import okhttp3.OkHttpClient

abstract class ServiceRepository<A, B>(
    val context: Context,
    navController: NavController?,
    open val baseUrl: String
) {
    open val client: OkHttpClient = OkHttpClientInstance.createOkHttpClient(context, navController)
    open val gson = Gson()

    abstract suspend fun getData(vararg values: Any): A?

    abstract suspend fun getEntity(vararg value: Any): B?

    abstract fun convertData(user: UserEntity, data: A): B?

    abstract fun storeData(data: B)

}