/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

package com.max.lib.utils.logger

import com.max.lib.BuildConfig
import timber.log.Timber

class MaxLogger : Logger {
    private val myTag = BuildConfig.lct

    fun initialize() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            d("Initializing Timber Plant")
        }
    }

    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun d(message: String) {
        Timber.tag(myTag).d(message)
    }

    override fun i(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }

    override fun i(message: String) {
        Timber.tag(myTag).i(message)
    }

    override fun e(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }

    override fun e(message: String) {
        Timber.tag(myTag).e(message)
    }

    override fun e(throwable: Throwable) {
        Timber.tag(myTag).e(throwable)
    }
}