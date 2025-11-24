package com.example.maternal_childapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "landing",
    ) {

        composable("landing") {
            LandingScreen(
                onLogin = { navController.navigate("login") },
                onSignUp = { navController.navigate("signup") }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                onRegisterClick = {
                    navController.navigate("onboarding") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                userName = "Faith",
                onContinueClick = { navController.navigate("home") }
            )
        }

        composable("home") {
            BottomBarScreen(rootNavController = navController)
        }

        composable("growth") {
            GrowthScreenRoute(
                onBack = { navController.popBackStack() },
                onAddMeasurement = { /* later: open add-measurement screen */ }
            )
        }

    }
}

@Composable
fun BottomBar(bottomNavController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false,
            onClick = { bottomNavController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Analytics, contentDescription = "Track") },
            selected = false,
            onClick = { bottomNavController.navigate("track") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "Learn") },
            selected = false,
            onClick = { bottomNavController.navigate("learn") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Message, contentDescription = "Messages") },
            selected = false,
            onClick = { bottomNavController.navigate("messages") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dehaze, contentDescription = "More") },
            selected = false,
            onClick = { bottomNavController.navigate("more") }
        )
    }
}

@Composable
fun BottomBarScreen(rootNavController: NavHostController) {

    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(bottomNavController) }
    ) { innerPadding ->

        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("home") {
                HomePage(
                    onAddClick = { bottomNavController.navigate("addChild")},
                    onSettingsClick = {bottomNavController.navigate("settings")}
                )
            }

            composable("profile") { ChangeProfile() }
            composable("settings") { Settings() }

            composable("track") {
                Track(
                    onGrowthClick = {
                        rootNavController.navigate("growth")
                    },
                    onVaccineClick = {
                        bottomNavController.navigate("vaccine")
                    }
                )
            }

            composable("learn") { Learn() }
            composable("messages") { Messages() }
            composable("more") { MorePage() }
            composable("vaccine") { Vaccine() }
            composable("addChild") { AddChild() }
        }
    }
}
