package klo0812.mlaserna.weatherweatherlang.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import klo0812.mlaserna.weatherweatherlang.models.Clouds
import klo0812.mlaserna.weatherweatherlang.models.Coordinates
import klo0812.mlaserna.weatherweatherlang.models.Data
import klo0812.mlaserna.weatherweatherlang.models.Rain
import klo0812.mlaserna.weatherweatherlang.models.Sys
import klo0812.mlaserna.weatherweatherlang.models.Weather
import klo0812.mlaserna.weatherweatherlang.models.Wind

@Entity
@TypeConverters(
    WeatherEntity.CoordinatesConverter::class,
    WeatherEntity.WeatherConverter::class,
    WeatherEntity.DataConverter::class,
    WeatherEntity.WindConverter::class,
    WeatherEntity.RainConverter::class,
    WeatherEntity.CloudsConverter::class,
    WeatherEntity.SysConverter::class
)
data class WeatherEntity(
    @PrimaryKey
    val id: Long,
    @Embedded
    val userEntity: UserEntity,
    val coor: Coordinates,
    val weather: Weather,
    val base: String,
    val main: Data,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Long,
    val name: String,
    val cod: Int
) {

    class CoordinatesConverter {
        @TypeConverter
        fun toCoor(value: String): Coordinates = Gson().fromJson<Coordinates>(value, Coordinates::class.java)

        @TypeConverter
        fun fromCoor(value: Coordinates): String = Gson().toJson(value)
    }

    class WeatherConverter {
        @TypeConverter
        fun toWeather(value: String): Weather = Gson().fromJson<Weather>(value, Weather::class.java)

        @TypeConverter
        fun fromWeather(value: Weather): String = Gson().toJson(value)
    }

    class DataConverter {
        @TypeConverter
        fun toMain(value: String): Data = Gson().fromJson<Data>(value, Data::class.java)

        @TypeConverter
        fun fromMain(value: Data): String = Gson().toJson(value)
    }

    class WindConverter {
        @TypeConverter
        fun toWind(value: String): Wind = Gson().fromJson<Wind>(value, Wind::class.java)

        @TypeConverter
        fun fromWind(value: Wind): String = Gson().toJson(value)
    }

    class RainConverter {
        @TypeConverter
        fun toRain(value: String): Rain = Gson().fromJson<Rain>(value, Rain::class.java)

        @TypeConverter
        fun fromRain(value: Rain): String = Gson().toJson(value)
    }

    class CloudsConverter {
        @TypeConverter
        fun toClouds(value: String): Clouds = Gson().fromJson<Clouds>(value, Clouds::class.java)

        @TypeConverter
        fun fromClouds(value: Clouds): String = Gson().toJson(value)
    }

    class SysConverter {
        @TypeConverter
        fun toSys(value: String): Sys = Gson().fromJson<Sys>(value, Sys::class.java)

        @TypeConverter
        fun fromSys(value: Sys): String = Gson().toJson(value)
    }

}