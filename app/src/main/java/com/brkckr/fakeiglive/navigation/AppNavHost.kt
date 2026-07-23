package com.brkckr.fakeiglive.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brkckr.fakeiglive.feature.live.LiveDestination
import com.brkckr.fakeiglive.feature.live.LiveRoute
import com.brkckr.fakeiglive.feature.live.LiveViewModel
import com.brkckr.fakeiglive.feature.setup.SetupDestination
import com.brkckr.fakeiglive.feature.setup.SetupRoute
import com.brkckr.fakeiglive.feature.setup.SetupViewModel

// orchestrate type-safe navigation between features
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SetupDestination,
    ) {
        composable<SetupDestination> {
            val viewModel = hiltViewModel<SetupViewModel>()
            SetupRoute(
                viewModel = viewModel,
                onStartLive = { profile ->
                    navController.navigate(
                        LiveDestination(
                            username = profile.username,
                            profileImageUri = profile.profileImageUri,
                        ),
                    ) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<LiveDestination> {
            val viewModel = hiltViewModel<LiveViewModel>()
            LiveRoute(
                viewModel = viewModel,
                onClose = {
                    if (!navController.popBackStack()) {
                        navController.navigate(SetupDestination) {
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }
}
