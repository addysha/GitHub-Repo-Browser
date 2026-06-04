package com.example.repobrowser.data

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit interface for the public GitHub REST API. No authentication is used,
 * so requests are subject to the ~60/hour unauthenticated rate limit.
 */
interface GitHubApi {

    /** All public repositories owned by [username]. */
    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String
    ): List<RepoDto>

    /** A single repository identified by its [owner] and [repo] name. */
    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): RepoDto
}
