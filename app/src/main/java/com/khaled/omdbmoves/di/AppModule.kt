package com.khaled.omdbmoves.di

import android.content.Context
import com.khaled.omdbmoves.BuildConfig
import com.khaled.omdbmoves.di.context.OmdbApplication
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListenerImpl
import com.khaled.omdbmoves.di.net.connectivity.ConnectivityListener
import com.khaled.omdbmoves.di.net.connectivity.ConnectivityListenerImpl
import com.khaled.omdbmoves.data.network.themoviedb.MoviesApiServices
import com.khaled.omdbmoves.di.photos.PhotosManager
import com.khaled.omdbmoves.di.photos.PhotosManagerImpl
import dagger.Module
import dagger.Provides
import io.realm.Realm
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideMoviesApiServices(okHttpClient: OkHttpClient): MoviesApiServices {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OMDB_DOMAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MoviesApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().addInterceptor(logging)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun providePhotoManager(photosManagerImpl: PhotosManagerImpl): PhotosManager = photosManagerImpl

    @Provides
    @Singleton
    fun provideRealm(): Realm = Realm.getDefaultInstance()
}
