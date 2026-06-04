package com.example.repobrowser.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.repobrowser.data.RepoDto
import com.example.repobrowser.ui.UiState
import com.example.repobrowser.ui.toDisplayDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    owner: String,
    repoName: String,
    onBack: () -> Unit,
    viewModel: RepoDetailViewModel = viewModel(factory = RepoDetailViewModel.factory())
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(owner, repoName) { viewModel.load(owner, repoName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(repoName) },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
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
                is UiState.Success -> RepoDetail(s.data)
            }
        }
    }
}

@Composable
private fun RepoDetail(repo: RepoDto) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(repo.fullName, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = repo.description ?: "No description provided.",
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider()
        StatRow("Stars", repo.stars.toString())
        StatRow("Forks", repo.forks.toString())
        StatRow("Open issues", repo.openIssues.toString())
        StatRow("Language", repo.language ?: "—")
        StatRow("Last updated", repo.updatedAt.toDisplayDate())
        HorizontalDivider()
        Text(repo.htmlUrl, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
