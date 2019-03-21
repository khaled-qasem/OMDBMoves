package com.khaled.omdbmoves.data

import com.khaled.omdbmoves.data.model.Movie
import com.khaled.omdbmoves.data.database.model.Movie as DbMovie
import com.khaled.omdbmoves.data.network.themoviedb.model.ApiMovie


object DataMapper {

    fun convertMovieFromDb(dbMovie: DbMovie) =
        Movie(dbMovie.id, dbMovie.posterPath, dbMovie.releaseDate, dbMovie.title)

    fun covertMovieToDb(movie: Movie) =
        DbMovie(movie.id, movie.title, movie.releaseDate, movie.posterPath)

    fun convertMovieFromApi(apiMovie: ApiMovie) =
        Movie(apiMovie.id, apiMovie.posterPath, apiMovie.releaseDate, apiMovie.title)
}