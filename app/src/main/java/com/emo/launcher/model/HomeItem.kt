package com.emo.launcher.model

sealed class HomeItem {
    abstract val id: String
    abstract val position: Int

    data class App(
        override val id: String,
        val packageName: String,
        val activityName: String,
        override val position: Int
    ) : HomeItem()

    data class Widget(
        override val id: String,
        val appWidgetId: Int,
        val providerPackage: String,
        val spanX: Int,
        val spanY: Int,
        override val position: Int
    ) : HomeItem()
}
