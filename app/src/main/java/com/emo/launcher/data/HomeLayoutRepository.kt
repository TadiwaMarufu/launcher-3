package com.emo.launcher.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.emo.launcher.model.HomeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.homeLayoutDataStore by preferencesDataStore(
    name = "emo_launcher_home_layout"
)

class HomeLayoutRepository(
    private val context: Context
) {

    private object Keys {
        val APP_POSITIONS =
            stringPreferencesKey("app_positions")
    }

    val items: Flow<List<HomeItem.App>> =
        context.homeLayoutDataStore.data.map { preferences ->
            decode(preferences[Keys.APP_POSITIONS])
        }

    suspend fun save(
        items: List<HomeItem.App>
    ) {
        context.homeLayoutDataStore.edit { preferences ->
            preferences[Keys.APP_POSITIONS] =
                encode(items)
        }
    }

    suspend fun reconcile(
        installedApps: List<HomeItem.App>,
        maxItems: Int
    ) {
        val existing = items.first()

        val installedById =
            installedApps.associateBy { it.id }

        val kept =
            existing
                .filter { installedById.containsKey(it.id) }
                .sortedBy { it.position }

        val existingIds =
            kept.map { it.id }.toHashSet()

        val missing =
            installedApps
                .filterNot { existingIds.contains(it.id) }

        val merged =
            (kept + missing.mapIndexed { index, app ->
                app.copy(
                    position = kept.size + index
                )
            })
                .take(maxItems)
                .mapIndexed { index, app ->
                    app.copy(position = index)
                }

        save(merged)
    }

    suspend fun moveApp(
        packageName: String,
        activityName: String,
        position: Int
    ) {
        val current =
            items.first()

        val id =
            "$packageName/$activityName"

        val moving =
            current.firstOrNull {
                it.id == id
            } ?: HomeItem.App(
                id = id,
                packageName = packageName,
                activityName = activityName,
                position = position
            )

        val withoutMoving =
            current.filterNot {
                it.id == id
            }

        val target =
            position.coerceIn(
                0,
                withoutMoving.size
            )

        val updated =
            withoutMoving
                .toMutableList()
                .apply {
                    add(target, moving)
                }
                .mapIndexed { index, item ->
                    item.copy(
                        position = index
                    )
                }

        save(updated)
    }

    private fun encode(
        items: List<HomeItem.App>
    ): String {
        return items
            .sortedBy { it.position }
            .joinToString("|") { item ->
                listOf(
                    item.position.toString(),
                    item.packageName,
                    item.activityName
                ).joinToString(",")
            }
    }

    private fun decode(
        value: String?
    ): List<HomeItem.App> {
        if (value.isNullOrBlank()) {
            return emptyList()
        }

        return value
            .split("|")
            .mapNotNull { entry ->
                val parts =
                    entry.split(",")

                if (parts.size != 3) {
                    return@mapNotNull null
                }

                val position =
                    parts[0].toIntOrNull()
                        ?: return@mapNotNull null

                HomeItem.App(
                    id =
                        "${parts[1]}/${parts[2]}",
                    packageName =
                        parts[1],
                    activityName =
                        parts[2],
                    position =
                        position
                )
            }
            .sortedBy { it.position }
    }
}
