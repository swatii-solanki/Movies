package com.movie.data.network

import com.movie.data.response.MMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET("movie/popular")
    suspend fun getMovies(@Query("api_key") api_key: String, @Query("page") page: Int): MMovieResponse
}