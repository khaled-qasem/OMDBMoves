package com.khaled.omdbmoves.repository

import com.khaled.omdbmoves.BuildConfig
import com.khaled.omdbmoves.data.database.MoviesDao
import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.data.network.MoviesApiServices
import com.khaled.omdbmoves.data.model.MoviesApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val moviesApiServices: MoviesApiServices,
    private val moviesDao: MoviesDao
) : MoviesRepository{
    override suspend fun getMoviesFromNetwork(): List<Movie> {
        return coroutineScope {
            val firstPageDeferred = async { getMoviesPerPage(1).movies }
            val secondPageDeferred = async { getMoviesPerPage(2).movies }
            val thirdPageDeferred = async { getMoviesPerPage(3).movies }
            val fourthPageDeferred = async { getMoviesPerPage(4).movies }
            val fifthPageDeferred = async { getMoviesPerPage(5).movies }

            val movies = withContext(Dispatchers.Default) {
                mutableListOf<Movie>().apply {
                    addAll(firstPageDeferred.await())
                    addAll(secondPageDeferred.await())
                    addAll(thirdPageDeferred.await())
                    addAll(fourthPageDeferred.await())
                    addAll(fifthPageDeferred.await())
                }
            }

            insertMovies(movies)
            movies
        }
    }

    override suspend fun getMoviesFromDB(): List<Movie> = moviesDao.getMovies().sortedBy { it.title }

    private suspend fun getMoviesPerPage(page: Int): MoviesApiResponse {
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

    private suspend fun insertMovies(movies: List<Movie>) {
        moviesDao.insertMovies(movies)
    }

    companion object {
        private const val MOVIES_LANGUAGE = "en-US"
        private const val MOVIES_RELEASE_YEAR = 2018
        private const val SORTED_BY = "vote_average.desc"
        private const val INCLUDE_ADULT = false
        private const val INCLUDE_VIDEO = false
    }
}