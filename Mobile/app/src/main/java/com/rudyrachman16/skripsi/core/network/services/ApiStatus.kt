package com.rudyrachman16.skripsi.core.network.services

sealed class ApiStatus<out T> {
    data class Success<out T>(val data: T): ApiStatus<T>()
    data class Failed(val error: Exception): ApiStatus<Nothing>()
    object Empty: ApiStatus<Nothing>()
}
