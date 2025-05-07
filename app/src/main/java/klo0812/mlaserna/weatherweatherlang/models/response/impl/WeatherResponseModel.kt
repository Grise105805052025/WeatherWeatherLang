package klo0812.mlaserna.weatherweatherlang.models.response.impl

import com.google.gson.annotations.SerializedName
import klo0812.mlaserna.weatherweatherlang.models.response.ResponseModel

data class WeatherResponseModel(
    val coord: Coordinates? = null,
    val weather: Array<Weather>? = null,
    val base: String? = null,
    val main: Data? = null,
    val visibility: Int? = null,
    val wind: Wind? = null,
    val rain: Rain? = null,
    val clouds: Clouds? = null,
    val dt: Long? = null,
    val sys: Sys? = null,
    val timezone: Long? = null,
    val id: Long? = null,
    val name: String? = null,
    val cod: Int? = null
) : ResponseModel() {

    constructor(
        code: Int? = null,
        message: String? = null,
        exception: Exception? = null
    ) : this(
        coord = null,
        weather = null,
        base = null,
        main = null,
        visibility = null,
        wind = null,
        rain = null,
        clouds = null,
        dt = null,
        sys = null,
        timezone = null,
        id = null,
        name = null,
        cod = null
    ) {
        this.code = code
        this.message = message
        this.exception = exception
    }

    // auto-generated equals and hashCode methods
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherResponseModel

        if (visibility != other.visibility) return false
        if (dt != other.dt) return false
        if (timezone != other.timezone) return false
        if (id != other.id) return false
        if (cod != other.cod) return false
        if (coord != other.coord) return false
        if (!weather.contentEquals(other.weather)) return false
        if (base != other.base) return false
        if (main != other.main) return false
        if (wind != other.wind) return false
        if (rain != other.rain) return false
        if (clouds != other.clouds) return false
        if (sys != other.sys) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = visibility ?: 0
        result = 31 * result + (dt?.hashCode() ?: 0)
        result = 31 * result + (timezone?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (cod ?: 0)
        result = 31 * result + (coord?.hashCode() ?: 0)
        result = 31 * result + (weather?.contentHashCode() ?: 0)
        result = 31 * result + (base?.hashCode() ?: 0)
        result = 31 * result + (main?.hashCode() ?: 0)
        result = 31 * result + (wind?.hashCode() ?: 0)
        result = 31 * result + (rain?.hashCode() ?: 0)
        result = 31 * result + (clouds?.hashCode() ?: 0)
        result = 31 * result + (sys?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }
}

data class Coordinates(
    val lon: Double?,
    val lat: Double?,
)

data class Weather(
    val id: Long?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Data(
    var temp: Double?,
    val feels_like: Double?,
    val temp_min: Double?,
    val temp_max: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val sea_level: Int?,
    val grnd_level: Int?
)

data class Wind(
    val speed: Double?,
    val deg: Int?,
    val gust: Double?
)

data class Rain(
    @SerializedName("1h")
    val hourRain: Double?
)

data class Clouds(
    val all: Int?
)

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?
)