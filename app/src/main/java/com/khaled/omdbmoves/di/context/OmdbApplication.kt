package com.khaled.omdbmoves.di.context

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.khaled.omdbmoves.BuildConfig
import com.khaled.omdbmoves.di.AppComponent
import com.khaled.omdbmoves.di.AppInjector
import com.khaled.omdbmoves.di.DaggerAppComponent
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class OmdbApplication : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var applicationLifeCycleListener: ApplicationLifeCycleListener

    val applicationComponent: AppComponent by lazy { initializeDaggerComponent() }

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        applicationComponent.inject(this)
        registerActivityLifecycleCallbacks(applicationLifeCycleListener)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    private fun initializeDaggerComponent() =
        DaggerAppComponent.builder()
            .application(this)
            .build()

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}