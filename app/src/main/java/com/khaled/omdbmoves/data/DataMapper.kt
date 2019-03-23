package com.khaled.omdbmoves.data

import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.data.database.model.Movie as DbMovie
import com.khaled.omdbmoves.data.network.themoviedb.model.ApiMovie

/**
 * Util class to map models
 */
object DataMapper {

    fun convertMovieFromDb(dbMovie: DbMovie) =
        Movie(
            dbMovie.id,
            dbMovie.title,
            dbMovie.adult,
            dbMovie.backdropPath,
            dbMovie.originalLanguage,
            dbMovie.originalTitle,
            dbMovie.overview,
            dbMovie.popularity,
            dbMovie.posterPath,
            dbMovie.releaseDate,
            dbMovie.video,
            dbMovie.voteAverage,
            dbMovie.voteCount
        )

    fun covertMovieToDb(movie: Movie) =
        DbMovie(
            movie.id,
            movie.title,
            movie.adult,
            movie.backdropPath,
            movie.originalLanguage,
            movie.originalTitle,
            movie.overview,
            movie.popularity,
            movie.posterPath,
            movie.releaseDate,
            movie.video,
            movie.voteAverage,
            movie.voteCount
        )

    fun convertMovieFromApi(apiMovie: ApiMovie) =
        Movie(
            apiMovie.id,
            apiMovie.title,
            apiMovie.adult,
            apiMovie.backdropPath,
            apiMovie.originalLanguage,
            apiMovie.originalTitle,
            apiMovie.overview,
            apiMovie.popularity,
            apiMovie.posterPath,
            apiMovie.releaseDate,
            apiMovie.video,
            apiMovie.voteAverage,
            apiMovie.voteCount
        )
}