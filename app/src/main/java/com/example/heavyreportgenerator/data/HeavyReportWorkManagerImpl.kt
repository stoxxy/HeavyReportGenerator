package com.example.heavyreportgenerator.data

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.heavyreportgenerator.data.work.CleanupWorker
import com.example.heavyreportgenerator.data.work.GenerateReportWorker
import com.example.heavyreportgenerator.data.work.UploadWorker
import com.example.heavyreportgenerator.data.work.WorkerConstants.Companion.FILE_NAME_KEY
import com.example.heavyreportgenerator.domain.HeavyReportWorkManager
import java.util.concurrent.TimeUnit

class HeavyReportWorkManagerImpl(
    private val workManager: WorkManager
): HeavyReportWorkManager {
    override fun generateReport(isChargingRequirementOn: Boolean, fileName: String) {
        val constraints = Constraints.Builder()
            .setRequiresCharging(isChargingRequirementOn)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val cleanupRequest = OneTimeWorkRequestBuilder<CleanupWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        val generateRequest = OneTimeWorkRequestBuilder<GenerateReportWorker>()
            .setInputData(workDataOf(FILE_NAME_KEY to fileName))
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        val uploadRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .build()

        workManager
            .beginUniqueWork("heavyReportWork", ExistingWorkPolicy.REPLACE, cleanupRequest)
            .then(generateRequest)
            .then(uploadRequest)
            .enqueue()
    }

    override fun performPeriodicCleanup() {
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