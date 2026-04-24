package com.example.vedika.core.data.util

import android.util.Log
import javax.inject.Inject

/**
 * Interface for logging to allow mocking in unit tests.
 */
interface VedikaLogger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String)
}

/**
 * Production implementation of [VedikaLogger] that uses [android.util.Log].
 */
class AndroidVedikaLogger @Inject constructor() : VedikaLogger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}
