package com.khaled.omdbmoves.di.viewModel

import android.content.Context
import com.khaled.omdbmoves.di.ApplicationDependencyInjector
import javax.inject.Inject

class ViewModelFactoryProvider(context: Context) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    init {
        ApplicationDependencyInjector.inject(context, this)
    }
}