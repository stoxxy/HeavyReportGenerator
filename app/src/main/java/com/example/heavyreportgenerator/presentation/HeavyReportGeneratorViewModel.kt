package com.example.heavyreportgenerator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heavyreportgenerator.domain.HeavyReportGeneratorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HeavyReportGeneratorViewModel(
    private val heavyReportGeneratorRepository: HeavyReportGeneratorRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun handleIntent(intent: HeavyReportGeneratorIntent) {
        when (intent) {
            is HeavyReportGeneratorIntent.InputFileName -> _uiState.update { it.copy(fileName = intent.fileName) }
            is HeavyReportGeneratorIntent.GenerateReport -> viewModelScope.launch { heavyReportGeneratorRepository.generateReport(
                isChargingRequirementOn = uiState.value.isChargingConstraintOn,
                fileName = uiState.value.fileName) }
            is HeavyReportGeneratorIntent.SetChargingConstraint -> _uiState.update { it.copy(isChargingConstraintOn = !it.isChargingConstraintOn) }
        }
    }
}

data class UiState(
    val isChargingConstraintOn: Boolean = false,
    val fileName: String = ""
)