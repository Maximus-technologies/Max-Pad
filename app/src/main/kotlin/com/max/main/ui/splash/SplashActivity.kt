package com.max.main.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import com.max.firebase.analytics.FirebaseAnalyticsHelper
import com.max.lib.base.MVVMBaseActivity
import com.max.main.databinding.ActivitySplashBinding
import org.koin.android.ext.android.inject

class SplashActivity : MVVMBaseActivity<ActivitySplashBinding>() {
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper by inject()

    override fun setBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalyticsHelper.logEvent("Application_started")
    }
}
