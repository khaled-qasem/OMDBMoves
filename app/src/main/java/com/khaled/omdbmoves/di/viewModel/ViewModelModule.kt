package com.khaled.omdbmoves.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khaled.omdbmoves.ui.movies.MoviesActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MoviesActivityViewModel::class)
    abstract fun bindMainActivityViewModel(moviesActivityViewModel: MoviesActivityViewModel): ViewModel
}
