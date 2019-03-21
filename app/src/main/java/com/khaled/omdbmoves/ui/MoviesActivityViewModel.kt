package com.khaled.omdbmoves.ui

import androidx.lifecycle.MutableLiveData
import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.di.net.connectivity.ConnectivityListener
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
    connectivityListener: ConnectivityListener
) : DisposableViewModel(), ConnectivityListener.ConnectivityChangeListener {
    val moviesLiveData = MutableLiveData<List<Movie>>()
    val isLoading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    private val searchSubject = PublishSubject.create<String>()
    private var moviesList = emptyList<Movie>()
    private var searchString: String = ""

    init {
        addSearchSubjectDisposable()
        connectivityListener.registerForConnectivityChange(this)
        if (connectivityListener.isConnected) {
            getMovies(true)
        } else {
            getMovies(false)
        }
    }

    override fun onConnectivityChanged(isConnected: Boolean) {
        if (isConnected) {
            addDisposable(moviesRepository.getMoviesFromNetwork().subscribe())
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
                            moviesLiveData.value = it
                        }
                        hideLoading()
                    }, {
                        Timber.e(it)
                        error.value = it.message
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
                            moviesLiveData.value = it
                        }
                        hideLoading()
                    }, {
                        Timber.e(it)
                        error.value = it.message
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
                moviesLiveData.value = list
            })
    }
}