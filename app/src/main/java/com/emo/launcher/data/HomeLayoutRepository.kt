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
    ): List<HomeItem.App> {

        val saved = items.first()

        val installedById =
            installedApps.associateBy { it.id }

        val validSaved = saved
            .asSequence()
            .filter { installedById.containsKey(it.id) }
            .sortedBy { it.position }
            .toList()

        val usedIds =
            validSaved.map { it.id }.toHashSet()

        val nextPosition =
            validSaved
                .maxOfOrNull { it.position }
                ?.plus(1)
                ?: 0

        var position = nextPosition

        val missing = installedApps
            .asSequence()
            .filterNot { usedIds.contains(it.id) }
            .sortedBy { it.label.lowercase() }
            .map { app ->
                val item =
                    app.copy(
                        position = position
                    )

                position++
                item
            }
            .toList()

        val result =
            (validSaved + missing)
                .sortedBy { it.position }
                .take(maxItems)

        save(result)

        return result
    }

    suspend fun moveApp(
        app: HomeItem.App,
        newPosition: Int
    ) {
        val current =
            items.first()
                .filterNot { it.id == app.id }
                .toMutableList()

        val clamped =
            newPosition.coerceAtLeast(0)

        current.add(
            app.copy(
                position = clamped
            )
        )

        val normalized =
            current
                .sortedBy { it.position }
                .mapIndexed { index, item ->
                    item.copy(position = index)
                }

        save(normalized)
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
                    parts[0]
                        .toIntOrNull()
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
                    id = "$packageName/$activityName",
                    packageName = packageName,
                    label = "",
                    position = position
                )
            }
            .sortedBy { it.position }
    }
}
