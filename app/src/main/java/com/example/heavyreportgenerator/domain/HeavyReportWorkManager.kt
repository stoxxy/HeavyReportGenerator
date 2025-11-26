package com.example.heavyreportgenerator.domain

interface HeavyReportWorkManager {
    fun generateReport(isChargingRequirementOn: Boolean,
                       fileName: String)
    fun performPeriodicCleanup()
}