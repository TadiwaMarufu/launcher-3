package com.emo.launcher.ui.home

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.content.Context

class LauncherWidgetController(
    private val context: Context
) {
    private val host =
        LauncherWidgetHost(context)

    private val manager =
        AppWidgetManager.getInstance(context)

    fun startListening() {
        host.startListening()
    }

    fun stopListening() {
        host.stopListening()
    }

    fun createView(
        appWidgetId: Int
    ): AppWidgetHostView? {
        val info =
            manager.getAppWidgetInfo(appWidgetId)
                ?: return null

        return host.createView(
            context,
            appWidgetId,
            info
        )
    }

    fun allocateId(): Int =
        host.allocateAppWidgetId()

    fun deleteId(
        appWidgetId: Int
    ) {
        host.deleteAppWidgetId(appWidgetId)
    }
}
