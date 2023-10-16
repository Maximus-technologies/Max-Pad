package com.max.lib.utils

import android.content.Context
import com.max.lib.BuildConfig

fun verifyInstallerId(context: Context): Boolean {
    val validInstallers: List<String> =
        ArrayList(listOf("com.android.vending", "com.google.android.feedback"))
    val installer = context.packageManager.getInstallerPackageName(context.packageName)

    // true Play Store
    return if (BuildConfig.DEBUG) {
        true
    } else
        installer != null && validInstallers.contains(installer)
}