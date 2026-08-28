package com.emo.launcher.security

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class EmoAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        AccessibilityLockController.attach(this)
    }

    override fun onDestroy() {
        AccessibilityLockController.detach(this)
        super.onDestroy()
    }

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {
        // EmoLauncher does not inspect screen content.
        // The service exists only to expose the system lock action.
    }

    override fun onInterrupt() {
        // Nothing to interrupt.
    }

    fun lockScreen(): Boolean {
        return performGlobalAction(
            GLOBAL_ACTION_LOCK_SCREEN
        )
    }
}
