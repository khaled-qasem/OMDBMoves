package com.khaled.omdbmoves.di

import com.khaled.omdbmoves.ui.details.DetailsActivity
import com.khaled.omdbmoves.ui.movies.MoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MoviesActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailsActivity(): DetailsActivity
}