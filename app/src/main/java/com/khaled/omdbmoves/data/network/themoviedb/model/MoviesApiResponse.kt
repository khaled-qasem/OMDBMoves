package com.khaled.omdbmoves.data.network.themoviedb.model

import com.google.gson.annotations.SerializedName

data class MoviesApiResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<ApiMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)