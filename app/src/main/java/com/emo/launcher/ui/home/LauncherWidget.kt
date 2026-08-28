package com.emo.launcher.ui.home

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun LauncherWidget(
    widgetView: View?,
    modifier: Modifier = Modifier
) {
    if (widgetView != null) {
        AndroidView(
            factory = { widgetView },
            modifier = modifier
        )
    }
}
