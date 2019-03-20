package com.khaled.omdbmoves.data.network.themoviedb

import com.khaled.omdbmoves.data.network.themoviedb.models.MoviesApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiServices {

    @GET("3/discover/movie/")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("primary_release_year") primaryReleaseYear: Int,
        @Query("sort_by") sortBy: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("include_video") includeVideo: Boolean,
        @Query("page") page: Int
    ): Single<MoviesApiResponse>
}