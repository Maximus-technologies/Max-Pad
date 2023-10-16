package com.max.lib.logger

import timber.log.Timber

class MaxLogger : Logger {
    fun initialize() {
        Timber.plant(Timber.DebugTree())
    }

    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun d(message: String) {
        Timber.tag("MaxLog").d(message)
    }

    override fun e(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }

    override fun e(message: String) {
        Timber.tag("MaxLog").e(message)
    }
}