package com.example.maternal_childapp.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

data class ProfileUIState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userName: String = "User",
    val userDob: String = "Not set",
    val userEmail: String = "Not set",
    val numberOfChildren: Int = 0,
    val phone: String = "Not set"
)

class ProfileViewModel : ViewModel() {

    var uiState by mutableStateOf(ProfileUIState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user == null) {
            uiState = uiState.copy(
                isLoading = false,
                error = "User not logged in"
            )
            Log.d("ProfileViewModel", "No logged-in user found.")
            return
        }

        Log.d("ProfileViewModel", "Logged-in user UID: ${user.uid}")

        uiState = uiState.copy(isLoading = true, error = null)

        // Fetch the user's document first
        db.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val firstName = document.getString("firstName") ?: "User"
                    val dob = document.getString("dob") ?: "Not set"
                    val email = document.getString("email") ?: user.email ?: "Not set"
                    val phone = document.getString("phone") ?: "Not set"

                    Log.d("ProfileViewModel", "Loaded user info: $firstName, $dob, $email, $phone")

                    uiState = uiState.copy(
                        userName = firstName,
                        userDob = dob,
                        userEmail = email,
                        phone = phone,
                        isLoading = false
                    )

                    // Now load children count
                    loadChildrenCount(user.uid)

                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "User profile not found"
                    )
                    Log.d("ProfileViewModel", "User document does not exist.")
                }
            }
            .addOnFailureListener { e ->
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Failed to load profile: ${e.message}"
                )
                Log.e("ProfileViewModel", "Error fetching user document", e)
            }
    }

    private fun loadChildrenCount(userId: String) {
        db.collection("users")
            .document(userId)
            .collection("children")
            .get()
            .addOnSuccessListener { snapshot ->
                uiState = uiState.copy(
                    numberOfChildren = snapshot.size()
                )
                Log.d("ProfileViewModel", "Number of children: ${snapshot.size()}")
            }
            .addOnFailureListener { e ->
                uiState = uiState.copy(
                    error = "Failed to load children count: ${e.message}"
                )
                Log.e("ProfileViewModel", "Error fetching children", e)
            }
    }

    fun refresh() {
        Log.d("ProfileViewModel", "Refreshing profile data...")
        loadUserProfile()
    }
}
