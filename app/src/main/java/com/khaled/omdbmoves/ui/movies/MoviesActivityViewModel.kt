package com.khaled.omdbmoves.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.di.net.connectivity.NetworkConnectivityListener
import com.khaled.omdbmoves.di.net.connectivity.NetworkStatusListener
import com.khaled.omdbmoves.repository.MoviesRepository
import com.khaled.omdbmoves.utils.viewmodel.DisposableViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesActivityViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    networkConnectivityListener: NetworkConnectivityListener
) : DisposableViewModel(), NetworkStatusListener {
    private val _moviesLiveData = MutableLiveData<List<Movie>>()
    val moviesLiveData: LiveData<List<Movie>> get() = _moviesLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val searchSubject = PublishSubject.create<String>()
    private var moviesList = emptyList<Movie>()
    private var searchString: String = ""

    init {
        addSearchSubjectDisposable()
        networkConnectivityListener.registerForNetworkStatusChanges(this)
        if (networkConnectivityListener.isConnected.value?.peekContent() == true) {
            getMovies(true)
        } else {
            getMovies(false)
        }
    }

    fun getMovies(forceUpdate: Boolean) {
        showLoading()
        if (forceUpdate) {
            addDisposable(
                moviesRepository.getMoviesFromNetwork()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        moviesList = it.orEmpty()
                        if (searchString.isNotEmpty()) {
                            searchMovie(searchString)
                        } else {
                            _moviesLiveData.value = it
                        }
                        hideLoading()
                    }, {
                        Timber.e(it)
                        _error.value = it.message
                        hideLoading()
                    })
            )
        } else {
            addDisposable(
                moviesRepository.getMoviesFromDB()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        moviesList = it.orEmpty()
                        if (searchString.isNotEmpty()) {
                            searchMovie(searchString)
                        } else {
                            _moviesLiveData.value = it
                        }
                        hideLoading()
                    }, {
                        Timber.e(it)
                        _error.value = it.message
                        hideLoading()
                    })
            )
        }
    }

    private fun hideLoading() {
        isLoading.value = false
    }

    private fun showLoading() {
        isLoading.value = true
    }

    fun searchMovie(search: String) {
        searchString = search
        searchSubject.onNext(search)
    }

    private fun addSearchSubjectDisposable() {
        addDisposable(searchSubject.debounce(300, TimeUnit.MILLISECONDS)
            .map { query ->
                if (query.isNotEmpty()) {
                    val queryLowerCase = query.toLowerCase()
                    moviesList.filter {
                        it.title.toLowerCase().contains(queryLowerCase)
                    }
                } else {
                    moviesList
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy<List<Movie>> { list ->
                _moviesLiveData.value = list
            })
    }

    override fun onCleared() {
        moviesRepository.closeRealm()
        super.onCleared()
    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        if (isConnected) {
            addDisposable(
                moviesRepository.getMoviesFromNetwork()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }
}