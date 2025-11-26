package com.example.heavyreportgenerator.data

import com.example.heavyreportgenerator.domain.HeavyReportGeneratorRepository
import com.example.heavyreportgenerator.domain.HeavyReportWorkManager
import com.example.heavyreportgenerator.domain.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class HeavyReportGeneratorRepositoryImpl(
    private val heavyReportWorkManager: HeavyReportWorkManager,
    private val preferencesManager: PreferencesManager
): HeavyReportGeneratorRepository {
    override suspend fun generateReport(fileName: String) {
        heavyReportWorkManager.generateReport(
            isChargingRequirementOn = isChargingRequired().first(),
            fileName = fileName
        )
    }

    override suspend fun toggleIsChargingRequired() {
        preferencesManager.toggleIsChargingRequired()
    }

    override fun isChargingRequired(): Flow<Boolean> = preferencesManager.isChargingRequiredFlow()
}