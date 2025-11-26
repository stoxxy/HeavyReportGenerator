package com.example.heavyreportgenerator.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class CleanupWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        delay(DELAY)
        Log.d(this::class.simpleName, "Cleanup of old files complete.")

        return Result.success()
    }

    companion object {
        private const val DELAY = 1000L
    }
}