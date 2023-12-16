package com.example.currencyconverter.api

sealed class NetworkResult<T>(
    val data: T?,
    val message: String?
) {
    class Success<T>(
        data: T
    ) : NetworkResult<T>(
        data = data,
        message = null
    )

    class Failure<T>(
        message: String
    ) : NetworkResult<T>(
        data = null,
        message = message
    )
}