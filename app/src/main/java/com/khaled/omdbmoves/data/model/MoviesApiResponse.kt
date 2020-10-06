package com.khaled.omdbmoves.data.model

import com.google.gson.annotations.SerializedName
import com.khaled.omdbmoves.data.model.Movie

data class MoviesApiResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)