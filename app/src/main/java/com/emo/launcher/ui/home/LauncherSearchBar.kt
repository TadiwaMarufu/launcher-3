package com.emo.launcher.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LauncherClock() {

    var now by remember {
        mutableStateOf(Date())
    }

    LaunchedEffect(Unit) {

        while (true) {

            now = Date()

            delay(1000)
        }
    }

    val time =
        SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(now)

    val date =
        SimpleDateFormat(
            "EEEE, d MMMM",
            Locale.getDefault()
        ).format(now)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = time,
            fontSize = 64.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = date.uppercase(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.65f
            )
        )
    }
}