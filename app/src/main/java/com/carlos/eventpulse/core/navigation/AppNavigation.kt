package com.carlos.eventpulse.core.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.carlos.eventpulse.core.util.Constants
import com.carlos.eventpulse.features.auth.presentation.screens.LoginScreen
import com.carlos.eventpulse.features.chat.presentation.screens.ChatScreen
import com.carlos.eventpulse.features.events.presentation.admin.screens.AdminDashboardScreen
import com.carlos.eventpulse.features.events.presentation.staff.screens.StaffFeedScreen
import com.carlos.eventpulse.features.events.presentation.shared.screens.IncidentDetailScreen
import com.carlos.eventpulse.features.events.presentation.shared.screens.TaskDetailScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    intent: Intent?
) {
    // Handle deep links from notifications
    LaunchedEffect(intent) {
        intent?.let { handleDeepLink(it, navController) }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { rol ->
                    val destination = if (rol == "admin") Screen.AdminDashboard.route
                    else Screen.StaffFeed.route
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                onNavigateToChat = { navController.navigate(Screen.Chat.route) },
                onNavigateToIncident = { id ->
                    navController.navigate(Screen.IncidentDetail.createRoute(id))
                },
                onNavigateToTask = { id ->
                    navController.navigate(Screen.TaskDetail.createRoute(id))
                }
            )
        }

        composable(Screen.StaffFeed.route) {
            StaffFeedScreen(
                onNavigateToChat = { navController.navigate(Screen.Chat.route) },
                onNavigateToIncident = { id ->
                    navController.navigate(Screen.IncidentDetail.createRoute(id))
                },
                onNavigateToTask = { id ->
                    navController.navigate(Screen.TaskDetail.createRoute(id))
                }
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(
                onNavigateToIncident = { id ->
                    navController.navigate(Screen.IncidentDetail.createRoute(id))
                },
                onNavigateToTask = { id ->
                    navController.navigate(Screen.TaskDetail.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.IncidentDetail.route,
            arguments = listOf(navArgument("incidentId") { type = NavType.StringType })
        ) { backStackEntry ->
            IncidentDetailScreen(
                incidentId = backStackEntry.arguments?.getString("incidentId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TaskDetail.route,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            TaskDetailScreen(
                taskId = backStackEntry.arguments?.getString("taskId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun handleDeepLink(intent: Intent, navController: NavHostController) {
    val type = intent.getStringExtra(Constants.DEEP_LINK_TYPE) ?: return
    val id = intent.getStringExtra(Constants.DEEP_LINK_ID)
    when (type) {
        Constants.DEEP_LINK_CHAT -> navController.navigate(Screen.Chat.route)
        Constants.DEEP_LINK_INCIDENCIA -> id?.let {
            navController.navigate(Screen.IncidentDetail.createRoute(it))
        }
        Constants.DEEP_LINK_TAREA -> id?.let {
            navController.navigate(Screen.TaskDetail.createRoute(it))
        }
    }
}
