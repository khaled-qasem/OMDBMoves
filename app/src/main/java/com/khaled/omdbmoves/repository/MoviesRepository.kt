package com.khaled.omdbmoves.repository

import android.annotation.SuppressLint
import com.khaled.omdbmoves.BuildConfig
import com.khaled.omdbmoves.data.database.model.LocalMovie
import com.khaled.omdbmoves.data.network.themoviedb.MoviesApiServices
import com.khaled.omdbmoves.data.network.themoviedb.models.Movie
import com.khaled.omdbmoves.data.network.themoviedb.models.MoviesApiResponse
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function5
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

@Suppress("SENSELESS_COMPARISON")
class MoviesRepository @Inject constructor(
    private val moviesApiServices: MoviesApiServices,
    private val realm: Realm
) {
    fun getMoviesFromNetwork(): Single<List<Movie>> =
        Single.zip<MoviesApiResponse, MoviesApiResponse, MoviesApiResponse, MoviesApiResponse, MoviesApiResponse, List<Movie>>(
            getMoviesPerPage(1),
            getMoviesPerPage(2),
            getMoviesPerPage(3),
            getMoviesPerPage(4),
            getMoviesPerPage(5),
            Function5 { response1, response2, response3, response4, response5 ->
                val moviesList = mutableListOf<Movie>()
                if (response1 != null) {
                    moviesList.addAll(response1.movies)
                }
                if (response2 != null) {
                    moviesList.addAll(response2.movies)
                }
                if (response3 != null) {
                    moviesList.addAll(response3.movies)
                }
                if (response4 != null) {
                    moviesList.addAll(response4.movies)
                }
                if (response5 != null) {
                    moviesList.addAll(response5.movies)
                }
                return@Function5 moviesList.distinct()
            }
        ).observeOn(AndroidSchedulers.mainThread())
            .map {
                writeOnDB(it)
                it
            }.observeOn(Schedulers.io())

    @SuppressLint("CheckResult")
    fun getMoviesFromDB(): Flowable<List<LocalMovie>> = realm.where(LocalMovie::class.java)
        .sort("title")
        .findAllAsync()
        .asFlowable()
        .filter { movie -> movie.isLoaded }
        .map { realm.copyFromRealm(it) }

    private fun getMoviesPerPage(page: Int): Single<MoviesApiResponse> {
        return moviesApiServices.getTopRatedMovies(
            BuildConfig.API,
            MOVIES_LANGUAGE,
            MOVIES_RELEASE_YEAR,
            SORTED_BY,
            INCLUDE_ADULT,
            INCLUDE_VIDEO,
            page
        )
    }

    private fun writeOnDB(movies: List<Movie>) {
        realm.executeTransaction {
            movies.forEach {
                realm.insertOrUpdate(LocalMovie(it.id, it.title, it.releaseDate))
            }
        }
    }

    companion object {
        private const val MOVIES_LANGUAGE = "en-US"
        private const val MOVIES_RELEASE_YEAR = 2018
        private const val SORTED_BY = "vote_average.desc"
        private const val INCLUDE_ADULT = false
        private const val INCLUDE_VIDEO = false
    }
}