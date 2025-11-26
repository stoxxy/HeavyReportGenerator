package com.example.heavyreportgenerator.domain

import kotlinx.coroutines.flow.Flow

interface HeavyReportGeneratorRepository {
    suspend fun generateReport(fileName: String)
    suspend fun toggleIsChargingRequired()
    fun isChargingRequired(): Flow<Boolean>
}