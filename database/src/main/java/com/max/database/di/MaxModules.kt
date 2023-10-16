package com.max.database.di

import com.max.database.MaxDatabase
import org.koin.dsl.module

var maxModules = module {
    single { MaxDatabase.getDatabase(get()) }
}