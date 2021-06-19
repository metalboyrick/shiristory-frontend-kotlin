package com.example.shiristory

import android.app.Application

class Shiristory : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: Shiristory
            private set
    }
}
