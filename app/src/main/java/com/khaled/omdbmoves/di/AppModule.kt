package com.khaled.omdbmoves.di

import android.content.Context
import com.khaled.omdbmoves.di.context.OmdbApplication
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListenerImpl
import com.khaled.omdbmoves.di.net.connectivity.ConnectivityListener
import com.khaled.omdbmoves.di.net.connectivity.ConnectivityListenerImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(omdbApplication: OmdbApplication): Context =
        omdbApplication

    @Provides
    @Singleton
    fun provideActivityLifeCycleListener(): ApplicationLifeCycleListener =
        ApplicationLifeCycleListenerImpl()

    @Provides
    @Singleton
    fun provideConnectivityListener(connectivityListenerImpl: ConnectivityListenerImpl): ConnectivityListener =
        connectivityListenerImpl

//    @Singleton
//    @Provides
//    fun provideWakeCapService(okHttpClient: OkHttpClient): ApiServices {
//        return Retrofit.Builder()
//            .baseUrl(BuildConfig.OMDB_DOMAIN)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//            .create(ApiServices::class.java)
//    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        return httpClient.build()
    }
}
