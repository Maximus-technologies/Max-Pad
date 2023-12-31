/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

package com.max.main.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.addCallback
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.max.firebase.analytics.FirebaseAnalyticsHelper
import com.max.firebase.config.RemoteConfigManager
import com.max.firebase.messaging.MaxFCM
import com.max.lib.base.MVVMBaseActivity
import com.max.main.databinding.ActivitySplashBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.system.exitProcess

class SplashActivity : MVVMBaseActivity<ActivitySplashBinding>() {
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper by inject()
    private val vm: SplashViewModel by viewModel()
    private val remoteConfigManager: RemoteConfigManager by inject()
    private var splashScreen: SplashScreen? = null

    override fun setBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun runBeforeCreating() {
        super.runBeforeCreating()
        splashScreen = installSplashScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MaxFCM.init()
        initializeRemoteConfig {
            //Continue Doing what needs to be done
            firebaseAnalyticsHelper.logEvent("Application_started")
        }

        onBackPressedDispatcher.addCallback {
            finishAffinity()
            finishAndRemoveTask()
            exitProcess(0)
        }

    }

    private fun initializeRemoteConfig(listener: () -> Unit) {
        logger.d("Request New Remote Config")
        lifecycleScope.launch {
            val result = remoteConfigManager.fetchRemoteConfig()
            if (result.isSuccess) {
                val remoteConfig = result.getOrNull()
                logger.e("Result", "${remoteConfig?.home}")
                listener.invoke()
            } else {
                logger.e("Result", "${result.exceptionOrNull()}")
            }
            listener.invoke()
        }
    }
}
