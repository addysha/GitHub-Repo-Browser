package com.example.repobrowser.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A GitHub repository as returned by the REST API. Only the fields the app
 * shows are mapped; Moshi ignores the rest. JSON snake_case keys are mapped to
 * idiomatic Kotlin camelCase names via [Json].
 */
@JsonClass(generateAdapter = true)
data class RepoDto(
    @Json(name = "name") val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "description") val description: String?,
    @Json(name = "stargazers_count") val stars: Int,
    @Json(name = "forks_count") val forks: Int,
    @Json(name = "open_issues_count") val openIssues: Int,
    @Json(name = "language") val language: String?,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "html_url") val htmlUrl: String
)
