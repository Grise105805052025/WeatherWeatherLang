package klo0812.mlaserna.weatherweatherlang.composables.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DigitalClock(
    textColor: Color = Color.Black,
    textStyle: TextStyle
) {
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000L)
            currentTime = LocalDateTime.now()
        }
    }

    ClockDisplay(
        time = currentTime,
        textColor = textColor,
        texStyle = textStyle
    )
}

@Composable
fun ClockDisplay(
    time: LocalDateTime,
    textColor: Color,
    texStyle: TextStyle,
) {
    val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
    val formattedTime = time.format(formatter)
    Text(
        text = formattedTime,
        color = textColor,
        style = texStyle,
    )
}

@Preview(showBackground = true)
@Composable
fun DigitalClockPreview() {
    DigitalClock(
        textColor = Color.Black,
        textStyle = TextStyle(
            fontSize = 48.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}