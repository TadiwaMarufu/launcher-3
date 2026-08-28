package com.emo.launcher.security

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager

class AccessibilityLockManager(
    private val context: Context
) {

    private val componentName =
        ComponentName(
            context,
            EmoAccessibilityService::class.java
        )

    fun isEnabled(): Boolean {
        val manager =
            context.getSystemService(
                Context.ACCESSIBILITY_SERVICE
            ) as? AccessibilityManager
                ?: return false

        return manager
            .getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_ALL_MASK
            )
            .any { serviceInfo ->
                val service = serviceInfo.resolveInfo?.serviceInfo

                service != null &&
                    ComponentName(
                        service.packageName,
                        service.name
                    ) == componentName
            }
    }

    fun openAccessibilitySettings() {
        context.startActivity(
            Intent(
                Settings.ACTION_ACCESSIBILITY_SETTINGS
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }
}
