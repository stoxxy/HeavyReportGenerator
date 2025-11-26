package com.example.heavyreportgenerator.domain

interface HeavyReportGeneratorRepository {
    suspend fun generateReport(isChargingRequirementOn: Boolean, fileName: String)
}