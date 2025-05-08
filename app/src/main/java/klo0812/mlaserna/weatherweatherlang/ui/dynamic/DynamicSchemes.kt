package klo0812.mlaserna.weatherweatherlang.ui.dynamic

import androidx.compose.ui.graphics.Color
import java.time.LocalTime
import java.util.concurrent.ThreadLocalRandom

fun rememberDynamicColorScheme(currentTime: LocalTime = generateRandomLocalTime()): DynamicColorScheme {

    return when {
        currentTime.isAfter(LocalTime.of(4, 0)) && currentTime.isBefore(LocalTime.of(5, 0)) -> {
            dawnColors
        }

        currentTime.isAfter(LocalTime.of(5, 0)) && currentTime.isBefore(LocalTime.of(6, 0)) -> {
            sunriseColors
        }

        currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(11, 0)) -> {
            morningColors
        }

        currentTime.isAfter(LocalTime.of(11, 0)) && currentTime.isBefore(LocalTime.of(13, 0)) -> {
            noonColors
        }

        currentTime.isAfter(LocalTime.of(13, 0)) && currentTime.isBefore(LocalTime.of(17, 0)) -> {
            afternoonColors
        }

        currentTime.isAfter(LocalTime.of(17, 0)) && currentTime.isBefore(LocalTime.of(18, 0)) -> {
            sunsetColors
        }

        else -> {
            eveningColors
        }
    }
}

// For testing rememberDynamicColorScheme()
fun generateRandomLocalTime(): LocalTime {
    val random = ThreadLocalRandom.current()

    // Generate random hours, minutes, seconds, and nanoseconds
    val hour = random.nextInt(0, 24) // 0 to 23
    val minute = random.nextInt(0, 60) // 0 to 59
    val second = random.nextInt(0, 60) // 0 to 59
    val nanoOfSecond = random.nextInt(0, 1_000_000_000) // 0 to 999,999,999

    return LocalTime.of(hour, minute, second, nanoOfSecond)
}

data class DynamicColorScheme(
    val name: String,
    val alpha: Color,
    val startColor: Color,
    val endColor: Color
)

val dawnColors = DynamicColorScheme(
    name = "Dawn",
    alpha = Color(0xFFCE75C2),
    startColor = Color(0xFF003285),
    endColor = Color(0xFFCE75C2),
)

val sunriseColors = DynamicColorScheme(
    name = "Sunrise",
    alpha = Color(0xFFDB907D),
    startColor = Color(0xFF3674B5),
    endColor = Color(0xFFDB907D),
)

val morningColors = DynamicColorScheme(
    name = "Morning",
    alpha = Color(0xFFF7E9D1),
    startColor = Color(0xFF3674B5),
    endColor = Color(0xFFF7E9D1),
)

val noonColors = DynamicColorScheme(
    name = "Noon",
    alpha = Color(0xFFFFD009),
    startColor = Color(0xFF3674B5),
    endColor = Color(0xFFFFD009),
)

val afternoonColors = DynamicColorScheme(
    name = "Afternoon",
    alpha = Color(0xFFFFAE42),
    startColor = Color(0xFF3674B5),
    endColor = Color(0xFFFFAE42),
)

val sunsetColors = DynamicColorScheme(
    name = "Sunset",
    alpha = Color(0xFFFD5E53),
    startColor = Color(0xFF003285),
    endColor = Color(0xFFFD5E53),
)

val eveningColors = DynamicColorScheme(
    name = "Evening",
    alpha = Color(0xFF6D54A9),
    startColor = Color(0xFF003285),
    endColor = Color(0xFF6D54A9),
)