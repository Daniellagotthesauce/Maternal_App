package com.example.maternal_childapp

import com.google.firebase.FirebaseApp
import android.util.Log
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.maternal_childapp.ui.MorePage
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.maternal_childapp.ui.AddChild
import com.example.maternal_childapp.ui.ChangeProfile
import com.example.maternal_childapp.ui.HomePage
import com.example.maternal_childapp.ui.LandingScreen
import com.example.maternal_childapp.ui.LoginScreen
import com.example.maternal_childapp.ui.OnboardingScreen
import com.example.maternal_childapp.ui.Settings
import com.example.maternal_childapp.ui.SignUpScreen
import com.example.maternal_childapp.ui.Messages
import com.example.maternal_childapp.ui.Track
import com.example.maternal_childapp.ui.Learn


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Log.d("FirebaseStatus", "✅ Firebase initialized successfully!")
        setContent {
            MorePage()
            Settings()
            ChangeProfile()
            AddChild()
            HomePage()
            Messages()
            Track()
            Learn()
            MaterialTheme {
                Surface {
                    val nav = rememberNavController()
                    NavHost(
                        navController = nav,
                        startDestination = "landing" //the 1st screen to show
                    )
                    {
                        composable("landing") {
                            LandingScreen(
                                onLogin = { nav.navigate("login") },
                                onSignUp = { nav.navigate("signup") }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                onLoginClick = {
                                    nav.navigate("home") {
                                        popUpTo("login")
                                        { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("onboarding") {
                            OnboardingScreen(
                                userName = "Faith",
                                onContinueClick = { }
                            )
                        }

                        composable ("home") {HomePage()}
                        composable("signup") {
                            SignUpScreen(
                                onRegisterClick = { nav.navigate("onboarding") }
                            ) }

                    }
                }
            }
        }
    }
}
