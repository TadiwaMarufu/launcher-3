package com.emo.launcher.ui.home

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Context

class LauncherWidgetHost(
    context: Context
) : AppWidgetHost(
    context,
    HOST_ID
) {
    companion object {
        const val HOST_ID = 0x454D4F
    }

    val manager: AppWidgetManager =
        AppWidgetManager.getInstance(context)
}
