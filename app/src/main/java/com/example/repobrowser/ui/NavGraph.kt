package com.example.repobrowser.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.repobrowser.ui.detail.RepoDetailScreen
import com.example.repobrowser.ui.list.RepoListScreen
import com.example.repobrowser.ui.search.SearchScreen

private object Routes {
    const val SEARCH = "search"
    const val LIST = "list/{username}"
    const val DETAIL = "detail/{owner}/{repo}"

    fun list(username: String) = "list/$username"
    fun detail(owner: String, repo: String) = "detail/$owner/$repo"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SEARCH) {
        composable(Routes.SEARCH) {
            SearchScreen(
                onSearch = { username -> navController.navigate(Routes.list(username)) }
            )
        }

        composable(
            route = Routes.LIST,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username").orEmpty()
            RepoListScreen(
                username = username,
                onRepoClick = { repo ->
                    val owner = repo.fullName.substringBefore('/')
                    navController.navigate(Routes.detail(owner, repo.name))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner").orEmpty()
            val repo = backStackEntry.arguments?.getString("repo").orEmpty()
            RepoDetailScreen(
                owner = owner,
                repoName = repo,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
