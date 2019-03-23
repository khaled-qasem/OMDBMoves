package com.khaled.omdbmoves.utils.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.realm.Realm


/**
 * Base class for the ViewModels that want to use RxJava [Disposable]s, to eliminate the boilerplate code.
 */
abstract class DisposableViewModel(private val realm: Realm? = null) : ViewModel() {

    /**
     *  used to store all [Disposable] objects inside the viewModel
     */
    private val compositeDisposable = CompositeDisposable()

    /**
     *  clear all [Disposable]s when the [ViewModel] get destroyed, to prevent a memory leaks
     */
    override fun onCleared() {
        clearDisposables()
        realm?.close()
        super.onCleared()
    }

    /**
     *  add [Disposable] to compositeDisposable
     */
    fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    /**
     *  clears all [Disposable]s inside the compositeDisposable
     */
    private fun clearDisposables() = compositeDisposable.dispose()
}

