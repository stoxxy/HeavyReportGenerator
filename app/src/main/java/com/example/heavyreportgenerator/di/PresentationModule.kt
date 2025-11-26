package com.example.heavyreportgenerator.di

import com.example.heavyreportgenerator.presentation.HeavyReportGeneratorViewModel
import org.koin.dsl.module

val presentationModule = module {
    single { HeavyReportGeneratorViewModel(
        heavyReportGeneratorRepository = get()
    ) }
}