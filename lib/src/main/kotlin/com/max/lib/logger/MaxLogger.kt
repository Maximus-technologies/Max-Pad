package com.max.lib.logger

import com.max.lib.BuildConfig
import timber.log.Timber

class MaxLogger : Logger {
    private val myTag = BuildConfig.lct

    fun initialize() {
        Timber.plant(Timber.DebugTree())
    }

    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun d(message: String) {
        Timber.tag(myTag).d(message)
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