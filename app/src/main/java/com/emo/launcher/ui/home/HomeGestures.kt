package com.emo.launcher.ui.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.emoHomeGestures(
    onDoubleTap: () -> Unit,
    onLongPress: () -> Unit,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit
): Modifier {
    return pointerInput(Unit) {

        detectTapGestures(
            onDoubleTap = {
                onDoubleTap()
            },
            onLongPress = {
                onLongPress()
            }
        )
    }.pointerInput(Unit) {

        var totalDrag = 0f

        detectVerticalDragGestures(
            onDragStart = {
                totalDrag = 0f
            },
            onVerticalDrag = { _, dragAmount ->
                totalDrag += dragAmount
            },
            onDragEnd = {
                when {
                    totalDrag < -100f -> onSwipeUp()
                    totalDrag > 100f -> onSwipeDown()
                }

                totalDrag = 0f
            }
        )
    }
}
