package com.fittracker.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable, val message: String? = exception.message) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}
