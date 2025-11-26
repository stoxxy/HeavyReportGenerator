package com.example.heavyreportgenerator.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun Context.isPostNotificationGranted() =
    (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(
        this, Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED)
            || Build.VERSION.SDK_INT < 32