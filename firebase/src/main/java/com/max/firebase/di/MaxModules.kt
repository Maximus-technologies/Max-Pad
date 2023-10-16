package com.max.firebase.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.FirebaseAnalyticsKtxRegistrar
import com.max.firebase.analytics.FirebaseAnalyticsHelper
import org.koin.dsl.module

var firebaseModules = module {
    single { FirebaseAnalytics.getInstance(get()) }
    single { FirebaseAnalyticsHelper(get()) }
}