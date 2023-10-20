package com.max.lib.utils.logger

interface Logger {
    fun d(tag: String, message: String)
    fun d(message: String)
    fun e(tag: String, message: String)
    fun e(message: String)
    fun e(throwable: Throwable)
}