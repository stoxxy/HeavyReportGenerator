package com.example.heavyreportgenerator.presentation

interface HeavyReportGeneratorIntent {
    data class InputFileName(val fileName: String): HeavyReportGeneratorIntent
    data object GenerateReport: HeavyReportGeneratorIntent
    data object SetChargingConstraint: HeavyReportGeneratorIntent
}