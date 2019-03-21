package com.khaled.omdbmoves.data.model

data class Movie(
    val id: Long,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String
)