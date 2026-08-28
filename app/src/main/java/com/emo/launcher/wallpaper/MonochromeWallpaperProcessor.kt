package com.emo.launcher.wallpaper

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.max
import kotlin.math.min

object MonochromeWallpaperProcessor {

    fun process(
        source: Bitmap,
        brightness: Float = 0f,
        contrast: Float = 1f,
        preset: String = "Pure"
    ): Bitmap {
        val output = source.copy(Bitmap.Config.ARGB_8888, true)

        val width = output.width
        val height = output.height
        val pixels = IntArray(width * height)

        output.getPixels(
            pixels,
            0,
            width,
            0,
            0,
            width,
            height
        )

        val presetContrast = when (preset) {
            "Soft" -> 0.85f
            "High Contrast" -> 1.35f
            "Noir" -> 1.55f
            "Film" -> 1.15f
            "Matte" -> 0.9f
            else -> 1f
        }

        val finalContrast =
            (contrast * presetContrast).coerceIn(0.5f, 2f)

        val brightnessOffset =
            brightness.coerceIn(-1f, 1f) * 255f

        for (i in pixels.indices) {
            val pixel = pixels[i]

            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)
            val alpha = Color.alpha(pixel)

            var luminance =
                (0.2126f * red) +
                (0.7152f * green) +
                (0.0722f * blue)

            luminance += brightnessOffset

            luminance =
                ((luminance - 128f) * finalContrast) + 128f

            luminance =
                max(0f, min(255f, luminance))

            val value = luminance.toInt()

            pixels[i] =
                Color.argb(
                    alpha,
                    value,
                    value,
                    value
                )
        }

        output.setPixels(
            pixels,
            0,
            width,
            0,
            0,
            width,
            height
        )

        return output
    }
}
