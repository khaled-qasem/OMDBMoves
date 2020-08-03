package com.khaled.omdbmoves.di.context

import androidx.multidex.MultiDexApplication
import com.khaled.omdbmoves.BuildConfig
import com.khaled.omdbmoves.di.AppComponent
import com.khaled.omdbmoves.di.DaggerAppComponent
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener
import com.khaled.omdbmoves.di.net.connectivity.NetworkConnectivityListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber
import javax.inject.Inject

class OmdbApplication : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var applicationLifeCycleListener: ApplicationLifeCycleListener

    @Inject
    lateinit var networkConnectivityListener: NetworkConnectivityListener

    val applicationComponent: AppComponent by lazy { initializeDaggerComponent() }

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        applicationComponent.inject(this)
        registerActivityLifecycleCallbacks(applicationLifeCycleListener)
        initNetworkConnectivityListener()
        initRealm()
    }

    private fun initNetworkConnectivityListener() {
        // This's just to init networkConnectivityListener before starting an activity
        networkConnectivityListener.isConnected
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("omdb.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(config)
    }

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