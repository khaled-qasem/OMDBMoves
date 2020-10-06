package com.khaled.omdbmoves.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khaled.omdbmoves.data.model.Movie


@Database(entities = [Movie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase(){

    abstract fun moviesDao(): MoviesDao
}