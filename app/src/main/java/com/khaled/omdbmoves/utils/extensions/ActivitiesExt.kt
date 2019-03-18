package com.khaled.omdbmoves.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.khaled.omdbmoves.di.viewModel.ViewModelFactoryProvider

/**
 * Returns the view model that will be used in this activity.
 *
 * Note that the view model must be declared in [ViewModelsModule] to be provided,
 * or else [IllegalArgumentException] will be thrown.
 */
inline fun <reified VM : ViewModel> FragmentActivity.viewModel(): VM {
    return ViewModelProviders.of(this, ViewModelFactoryProvider(this).viewModelFactory)
        .get(VM::class.java)
}

/**
 * Returns the view model that will be used in this Fragment.
 *
 * Note that the view model must be declared in [ViewModelsModule] to be provided,
 * or else [IllegalArgumentException] will be thrown.
 */
inline fun <reified VM : ViewModel> Fragment.viewModel(): VM {
    return ViewModelProviders.of(this, ViewModelFactoryProvider(context!!).viewModelFactory)
        .get(VM::class.java)
}

/**
 * Hide the back button in toolbar
 */
fun AppCompatActivity.hideBackArrow() = supportActionBar?.setDisplayHomeAsUpEnabled(false)

/**
 * Show the back button in toolbar
 */
fun AppCompatActivity.showBackArrow() = supportActionBar?.setDisplayHomeAsUpEnabled(true)