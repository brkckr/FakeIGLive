package com.brkckr.fakeiglive.domain.model

sealed interface DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>
    data class Error(val throwable: Throwable? = null) : DomainResult<Nothing>
}
