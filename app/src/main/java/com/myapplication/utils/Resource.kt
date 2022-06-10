package com.myapplication.utils


sealed class Resource<T>(
    val status: Status,
    val data: T? = null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T? = null) : Resource<T>(Status.SUCCESS, data = data)
    class Error<T>(errorMessage: String) : Resource<T>(Status.ERROR, errorMessage = errorMessage)
    class Loading<T> : Resource<T>(Status.LOADING)
}
