package com.movie.data.repo

import com.movie.data.Constant
import com.movie.data.network.API
import com.movie.data.network.SafeApiCall

class MovieRepo(private val api: API) : SafeApiCall {

    suspend fun getMovie(page : Int) = safeApiCall {
        api.getMovies(Constant.API_KEY,page)
    }
}