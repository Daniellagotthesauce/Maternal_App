package com.example.maternal_childapp

import com.google.firebase.FirebaseApp
import android.util.Log
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.maternal_childapp.ui.MainScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Log.d("FirebaseStatus", "✅ Firebase initialized successfully!")
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    MainScreen(navController)
                    }
                }
            }
        }
    }

