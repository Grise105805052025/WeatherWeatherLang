package klo0812.mlaserna.weatherweatherlang.models

class WeatherResponseModel(
    coor: Coordinates?,
    weather: Weather?,
    base: String?,
    main: Data?,
    visibility: Int?,
    wind: Wind?,
    rain: Rain?,
    clouds: Clouds?,
    dt: Long?,
    sys: Sys,
    timezone: Long?,
    id: Long?,
    name: String?,
    cod: Int?
)

class Coordinates(
    var lon: Double?,
    var lat: Double?,
)

class Weather(
    var id: Long?,
    var main: String?,
    var description: String?,
    var icon: String?
)

class Data(
    var temp: Double?,
    var feels_like: Double?,
    var temp_min: Double?,
    var temp_max: Double?,
    var pressure: Int?,
    var humidity: Int?,
    var sea_level: Int?,
    var grnd_level: Int?
)

class Wind(
    var speed: Double?,
    var deg: Int?,
    var gust: Double?
)

class Rain(
    var hourRain: Double? // 1h
)

class Clouds(
    var all: Int?
)

class Sys(
    var type: Int?,
    var id: Int?,
    var country: String?,
    var sunrise: Long?,
    var sunset: Long?
)