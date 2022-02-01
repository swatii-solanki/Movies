package com.movie.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException


interface SafeApiCall {
    // high order function
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke()) // call the function
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main) {
                    when (throwable) {
                        is HttpException -> {
                            Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                        }
                        is SocketTimeoutException -> {
                            Resource.Failure(false, null, null)
                        }
                        else -> {
                            Resource.Failure(true, null, null)
                        }
                    }
                }
            }
        }
    }
}