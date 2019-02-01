package com.example.koltincorutines.utils

import android.util.Log

class Logger {

    companion object {
        fun e(tag: String, value: String) = Log.e(tag, value)
    }
}