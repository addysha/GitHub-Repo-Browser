package com.example.repobrowser.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repobrowser.data.RepoDto
import com.example.repobrowser.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoListScreen(
    username: String,
    onRepoClick: (RepoDto) -> Unit,
    onBack: () -> Unit,
    viewModel: RepoListViewModel = viewModel(factory = RepoListViewModel.factory())
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Kick off the load when this screen first appears for a given username.
    LaunchedEffect(username) { viewModel.load(username) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(username) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                is UiState.Loading -> CircularProgressIndicator()

                is UiState.Error -> Text(
                    text = s.message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )

                is UiState.Success -> {
                    if (s.data.isEmpty()) {
                        Text("This user has no public repositories.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(s.data) { repo ->
                                RepoRow(repo = repo, onClick = { onRepoClick(repo) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoRow(repo: RepoDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = repo.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!repo.description.isNullOrBlank()) {
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("★ ${repo.stars}", style = MaterialTheme.typography.labelMedium)
                repo.language?.let {
                    Text(it, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
