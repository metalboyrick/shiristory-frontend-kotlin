package com.example.shiristory

import android.app.Application
import android.content.Context

class Shiristory : Application() {

    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    fun getAppContext(): Context? {
        return context
    }
}
