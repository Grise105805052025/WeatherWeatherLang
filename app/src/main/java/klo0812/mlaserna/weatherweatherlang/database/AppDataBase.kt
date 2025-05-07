package klo0812.mlaserna.weatherweatherlang.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import klo0812.mlaserna.weatherweatherlang.constants.AppConstants
import klo0812.mlaserna.weatherweatherlang.database.daos.UserDao
import klo0812.mlaserna.weatherweatherlang.database.daos.WeatherDao
import klo0812.mlaserna.weatherweatherlang.database.entities.UserEntity
import klo0812.mlaserna.weatherweatherlang.database.entities.WeatherEntity

@Database(
    entities = [
        UserEntity::class,
        WeatherEntity::class
    ],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    // RoomDataBase only used for caching logged users and the weather data associated with them.
    // We only save login data to backend service while weather data is kept in device cache.
    abstract fun userDao(): UserDao
    abstract fun weatherDao(): WeatherDao

    companion object {

        @Volatile private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                AppConstants.DATABASE_NAME).build()
        }

    }

}