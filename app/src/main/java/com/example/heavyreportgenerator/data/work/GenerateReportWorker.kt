package com.example.heavyreportgenerator.data.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.heavyreportgenerator.R
import com.example.heavyreportgenerator.data.work.WorkerConstants
import com.example.heavyreportgenerator.data.work.WorkerConstants.Companion.FINAL_PATH_KEY
import com.example.heavyreportgenerator.data.work.WorkerConstants.Companion.REPORTS_LOCATION
import com.example.heavyreportgenerator.util.isPostNotificationGranted
import kotlinx.coroutines.delay

class GenerateReportWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private val notificationManager =
        context.getSystemService<NotificationManager>()

    override suspend fun doWork(): Result {
        val fileName = inputData.getString(WorkerConstants.Companion.FILE_NAME_KEY)

        if (applicationContext.isPostNotificationGranted()) setForeground(createForegroundInfo(fileName!!))

        delay(DELAY)
        Log.d(this::class.simpleName, "Report for $fileName generated.")

        val output = workDataOf(FINAL_PATH_KEY to "${REPORTS_LOCATION}/$fileName.csv")
        return Result.success(output)
    }

    private fun createForegroundInfo(fileName: String): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Report generation",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager!!.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        ).setContentTitle("Generating Report")
            .setContentText("Processing $fileName")
            .setSmallIcon(R.drawable.generating_icon)
            .setOngoing(true)
            .setProgress(100, 0, true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        return if (Build.VERSION.SDK_INT >= 34) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else ForegroundInfo(NOTIFICATION_ID, notification)
    }

    private companion object {
        const val DELAY = 3000L
        const val CHANNEL_ID = "report_generation_channel"
        private const val NOTIFICATION_ID = 100

    }
}