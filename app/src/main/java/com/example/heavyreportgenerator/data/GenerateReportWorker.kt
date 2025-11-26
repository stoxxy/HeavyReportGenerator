package com.example.heavyreportgenerator.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.heavyreportgenerator.data.WorkerConstants.Companion.FINAL_PATH_KEY
import com.example.heavyreportgenerator.data.WorkerConstants.Companion.REPORTS_LOCATION
import kotlinx.coroutines.delay

class GenerateReportWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val fileName = inputData.getString(WorkerConstants.Companion.FILE_NAME_KEY)

        delay(DELAY)
        Log.d(this::class.simpleName, "Report for $fileName generated.")

        val output = workDataOf(FINAL_PATH_KEY to "${REPORTS_LOCATION}/$fileName.csv")
        return Result.success(output)
    }

    companion object {
        private const val DELAY = 1000L
    }
}