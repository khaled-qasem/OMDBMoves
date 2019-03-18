package com.khaled.omdbmoves

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class OmdbApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}