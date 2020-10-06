package com.khaled.omdbmoves.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khaled.omdbmoves.data.model.Movie

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movie")
    suspend fun getMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)
}