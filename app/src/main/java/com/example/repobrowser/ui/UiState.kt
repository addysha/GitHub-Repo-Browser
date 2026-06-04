package com.example.repobrowser.ui

/**
 * The three states any data-driven screen can be in. Generic over the payload
 * type so both the list and detail screens can reuse it.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
