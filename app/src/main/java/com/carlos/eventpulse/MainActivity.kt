package com.carlos.eventpulse

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.carlos.eventpulse.core.navigation.AppNavigation
import com.carlos.eventpulse.core.navigation.Screen
import com.carlos.eventpulse.core.service.EventPulseWebSocketService
import com.carlos.eventpulse.core.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = sharedPreferences.getString(Constants.KEY_TOKEN, null)
        val rol = sharedPreferences.getString(Constants.KEY_USER_ROL, null)

        // Start WS service if already logged in
        if (token != null) {
            EventPulseWebSocketService.start(this, token)
        }

        val startDestination = when {
            token == null -> Screen.Login.route
            rol == "admin" -> Screen.AdminDashboard.route
            else -> Screen.StaffFeed.route
        }

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    startDestination = startDestination,
                    intent = intent
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
