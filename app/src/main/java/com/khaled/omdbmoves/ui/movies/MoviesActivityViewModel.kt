package com.khaled.omdbmoves.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.di.net.connectivity.NetworkConnectivityListener
import com.khaled.omdbmoves.di.net.connectivity.NetworkStatusListener
import com.khaled.omdbmoves.repository.MoviesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MoviesActivityViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    networkConnectivityListener: NetworkConnectivityListener
) : ViewModel(), NetworkStatusListener {
    private val _moviesLiveData = MutableLiveData<List<Movie>>()
    val moviesLiveData: LiveData<List<Movie>> get() = _moviesLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var moviesList = emptyList<Movie>()
    private var searchString: String = ""

    init {
        networkConnectivityListener.registerForNetworkStatusChanges(this)
    }

    fun getMovies(forceUpdate: Boolean) {
        showLoading()
        viewModelScope.launch(errorHandler) {
            moviesList = if (forceUpdate) {
                moviesRepository.getMoviesFromNetwork()
            } else {
                moviesRepository.getMoviesFromDB()
            }

            if (searchString.isNotEmpty()) {
                searchMovie(searchString)
            } else {
                _moviesLiveData.value = moviesList
            }
            hideLoading()
        }
    }

    fun searchMovie(search: String) {
        viewModelScope.launch {
            searchString = search
            delay(300)
            if (search.isNotEmpty()) {
                val queryLowerCase = search.toLowerCase(Locale.getDefault())
                _moviesLiveData.value = moviesList.filter {
                    it.title?.toLowerCase(Locale.getDefault())?.contains(queryLowerCase) ?: false
                }
            } else {
                _moviesLiveData.value = moviesList
            }
        }

    }

    override fun onNetworkStatusChanged(isConnected: Boolean) {
        if (isConnected) {
            getMovies(forceUpdate = true)
        } else {
            getMovies(forceUpdate = false)
        }
    }

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        hideLoading()
        Timber.e(throwable)
        _error.value = throwable.message
    }

    private fun hideLoading() {
        isLoading.value = false
    }

    private fun showLoading() {
        isLoading.value = true
    }
}