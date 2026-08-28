package com.emo.launcher.model

data class LauncherSettings(
    val gridColumns: Int = 4,
    val gridRows: Int = 6,
    val iconSize: Float = 1f,
    val labelSize: Float = 1f,
    val showLabels: Boolean = true,
    val showDock: Boolean = true,
    val dockSize: Int = 4,
    val hapticFeedback: Boolean = true,
    val reducedMotion: Boolean = false,
    val wallpaperUri: String = "",
    val wallpaperBrightness: Float = 0f,
    val wallpaperContrast: Float = 1f,
    val wallpaperPreset: String = "Pure"
)
