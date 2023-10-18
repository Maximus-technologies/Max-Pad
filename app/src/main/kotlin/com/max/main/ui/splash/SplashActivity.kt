package com.max.main.ui.splash

import com.max.firebase.config.RemoteConfigManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.max.firebase.analytics.FirebaseAnalyticsHelper
import com.max.firebase.messaging.MaxFCM
import com.max.lib.base.MVVMBaseActivity
import com.max.main.databinding.ActivitySplashBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : MVVMBaseActivity<ActivitySplashBinding>() {
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper by inject()
    private val vm: SplashViewModel by viewModel()
    private val remoteConfigManager : RemoteConfigManager by inject()

    override fun setBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalyticsHelper.logEvent("Application_started")
        MaxFCM.init()
        initializeRemoteConfig()

    }

    private fun initializeRemoteConfig() {
        lifecycleScope.launch {
            var result = remoteConfigManager.fetchRemoteConfig()
            if(result.isSuccess){
                var remoteConfig = result.getOrNull()
                Log.e("Result", "${remoteConfig?.key1}")
            }else{
                //Remote Json fetched Failed
            }
        }
    }
}
