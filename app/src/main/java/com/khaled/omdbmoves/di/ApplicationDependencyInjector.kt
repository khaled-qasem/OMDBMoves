package com.khaled.omdbmoves.di

import android.content.Context
import com.khaled.omdbmoves.binding.BindingAdapters
import com.khaled.omdbmoves.di.context.OmdbApplication
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListenerImpl
import com.khaled.omdbmoves.di.viewModel.ViewModelFactoryProvider

object ApplicationDependencyInjector {

    private fun getApplicationComponent(context: Context): AppComponent =
        (context.applicationContext as OmdbApplication).applicationComponent

    fun inject(context: Context, instance: ApplicationLifeCycleListenerImpl) =
        getApplicationComponent(context).inject(instance)

    fun inject(context: Context, instance: ViewModelFactoryProvider) =
        getApplicationComponent(context).inject(instance)

    fun inject(context: Context, instance: BindingAdapters) =
        getApplicationComponent(context).inject(instance)
}