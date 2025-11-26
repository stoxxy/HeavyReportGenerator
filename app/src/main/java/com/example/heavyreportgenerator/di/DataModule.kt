package com.example.heavyreportgenerator.di

import androidx.work.WorkManager
import com.example.heavyreportgenerator.data.HeavyReportGeneratorRepositoryImpl
import com.example.heavyreportgenerator.domain.HeavyReportGeneratorRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { WorkManager.getInstance(androidContext()) }
    single<HeavyReportGeneratorRepository> { HeavyReportGeneratorRepositoryImpl(workManager = get()) }
}