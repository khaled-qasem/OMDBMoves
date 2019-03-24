package com.khaled.omdbmoves.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.khaled.omdbmoves.di.viewModel.ViewModelFactoryProvider
import com.khaled.omdbmoves.di.viewModel.ViewModelModule

/**
 * Returns the view model that will be used in this activity.
 *
 * Note that the view model must be declared in [ViewModelModule] to be provided,
 * or else [IllegalArgumentException] will be thrown.
 */
inline fun <reified VM : ViewModel> AppCompatActivity.viewModel(): VM {
    return ViewModelProviders.of(this, ViewModelFactoryProvider(this).viewModelFactory)
        .get(VM::class.java)
}

/**
 * Returns the view model that will be used in this Fragment.
 *
 * Note that the view model must be declared in [ViewModelModule] to be provided,
 * or else [IllegalArgumentException] will be thrown.
 */
inline fun <reified VM : ViewModel> Fragment.viewModel(): VM {
    return ViewModelProviders.of(this, ViewModelFactoryProvider(context!!).viewModelFactory)
        .get(VM::class.java)
}