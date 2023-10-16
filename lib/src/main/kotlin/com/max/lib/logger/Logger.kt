package com.max.lib.logger

interface Logger {
    fun d(tag: String, message: String)
    fun d(message: String)
    fun e(tag: String, message: String)
    fun e(message: String)
}