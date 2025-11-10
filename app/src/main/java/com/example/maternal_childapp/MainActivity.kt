package com.example.maternal_childapp

import com.google.firebase.FirebaseApp
import android.util.Log
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.maternal_childapp.ui.MorePage
//import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.maternal_childapp.ui.AddChild
import com.example.maternal_childapp.ui.ChangeProfile
import com.example.maternal_childapp.ui.HomePage
import com.example.maternal_childapp.ui.LandingScreen
import com.example.maternal_childapp.ui.LoginScreen
import com.example.maternal_childapp.ui.Settings
import com.example.maternal_childapp.ui.SignUpScreen
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
                        //composable("login") { AddChild() }
                        composable("login") {LoginScreen() }
                        composable("signup") { ChangeProfile() }

                    }
                }
            }
        }
    }
}
