package com.example.repobrowser

import com.example.repobrowser.data.RepoDto
import com.example.repobrowser.data.UserRepository
import com.example.repobrowser.ui.UiState
import com.example.repobrowser.ui.list.RepoListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RepoListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // viewModelScope dispatches on Dispatchers.Main; swap in a test one.
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun repo(name: String) = RepoDto(
        name = name,
        fullName = "octocat/$name",
        description = "desc",
        stars = 1,
        forks = 0,
        openIssues = 0,
        language = "Kotlin",
        updatedAt = "2024-12-01T00:00:00Z",
        htmlUrl = "https://github.com/octocat/$name"
    )

    @Test
    fun `load emits Success with the repository list`() = runTest(dispatcher) {
        val expected = listOf(repo("alpha"), repo("beta"))
        val viewModel = RepoListViewModel(FakeRepository(result = expected))

        viewModel.load("octocat")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("expected Success but was $state", state is UiState.Success)
        assertEquals(expected, (state as UiState.Success).data)
    }

    @Test
    fun `load emits Error when the repository throws`() = runTest(dispatcher) {
        val viewModel = RepoListViewModel(FakeRepository(error = IOException("boom")))

        viewModel.load("octocat")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue("expected Error but was $state", state is UiState.Error)
    }

    /** A hand-written fake: returns [result] or throws [error]. No network. */
    private class FakeRepository(
        private val result: List<RepoDto> = emptyList(),
        private val error: Throwable? = null
    ) : UserRepository {
        override suspend fun getRepos(username: String): List<RepoDto> {
            error?.let { throw it }
            return result
        }

        override suspend fun getRepo(owner: String, name: String): RepoDto =
            error?.let { throw it } ?: result.first()
    }
}
