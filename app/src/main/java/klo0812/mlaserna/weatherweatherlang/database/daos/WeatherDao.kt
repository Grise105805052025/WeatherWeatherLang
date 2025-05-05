package klo0812.mlaserna.weatherweatherlang.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import klo0812.mlaserna.weatherweatherlang.database.entities.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherEntity WHERE id = :id AND userId = :userId")
    fun get(id: String, userId: String) : WeatherEntity

    @Query("SELECT * FROM WeatherEntity WHERE userId = :userId")
    fun getAll(userId: String) : List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg weathers: WeatherEntity)

}