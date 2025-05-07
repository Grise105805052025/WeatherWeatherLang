package klo0812.mlaserna.weatherweatherlang.services.impl

import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import klo0812.mlaserna.weatherweatherlang.app.WWLApplication
import klo0812.mlaserna.weatherweatherlang.database.AppDataBase
import klo0812.mlaserna.weatherweatherlang.database.entities.UserEntity
import klo0812.mlaserna.weatherweatherlang.database.entities.WeatherEntity
import klo0812.mlaserna.weatherweatherlang.models.response.impl.WeatherResponseModel
import klo0812.mlaserna.weatherweatherlang.services.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Calendar

class WeatherRepository(
    context: Context,
    navController: NavController?,
    baseUrl: String
) : ServiceRepository<WeatherResponseModel, WeatherEntity>(
    context,
    navController,
    baseUrl
) {

    companion object {
        val TAG: String? = WeatherRepository::class.simpleName
    }

    override suspend fun getData(vararg values: Any): WeatherResponseModel? {
        return withContext(Dispatchers.IO) {
            val lat = values[0]
            val lon = values[1]
            val apiKey = values[2]
            val url = "${baseUrl}weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric"

            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    Log.e(TAG, "Request failed: ${response.code}")
                    return@withContext WeatherResponseModel(
                        code = response.code,
                        message = response.message
                    )
                }
                val body = response.body?.string() ?: return@withContext null
                return@withContext gson.fromJson(body, WeatherResponseModel::class.java)
            } catch (e: IOException) {
                Log.e(TAG, "Ran into issues.", e)
                return@withContext WeatherResponseModel(
                    exception = e
                )
            }
        }
    }

    override suspend fun getEntity(vararg values: Any): WeatherEntity? {
        val appDataBase = AppDataBase.getInstance(context)
        val authId = WWLApplication.getFirebaseAuth().currentUser?.uid
        return if (authId == null || values.isEmpty() || values.size != 1 || values[0] !is String) {
            null
        } else {
            appDataBase.weatherDao().get(values[0] as String, authId)
        }
    }

    override fun convertData(user: UserEntity, data: WeatherResponseModel): WeatherEntity {
        val timestamp = Calendar.getInstance().timeInMillis
        return WeatherEntity(user, data, timestamp)
    }

    override fun storeData(data: WeatherEntity) {
        AppDataBase.getInstance(context).weatherDao().insertAll(data)
    }

}