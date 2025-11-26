package com.example.heavyreportgenerator.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.heavyreportgenerator.R
import com.example.heavyreportgenerator.util.isPostNotificationGranted
import org.koin.androidx.compose.koinViewModel

@Composable
fun HeavyReportGeneratorScreen(
    modifier: Modifier,
    viewModel: HeavyReportGeneratorViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isPostNotificationPermissionGranted by rememberSaveable {
        mutableStateOf(context.isPostNotificationGranted())
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isPostNotificationPermissionGranted = isGranted
    }

    LaunchedEffect(isPostNotificationPermissionGranted) {
        if (!isPostNotificationPermissionGranted && Build.VERSION.SDK_INT >= 33) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    HeavyReportGeneratorContent(
        modifier = modifier,
        uiState = uiState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
private fun HeavyReportGeneratorContent(
    modifier: Modifier,
    uiState: UiState,
    onIntent: (HeavyReportGeneratorIntent) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.fileName,
            placeholder = { Text(stringResource(R.string.input_report_name)) },
            onValueChange = { onIntent(HeavyReportGeneratorIntent.InputFileName(it)) }
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp,
                    Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.must_be_charging))
                Checkbox(
                    checked = uiState.isChargingConstraintOn,
                    onCheckedChange = { onIntent(HeavyReportGeneratorIntent.SetChargingConstraint) }
                )
            }
            ElevatedButton(
                onClick = { onIntent(HeavyReportGeneratorIntent.GenerateReport) },
                enabled = uiState.fileName.isNotBlank()
            ) {
                Text(stringResource(R.string.generate_report))
            }
        }
    }
}