package com.example.datastoresample

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.datastoresample.AppPreferences.PreferencesKey.KEY_BOOKMARK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AppPreferences(context: Context) {

    private val appContext = context.applicationContext
    private val Context.dataStore by preferencesDataStore(
        name = PREFS_NAME,
        produceMigrations = {
            listOf(
                SharedPreferencesMigration(context, PREFS_NAME)
            )
        })


    val bookmark: Flow<String?>
        get() = appContext.dataStore.data.catch { exception ->
            // dataStore.data throws an IOException if it can't read the data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[KEY_BOOKMARK] ?: "none"
        }

    suspend fun saveBookmark(bookmark: String) {
        appContext.dataStore.edit { preferences ->
            preferences[KEY_BOOKMARK] = bookmark
        }
    }

    object PreferencesKey {
        val KEY_BOOKMARK = stringPreferencesKey("key_bookmark")
    }

    companion object {
        private const val PREFS_NAME = "app_preferences"
    }
}