package com.example.heavyreportgenerator

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.heavyreportgenerator.di.dataModule
import com.example.heavyreportgenerator.di.presentationModule
import com.example.heavyreportgenerator.domain.HeavyReportWorkManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App(): Application(), Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()

        val koin = startKoin {
            androidContext(applicationContext)
            modules(dataModule, presentationModule)
        }.koin

        val heavyReportWorkManager = koin.get<HeavyReportWorkManager>()
        heavyReportWorkManager.performPeriodicCleanup()
    }
}