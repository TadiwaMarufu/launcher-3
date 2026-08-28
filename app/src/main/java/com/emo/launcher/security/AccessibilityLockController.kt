package com.emo.launcher.security

import android.accessibilityservice.AccessibilityService

object AccessibilityLockController {

    @Volatile
    private var service: EmoAccessibilityService? = null

    fun attach(service: EmoAccessibilityService) {
        this.service = service
    }

    fun detach(service: EmoAccessibilityService) {
        if (this.service === service) {
            this.service = null
        }
    }

    fun isAvailable(): Boolean {
        return service != null
    }

    fun lock(): Boolean {
        return service?.lockScreen() == true
    }
}
