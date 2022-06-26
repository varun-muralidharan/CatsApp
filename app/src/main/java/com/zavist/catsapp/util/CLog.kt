package com.zavist.catsapp.util

import android.util.Log

object CLog {

    fun d(tag: String, functionName: String, message: String?) {
        Log.d(tag, "$functionName: $message")
    }

    fun e(tag: String, functionName: String, message: String?) {
        Log.e(tag, "$functionName: $message")
    }

    fun i(tag: String, functionName: String, message: String?) {
        Log.i(tag, "$functionName: $message")
    }

    fun v(tag: String, functionName: String, message: String?) {
        Log.v(tag, "$functionName: $message")
    }
}