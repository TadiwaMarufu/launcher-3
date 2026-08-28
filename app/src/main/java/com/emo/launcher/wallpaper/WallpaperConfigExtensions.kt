package com.emo.launcher.wallpaper

import com.emo.launcher.model.LauncherSettings
import com.emo.launcher.model.WallpaperConfig

fun LauncherSettings.toWallpaperConfig(): WallpaperConfig {
    return WallpaperConfig(
        uri = wallpaperUri,
        brightness = wallpaperBrightness,
        contrast = wallpaperContrast,
        preset = wallpaperPreset
    )
}
