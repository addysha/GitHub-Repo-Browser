package com.example.repobrowser.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.repobrowser.data.GitHubUserRepository
import com.example.repobrowser.data.RepoDto
import com.example.repobrowser.data.UserRepository
import com.example.repobrowser.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * Holds the state for the repo list screen. Exposes a [StateFlow] of
 * [UiState] that flips Loading -> Success/Error when [load] runs.
 */
class RepoListViewModel(
    private val repository: UserRepository = GitHubUserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<RepoDto>>>(UiState.Loading)
    val state: StateFlow<UiState<List<RepoDto>>> = _state.asStateFlow()

    fun load(username: String) {
        val trimmed = username.trim()
        if (trimmed.isEmpty()) {
            _state.value = UiState.Error("Please enter a username.")
            return
        }
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(repository.getRepos(trimmed))
            } catch (e: HttpException) {
                UiState.Error(
                    if (e.code() == 404) "No GitHub user named \"$trimmed\"."
                    else "GitHub request failed (${e.code()}). Try again later."
                )
            } catch (e: IOException) {
                UiState.Error("Network error. Check your connection and retry.")
            }
        }
    }

    /** Lets the screen create this ViewModel with a custom repository. */
    companion object {
        fun factory(
            repository: UserRepository = GitHubUserRepository()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                RepoListViewModel(repository) as T
        }
    }
}
