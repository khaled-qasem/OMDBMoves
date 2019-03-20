package com.khaled.omdbmoves.di

import com.khaled.omdbmoves.ui.MoviesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MoviesActivity
}