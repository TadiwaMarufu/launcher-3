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
            decode(
                preferences[Keys.APP_POSITIONS]
            )
        }

    suspend fun reconcile(
        installedApps: List<HomeItem.App>,
        maxItems: Int
    ) {
        val installedById =
            installedApps.associateBy { it.id }

        val current =
            items.first()

        val existing =
            current
                .filter { it.id in installedById }
                .distinctBy { it.id }
                .sortedBy { it.position }

        val existingIds =
            existing.map { it.id }.toSet()

        val availablePositions =
            generateSequence(0) { it + 1 }
                .filter { position ->
                    existing.none {
                        it.position == position
                    }
                }
                .iterator()

        val newApps =
            installedApps
                .filter {
                    it.id !in existingIds
                }
                .mapNotNull { app ->
                    if (!availablePositions.hasNext()) {
                        null
                    } else {
                        HomeItem.App(
                            id = app.id,
                            packageName =
                                app.packageName,
                            activityName =
                                app.activityName,
                            position =
                                availablePositions.next()
                        )
                    }
                }

        val merged =
            (existing + newApps)
                .sortedBy { it.position }
                .take(maxItems)

        save(merged)
    }

    suspend fun moveApp(
        app: HomeItem.App,
        newPosition: Int
    ) {
        val current =
            items.first()

        val moving =
            current.find {
                it.id == app.id
            } ?: app

        val others =
            current
                .filterNot {
                    it.id == moving.id
                }
                .sortedBy { it.position }

        val target =
            newPosition.coerceAtLeast(0)

        val reordered =
            mutableListOf<HomeItem.App>()

        var inserted = false

        for (item in others) {
            if (!inserted &&
                item.position >= target
            ) {
                reordered.add(
                    moving.copy(
                        position =
                            reordered.size
                    )
                )
                inserted = true
            }

            reordered.add(
                item.copy(
                    position =
                        reordered.size
                )
            )
        }

        if (!inserted) {
            reordered.add(
                moving.copy(
                    position =
                        reordered.size
                )
            )
        }

        save(reordered)
    }

    suspend fun save(
        items: List<HomeItem.App>
    ) {
        context.homeLayoutDataStore.edit { preferences ->
            preferences[Keys.APP_POSITIONS] =
                encode(items)
        }
    }

    private fun encode(
        items: List<HomeItem.App>
    ): String {
        return items
            .sortedBy { it.position }
            .joinToString("|") { item ->
                listOf(
                    item.position,
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

                val packageName =
                    parts[1]

                val activityName =
                    parts[2]

                if (
                    packageName.isBlank() ||
                    activityName.isBlank()
                ) {
                    return@mapNotNull null
                }

                HomeItem.App(
                    id =
                        "$packageName/$activityName",
                    packageName =
                        packageName,
                    activityName =
                        activityName,
                    position =
                        position
                )
            }
            .distinctBy { it.id }
            .sortedBy { it.position }
    }
}
