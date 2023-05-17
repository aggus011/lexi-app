package com.example.lexiapp

import android.app.Application

class LexiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        /*startKoin {
            androidLogger()
            androidContext(this@LexiApp)
            modules(appModule)
        }*/
    }
}
