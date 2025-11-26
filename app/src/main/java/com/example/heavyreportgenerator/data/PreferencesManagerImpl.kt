package com.example.heavyreportgenerator.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.heavyreportgenerator.domain.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.preferencesDataStore by preferencesDataStore(name = PreferencesManagerImpl.PREFERENCES_NAME)
class PreferencesManagerImpl(
    private val dataStore: DataStore<Preferences>
): PreferencesManager {
    private val isChargingRequiredKey = booleanPreferencesKey(name = IS_CHARGING_REQUIRED_KEY_NAME)

    override suspend fun toggleIsChargingRequired() {
        dataStore.edit {
            it[isChargingRequiredKey] = !(it[isChargingRequiredKey] ?: false)
        }
    }

    override fun isChargingRequiredFlow(): Flow<Boolean> =
        dataStore.data.map { it[isChargingRequiredKey] ?: false }

    companion object {
        const val PREFERENCES_NAME = "preferences"
        const val IS_CHARGING_REQUIRED_KEY_NAME = "isChargingRequired"
    }
}