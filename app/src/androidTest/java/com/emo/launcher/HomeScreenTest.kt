package com.emo.launcher

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launcherStarts() {
        // The HOME activity must render without throwing.
        rule.waitForIdle()
    }
}
