package com.khaled.omdbmoves.di

import android.content.Context
import androidx.room.Room
import com.khaled.omdbmoves.BuildConfig
import com.khaled.omdbmoves.data.database.MoviesDao
import com.khaled.omdbmoves.data.database.MoviesDatabase
import com.khaled.omdbmoves.di.context.OmdbApplication
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListenerImpl
import com.khaled.omdbmoves.data.network.MoviesApiServices
import com.khaled.omdbmoves.di.photos.PhotosManager
import com.khaled.omdbmoves.di.photos.PhotosManagerImpl
import com.khaled.omdbmoves.repository.MoviesRepository
import com.khaled.omdbmoves.repository.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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

    @Singleton
    @Provides
    fun provideMoviesApiServices(okHttpClient: OkHttpClient): MoviesApiServices {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OMDB_DOMAIN)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
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
    fun provideAppDatabase(context: Context): MoviesDatabase =
        Room.databaseBuilder(context, MoviesDatabase::class.java, "Movies.db").build()

    @Provides
    fun provideMoviesDao(db: MoviesDatabase): MoviesDao =
        db.moviesDao()

    @Provides
    @Singleton
    fun provideMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository =
        moviesRepositoryImpl
}
