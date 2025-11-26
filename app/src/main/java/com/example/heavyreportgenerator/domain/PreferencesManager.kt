package com.example.heavyreportgenerator.domain

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    suspend fun toggleIsChargingRequired()
    fun isChargingRequiredFlow(): Flow<Boolean>
}