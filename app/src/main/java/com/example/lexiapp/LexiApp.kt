package com.example.lexiapp

import android.app.Application
import com.example.lexiapp.configuration.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class LexiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@LexiApp)
            modules(appModule)
        }
    }
}
