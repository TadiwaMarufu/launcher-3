package com.emo.launcher.ui.home

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.emo.launcher.model.WallpaperConfig
import com.emo.launcher.wallpaper.WallpaperRepository

@Composable
fun LauncherWallpaper(
    config: WallpaperConfig,
    repository: WallpaperRepository,
    modifier: Modifier = Modifier
) {
    val bitmap: Bitmap? =
        remember(
            config.uri,
            config.brightness,
            config.contrast,
            config.preset
        ) {
            repository.load(config)
        }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
