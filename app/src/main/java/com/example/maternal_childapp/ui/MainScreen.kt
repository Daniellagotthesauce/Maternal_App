package com.example.maternal_childapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
            // Landing screen
            composable("landing") {
                LandingScreen(
                    onLogin = { navController.navigate("login") },
                    onSignUp = { navController.navigate("signup") }
                )
            }

            // Login screen
            composable("login") {
                LoginScreen(
                    onLoginClick = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            // Signup screen
            composable("signup") {
                SignUpScreen(
                    onRegisterClick = {
                        navController.navigate("onboarding") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                )
            }
            //Onboarding Screen
            composable("onboarding") {
                OnboardingScreen(
                    userName = "Faith",
                    onContinueClick = { navController.navigate("home") }
                )
            }
            composable("home") { BottomBarScreen() }
            composable("track") {
                Track(
                    onVaccineClick = { navController.navigate("vaccine") }
                )
            }
            composable("vaccine") { BottomBarScreen() }
        }
    }

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false,
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Analytics, contentDescription = "Track") },
            selected = false,
            onClick = { navController.navigate("track") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "Learn") },
            selected = false,
            onClick = { navController.navigate("learn") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Message, contentDescription = "Messages") },
            selected = false,
            onClick = { navController.navigate("messages") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Dehaze, contentDescription = "More") },
            selected = false,
            onClick = { navController.navigate("more") }
        )
    }
}


@Composable
fun BottomBarScreen() {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(bottomNavController) }
    ) { innerPadding ->

        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomePage() }
            composable("profile") { ChangeProfile() }
            composable("settings") { Settings() }
            composable("track") {
                Track(
                    onVaccineClick = { bottomNavController.navigate("vaccine") }
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

