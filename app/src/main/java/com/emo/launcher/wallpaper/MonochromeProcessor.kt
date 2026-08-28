package com.emo.launcher.wallpaper

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.max
import kotlin.math.min

object MonochromeProcessor {

    fun process(
        source: Bitmap,
        brightness: Float = 0f,
        contrast: Float = 1f
    ): Bitmap {

        val output =
            Bitmap.createBitmap(
                source.width,
                source.height,
                Bitmap.Config.ARGB_8888
            )

        for (y in 0 until source.height) {
            for (x in 0 until source.width) {

                val pixel =
                    source.getPixel(x, y)

                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val a = Color.alpha(pixel)

                val luminance =
                    (0.2126f * r) +
                    (0.7152f * g) +
                    (0.0722f * b)

                var value =
                    ((luminance - 128f) * contrast) +
                    128f +
                    brightness * 255f

                value =
                    min(
                        255f,
                        max(0f, value)
                    )

                val gray =
                    value.toInt()

                output.setPixel(
                    x,
                    y,
                    Color.argb(
                        a,
                        gray,
                        gray,
                        gray
                    )
                )
            }
        }

        return output
    }
}
