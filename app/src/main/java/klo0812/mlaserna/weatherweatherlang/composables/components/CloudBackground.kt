package klo0812.mlaserna.weatherweatherlang.composables.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import klo0812.mlaserna.weatherweatherlang.R

@Composable
fun CloudBackground(
    modifier: Modifier = Modifier,
    isVerticalGradient: Boolean = true,
    alphaColor: Color = Color.Transparent,
    colors: List<Color>,
    content: @Composable () -> Unit?
) {
    var image = ImageBitmap.imageResource(R.drawable.cloud)
    val brush = remember(image) { ShaderBrush(ImageShader(image, TileMode.Repeated, TileMode.Repeated)) }
    Surface {
        GradientBackground(
            isVerticalGradient = isVerticalGradient,
            colors = colors
        ) {
            // do nothing
        }
        Surface(
            modifier = modifier
                .fillMaxSize()
                .background(brush),
            color = alphaColor.copy(alpha = 0.1f)
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun CloudBackgroundPreview() {
    Surface (modifier = Modifier.fillMaxSize()) {
        CloudBackground(
            modifier = Modifier.fillMaxSize(),
            colors = listOf(
                klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearLight,
                klo0812.mlaserna.weatherweatherlang.ui.theme.SkyClearMedium
            )
        ) { }
    }
}