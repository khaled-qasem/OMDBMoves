package com.khaled.omdbmoves.repository

import com.khaled.omdbmoves.data.model.Movie

interface MoviesRepository {
    suspend fun getMoviesFromNetwork(): List<Movie>
    suspend fun getMoviesFromDB(): List<Movie>
}