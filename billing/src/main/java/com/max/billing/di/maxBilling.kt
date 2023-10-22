package com.max.billing.di

import com.max.billing.provider.InAppProvider
import com.max.billing.provider.SubscriptionProvider
import org.koin.dsl.module

var billingModules = module {
    single { InAppProvider() }
    single { SubscriptionProvider() }
}