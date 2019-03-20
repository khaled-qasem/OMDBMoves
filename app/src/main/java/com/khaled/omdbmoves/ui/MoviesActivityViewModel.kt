package com.khaled.omdbmoves.ui

import androidx.lifecycle.MutableLiveData
import com.khaled.omdbmoves.data.network.themoviedb.models.Movie
import com.khaled.omdbmoves.repository.MoviesRepository
import com.khaled.omdbmoves.utils.viewmodel.DisposableViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesActivityViewModel @Inject constructor(private val moviesRepository: MoviesRepository) :
    DisposableViewModel() {

    val moviesLiveData = MutableLiveData<List<Movie>>()
    val isLoading = MutableLiveData<Boolean>()
    private val searchSubject = PublishSubject.create<String>()
    private var moviesList = emptyList<Movie>()
    private var searchString: String = ""


    init {
        addSearchSubjectDisposable()
        getMovies(false)
    }

    fun getMovies(forceUpdate: Boolean) {
        showLoading()
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
                    hideLoading()
                })
        )
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
                    }.distinct()
                } else {
                    moviesList.distinct()
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy<List<Movie>> { list ->
                moviesLiveData.value = list
            })
    }

}