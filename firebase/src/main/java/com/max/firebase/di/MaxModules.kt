package com.max.firebase.di

import com.max.firebase.config.RemoteConfigManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.max.firebase.analytics.FirebaseAnalyticsHelper
import org.koin.dsl.module

var firebaseModules = module {
    single { FirebaseAnalytics.getInstance(get()) }
    single { FirebaseAnalyticsHelper(get()) }
    single { Gson() }
    single { FirebaseRemoteConfig.getInstance() }
    single { RemoteConfigManager.getInstance(get()) }
}