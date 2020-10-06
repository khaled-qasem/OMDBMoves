package com.khaled.omdbmoves.data.network

import com.khaled.omdbmoves.data.model.MoviesApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiServices {

    @GET("3/discover/movie/")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("primary_release_year") primaryReleaseYear: Int,
        @Query("sort_by") sortBy: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("include_video") includeVideo: Boolean,
        @Query("page") page: Int
    ): MoviesApiResponse
}