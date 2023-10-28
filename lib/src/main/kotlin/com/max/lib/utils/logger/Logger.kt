/*
 * Copyright (c) Maximus Technologies - 2023.
 * All Rights Reserved and Copy only allowed if given reference
 */

package com.max.lib.utils.logger

interface Logger {
    fun d(tag: String, message: String)
    fun d(message: String)
    fun i(tag: String, message: String)
    fun i(message: String)
    fun e(tag: String, message: String)
    fun e(message: String)
    fun e(throwable: Throwable)
}