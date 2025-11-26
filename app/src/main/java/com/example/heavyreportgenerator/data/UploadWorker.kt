package com.example.heavyreportgenerator.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.heavyreportgenerator.data.WorkerConstants.Companion.FINAL_PATH_KEY
import kotlinx.coroutines.delay

class UploadWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val finalPath = inputData.getString(FINAL_PATH_KEY)

        delay(DELAY)
        Log.d(this::class.simpleName, "Upload for $finalPath complete.")

        return Result.success()
    }
    private companion object {
        const val DELAY = 2000L
    }
}