package com.emo.launcher.wallpaper

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.LruCache
import com.emo.launcher.model.WallpaperConfig
import java.io.IOException

class WallpaperRepository(
    private val context: Context
) {

    private val cache =
        LruCache<String, android.graphics.Bitmap>(2)

    fun load(config: WallpaperConfig): android.graphics.Bitmap? {
        if (config.uri.isBlank()) {
            return null
        }

        val key =
            "${config.uri}|${config.brightness}|${config.contrast}|${config.preset}"

        cache.get(key)?.let {
            return it
        }

        return runCatching {
            val uri = Uri.parse(config.uri)

            context.contentResolver
                .openInputStream(uri)
                .use { stream ->
                    if (stream == null) {
                        return null
                    }

                    val source =
                        BitmapFactory.decodeStream(stream)
                            ?: return null

                    val processed =
                        MonochromeWallpaperProcessor.process(
                            source = source,
                            brightness = config.brightness,
                            contrast = config.contrast,
                            preset = config.preset
                        )

                    cache.put(key, processed)
                    processed
                }
        }.getOrNull()
    }

    fun clearCache() {
        cache.evictAll()
    }
}
