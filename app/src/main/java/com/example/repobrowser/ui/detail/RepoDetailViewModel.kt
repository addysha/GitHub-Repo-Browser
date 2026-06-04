package com.example.repobrowser.ui.detail

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
 * Loads a single repository for the detail screen via the
 * `GET /repos/{owner}/{repo}` endpoint.
 */
class RepoDetailViewModel(
    private val repository: UserRepository = GitHubUserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<RepoDto>>(UiState.Loading)
    val state: StateFlow<UiState<RepoDto>> = _state.asStateFlow()

    fun load(owner: String, name: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(repository.getRepo(owner, name))
            } catch (e: HttpException) {
                UiState.Error("Couldn't load repository (${e.code()}).")
            } catch (e: IOException) {
                UiState.Error("Network error. Check your connection and retry.")
            }
        }
    }

    companion object {
        fun factory(
            repository: UserRepository = GitHubUserRepository()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                RepoDetailViewModel(repository) as T
        }
    }
}
