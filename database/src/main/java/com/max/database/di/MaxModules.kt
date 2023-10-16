package com.max.database.di

import com.max.database.MaxDatabase
import org.koin.dsl.module

var databaseModules = module {
    single { MaxDatabase.getDatabase(get()) }
}