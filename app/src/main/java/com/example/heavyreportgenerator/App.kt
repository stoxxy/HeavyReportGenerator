package com.example.heavyreportgenerator

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.heavyreportgenerator.data.CleanupWorker
import com.example.heavyreportgenerator.di.dataModule
import com.example.heavyreportgenerator.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class App(): Application(), Configuration.Provider {
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

    private lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()

        val koin = startKoin {
            androidContext(applicationContext)
            modules(dataModule, presentationModule)
        }.koin
        workManager = koin.get<WorkManager>()

        startPeriodicCleanupJob()
    }

    private fun startPeriodicCleanupJob() {
        val constraint = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val cleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraint)
            .addTag("periodicCleanup")
            .build()

        workManager
            .enqueueUniquePeriodicWork("periodicCleanupWork", ExistingPeriodicWorkPolicy.KEEP, cleanupRequest)
    }
}