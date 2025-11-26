package com.example.heavyreportgenerator

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.example.heavyreportgenerator.data.work.GenerateReportWorker
import com.example.heavyreportgenerator.data.work.WorkerConstants.Companion.FILE_NAME_KEY
import com.example.heavyreportgenerator.data.work.WorkerConstants.Companion.FINAL_PATH_KEY
import com.example.heavyreportgenerator.data.work.WorkerConstants.Companion.REPORTS_LOCATION
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class GenerateReportWorkerTest {
    private lateinit var generateReportWorkerBuilder: TestListenableWorkerBuilder<GenerateReportWorker>

    @Before
    fun setUp() {
        generateReportWorkerBuilder = TestListenableWorkerBuilder<GenerateReportWorker>(
            context = ApplicationProvider.getApplicationContext()
        ).setForegroundUpdater(mockk(relaxed = true))
    }

    @Test
    fun test_outputCorrect() = runTest {
        val input = workDataOf(FILE_NAME_KEY to "Test_Report")

        val worker = generateReportWorkerBuilder.setInputData(input).build()
        val result = worker.doWork()

        assertEquals(result, ListenableWorker.Result.success(
                workDataOf(FINAL_PATH_KEY to "$REPORTS_LOCATION/Test_Report.csv")
        ))
    }
}