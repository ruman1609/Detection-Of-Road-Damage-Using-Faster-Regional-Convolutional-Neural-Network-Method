package com.rudyrachman16.skripsi.core

sealed class Status<out T> {
    data class Success<T>(val data: T): Status<T>()
    object Loading: Status<Nothing>()
    data class Error(val error: Exception): Status<Nothing>()
}
