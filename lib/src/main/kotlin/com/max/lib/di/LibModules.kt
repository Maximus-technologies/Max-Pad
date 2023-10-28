/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

package com.max.lib.di

import com.max.lib.data.local.MaxLocal
import com.max.lib.utils.logger.MaxLogger
import org.koin.dsl.module

val libModules = module {
    single { MaxLogger() }
    single { MaxLocal(get()) }
}