package com.max.main.di.modules

import com.max.main.ui.splash.SplashViewModel
import org.koin.dsl.module

val appModules = module {
    single { SplashViewModel() }
}