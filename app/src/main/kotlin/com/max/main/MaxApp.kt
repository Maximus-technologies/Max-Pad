package com.max.main

import androidx.multidex.MultiDexApplication
import com.max.billing.di.billingModules
import com.max.database.di.databaseModules
import com.max.firebase.FireSetup
import com.max.firebase.di.firebaseModules
import com.max.lib.di.libModules
import com.max.lib.utils.logger.MaxLogger
import com.max.main.di.modules.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class MaxApp : MultiDexApplication() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        initKoin()
        MaxLogger().initialize()
        startFirebase()
    }

    private fun startFirebase() {
        FireSetup().initialize(this@MaxApp)
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MaxApp)

            modules(databaseModules)
            modules(libModules)
            modules(appModules)
            modules(firebaseModules)
            modules(billingModules)
        }
    }

    companion object {
        lateinit var instance: MaxApp
            private set
    }
}