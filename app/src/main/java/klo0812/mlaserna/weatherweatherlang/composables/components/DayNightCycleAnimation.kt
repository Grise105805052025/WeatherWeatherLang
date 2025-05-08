package klo0812.mlaserna.weatherweatherlang.composables.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import klo0812.mlaserna.weatherweatherlang.R
import java.time.LocalDateTime

@Composable
fun DayNightCycleAnimation(
    imageSource: Int = R.drawable.day_and_night,
    startingAngle: Float = 0f,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val size = configuration.screenWidthDp.dp

    var currentRotation by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = currentRotation,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "rotationAnimation",
    )

    //TODO: Fix snapping problem
    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = LocalDateTime.now()
            val currentHour = currentTime.hour
            val currentMinute = currentTime.minute

            // Calculate the total minutes passed since midnight
            val totalMinutes = currentHour * 60 + (currentMinute)

            // Calculate the angle of rotation
            // 360 degrees / 24 hours = 15 degrees per hour or 1 degree per 4 minutes
            val rotationAngle = startingAngle + (totalMinutes / 4.0f) % 360f
            currentRotation = rotationAngle

            // Checks for rotation every minute
            kotlinx.coroutines.delay(60000L)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val options = BitmapFactory.Options()
        val originalBitmap = BitmapFactory.decodeResource(context.resources, imageSource, options)

        // Scale down bitmap to size that we need
        val scaledBitmap = scaleBitmap(originalBitmap, size.value.toInt())

        // Create size of canvas we need for rotating the bitmap
        val rotatedBitmap = createBitmap(
            size.value.toInt(),
            size.value.toInt(),
        )
        val canvas = Canvas(rotatedBitmap)

        // Re-center as needed
        val x = (size.value.toInt() - scaledBitmap.width) / 2f
        val y = (size.value.toInt() - scaledBitmap.height) / 2f

        // Create a Matrix for rotation
        val matrix = Matrix()
        matrix.postRotate(
            animatedRotation,
            scaledBitmap.width / 2f,
            scaledBitmap.height / 2f
        )
        matrix.postTranslate(x.toFloat(), y.toFloat())

        // Paint to be used when drawing the bitmap
        val paint = Paint()
        paint.isAntiAlias = true

        // Draw the scaledBitmap on the canvas
        canvas.drawBitmap(scaledBitmap, matrix, paint)

        // Display the final rotated and scaled down bitmap
        Image(
            bitmap = rotatedBitmap.asImageBitmap(),
            contentDescription = "Rotating Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(size)
                .height(size)
                .offset(y = -size / 2)
        )
    }
}

// Helper function to scale bitmap while maintaining aspect ratio
fun scaleBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
    val originalWidth = bitmap.width
    val originalHeight = bitmap.height

    val scale = if (originalWidth > originalHeight) {
        maxDimension.toFloat() / originalWidth
    } else {
        maxDimension.toFloat() / originalHeight
    }

    val newWidth = (originalWidth * scale).toInt()
    val newHeight = (originalHeight * scale).toInt()
    return bitmap.scale(newWidth, newHeight)
}

@Preview
@Composable
fun DayNightCycleAnimationPreview() {
    DayNightCycleAnimation()
}