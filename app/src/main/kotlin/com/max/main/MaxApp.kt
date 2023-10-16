package com.max.main

import androidx.multidex.MultiDexApplication
import com.max.database.di.maxModules
import com.max.firebase.di.firebaseModules
import com.max.lib.di.libModules
import com.max.lib.logger.MaxLogger
import com.max.main.di.modules.databaseModules
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
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MaxApp)

            modules(maxModules)
            modules(libModules)
            modules(databaseModules)
            modules(firebaseModules)
        }
    }

    companion object {
        lateinit var instance: MaxApp
            private set
    }
}