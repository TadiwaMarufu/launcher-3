package com.emo.launcher.data

import android.content.Context
import android.content.Intent
import com.emo.launcher.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    suspend fun loadApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        pm.queryIntentActivities(intent, 0)
            .asSequence()
            .mapNotNull { resolveInfo ->
                val activity = resolveInfo.activityInfo ?: return@mapNotNull null
                AppInfo(
                    label = resolveInfo.loadLabel(pm).toString(),
                    packageName = activity.packageName,
                    activityName = activity.name,
                    icon = resolveInfo.loadIcon(pm)
                )
            }
            .distinctBy { "${it.packageName}/${it.activityName}" }
            .sortedBy { it.label.lowercase() }
            .toList()
    }
}
