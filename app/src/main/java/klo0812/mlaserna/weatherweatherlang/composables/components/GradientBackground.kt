package klo0812.mlaserna.weatherweatherlang.composables.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
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
    alphaColor: Color = Color.Transparent,
    colors: List<Color>,
    content: @Composable () -> Unit?
) {
    val animatedColors = colors.map { color ->
        animateColorAsState(targetValue = color, animationSpec = tween(durationMillis = 1000), label = "" )
    }
    val animatedColorList = animatedColors.map { it.value }
    val modifier = modifier.background(
        gradientBackgroundBrush(
            isVerticalGradient,
            animatedColorList
        )
    )
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = alphaColor.copy(alpha = 0.1f)
    ) {
        content()
    }
}

@Composable
fun gradientBackgroundBrush (
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
    ) {
        // do nothing
    }
}