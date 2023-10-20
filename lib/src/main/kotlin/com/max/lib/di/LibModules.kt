package com.max.lib.di

import com.max.lib.data.local.MaxLocal
import com.max.lib.utils.logger.MaxLogger
import org.koin.dsl.module

val libModules = module {
    single { MaxLocal(get()) }
    single { MaxLogger() }
}