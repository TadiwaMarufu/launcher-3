package com.emo.launcher.security

object ScreenLockController {

    fun lock(): Boolean {
        return AccessibilityLockController.lock()
    }
}
