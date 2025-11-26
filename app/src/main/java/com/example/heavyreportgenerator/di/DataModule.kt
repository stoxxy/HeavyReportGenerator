package com.example.heavyreportgenerator.di

import androidx.work.WorkManager
import com.example.heavyreportgenerator.data.HeavyReportGeneratorRepositoryImpl
import com.example.heavyreportgenerator.data.HeavyReportWorkManagerImpl
import com.example.heavyreportgenerator.data.PreferencesManagerImpl
import com.example.heavyreportgenerator.data.preferencesDataStore
import com.example.heavyreportgenerator.domain.HeavyReportGeneratorRepository
import com.example.heavyreportgenerator.domain.HeavyReportWorkManager
import com.example.heavyreportgenerator.domain.PreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { WorkManager.getInstance(androidContext()) }
    single { androidContext().preferencesDataStore }

    single<PreferencesManager> { PreferencesManagerImpl(dataStore = get()) }
    single<HeavyReportWorkManager> { HeavyReportWorkManagerImpl(workManager = get()) }
    single<HeavyReportGeneratorRepository> { HeavyReportGeneratorRepositoryImpl(
        heavyReportWorkManager = get(),
        preferencesManager = get()
    ) }
}