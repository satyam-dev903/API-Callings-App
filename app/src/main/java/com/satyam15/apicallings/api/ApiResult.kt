package com.satyam15.apicallings.api

sealed class ApiResult<out T> {
    data class Success<T>(val data:T):ApiResult<T>()
    data class Error(val msg:String):ApiResult<Nothing>()
    object Loading:ApiResult<Nothing>()
}