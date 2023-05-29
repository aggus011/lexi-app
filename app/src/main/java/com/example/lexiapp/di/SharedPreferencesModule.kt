package com.example.lexiapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.lexiapp.R
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

object SharedPreferencesModule {
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            appContext.getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )
    }
}
