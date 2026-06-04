package com.example.repobrowser.data

/**
 * Source of repository data for the UI layer. Declared as an interface so the
 * ViewModels can be unit-tested against a fake without touching the network.
 */
interface UserRepository {

    /** Public repos for [username]. Throws on network/HTTP failure. */
    suspend fun getRepos(username: String): List<RepoDto>

    /** A single repo by [owner]/[name]. Throws on network/HTTP failure. */
    suspend fun getRepo(owner: String, name: String): RepoDto
}

/** Real implementation backed by the Retrofit [GitHubApi]. */
class GitHubUserRepository(
    private val api: GitHubApi = Network.gitHubApi
) : UserRepository {

    override suspend fun getRepos(username: String): List<RepoDto> =
        api.getUserRepos(username)

    override suspend fun getRepo(owner: String, name: String): RepoDto =
        api.getRepo(owner, name)
}
