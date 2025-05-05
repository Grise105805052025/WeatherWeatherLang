package klo0812.mlaserna.weatherweatherlang.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    isVerticalGradient: Boolean = true,
    colors: List<Color>
) {
    val modifier = modifier.background(
        GradientBackgroundBrush(
            isVerticalGradient,
            colors)
    )
    Box(modifier = modifier) { }
}

@Composable
fun GradientBackgroundBrush (
    isVerticalGradient: Boolean,
    colors: List<Color>
) : Brush {
    val endOffset = if (isVerticalGradient) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = endOffset
    )
}

@Preview
@Composable
fun GradientBackgroundPreview() {
    GradientBackground(
        modifier = Modifier.fillMaxSize(),
        isVerticalGradient = true,
        colors = listOf(
            klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearLight,
            klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearMedium
        )
    )
}