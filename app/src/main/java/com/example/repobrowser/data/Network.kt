package com.example.repobrowser.data

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Builds and holds the singletons for talking to the GitHub API. Kept as a
 * plain object to avoid a DI framework on this pass (see CLAUDE.md).
 */
object Network {

    private const val BASE_URL = "https://api.github.com/"

    // Codegen-generated adapters (@JsonClass) are registered automatically,
    // so no reflection-based factory is needed here.
    private val moshi: Moshi = Moshi.Builder().build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val gitHubApi: GitHubApi = retrofit.create(GitHubApi::class.java)
}
