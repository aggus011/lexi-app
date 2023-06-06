package com.example.lexiapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.lexiapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
