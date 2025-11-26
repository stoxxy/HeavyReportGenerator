package com.example.heavyreportgenerator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heavyreportgenerator.domain.HeavyReportGeneratorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HeavyReportGeneratorViewModel(
    private val heavyReportGeneratorRepository: HeavyReportGeneratorRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState
        .combine(heavyReportGeneratorRepository.isChargingRequired(), { state, value ->
            state.copy(isChargingConstraintOn = value)
        })
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun handleIntent(intent: HeavyReportGeneratorIntent) {
        when (intent) {
            is HeavyReportGeneratorIntent.InputFileName -> _uiState.update { it.copy(fileName = intent.fileName) }
            is HeavyReportGeneratorIntent.GenerateReport -> viewModelScope.launch { heavyReportGeneratorRepository.generateReport(
                fileName = _uiState.value.fileName) }
            is HeavyReportGeneratorIntent.SetChargingConstraint -> viewModelScope.launch { heavyReportGeneratorRepository.toggleIsChargingRequired() }
        }
    }
}

data class UiState(
    val isChargingConstraintOn: Boolean = false,
    val fileName: String = ""
)