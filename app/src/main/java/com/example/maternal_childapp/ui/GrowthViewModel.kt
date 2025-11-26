package com.example.maternal_childapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GrowthRecord(
    val title: String,
    val date: String,
    val weightKg: Double,
    val heightCm: Double
)

data class ChildOption(
    val id: String,
    val name: String,
    val dob: String? = null
)

data class GrowthUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val childName: String = "Your baby",
    val dateOfBirth: String? = null,
    val childAgeLabel: String = "",
    val latestWeightKg: Double? = null,
    val latestHeightCm: Double? = null,
    val history: List<GrowthRecord> = emptyList(),
    val children: List<ChildOption> = emptyList(),
    val selectedChildId: String? = null
)

class GrowthViewModel : ViewModel() {

    var uiState by mutableStateOf(GrowthUiState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    init {
        loadChildren()
    }

    private fun loadChildren() {
        val user = auth.currentUser ?: run {
            uiState = uiState.copy(
                isLoading = false,
                error = "User not logged in"
            )
            return
        }

        db.collection("users")
            .document(user.uid)
            .collection("children")
            .get()
            .addOnSuccessListener { snapshot ->
                val childOptions = snapshot.documents.map { doc ->
                    val id = doc.id
                    val first = doc.getString("firstName") ?: ""
                    val last = doc.getString("lastName") ?: ""
                    val name = "$first $last".trim().ifBlank { "Baby" }
                    val dob = doc.getString("dob")
                    val dobFormatted = doc.getString("dob")

                    ChildOption(id = id, name = name, dob = dobFormatted)
                }

                val firstChild = childOptions.firstOrNull()

                uiState = uiState.copy(
                    children = childOptions,
                    selectedChildId = firstChild?.id,
                    childName = firstChild?.name ?: "Your baby",
                    isLoading = true,
                    error = null,
                    history = emptyList(),
                    latestHeightCm = null,
                    latestWeightKg = null,
                    dateOfBirth = firstChild?.dob
                )


                firstChild?.id?.let { loadGrowthDataForChild(it) }
            }
            .addOnFailureListener { e ->
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Failed to load children: ${e.message}"
                )
            }
    }


    private fun loadGrowthDataForChild(childId: String) {
        val user = auth.currentUser ?: return

        uiState = uiState.copy(
            isLoading = true,
            error = null,
            history = emptyList(),
            latestHeightCm = null,
            latestWeightKg = null
        )

        val childDob = uiState.children.find { it.id == childId }?.dob ?: "Not set"

        db.collection("users")
            .document(user.uid)
            .collection("growthLogs")
            .whereEqualTo("childId", childId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = null,
                        latestWeightKg = null,
                        latestHeightCm = null,
                        history = emptyList()
                    )
                    return@addSnapshotListener
                }

                val records = snapshot.documents.mapNotNull { doc ->
                    val weight = doc.getDouble("weightKg")
                    val height = doc.getDouble("heightCm")
                    val timestamp = doc.getTimestamp("date")
                    if (weight == null || height == null || timestamp == null) return@mapNotNull null

                    GrowthRecord(
                        title = doc.getString("title") ?: "Checkup",
                        date = childDob,
                        weightKg = weight,
                        heightCm = height
                    )
                }

                val latest = records.firstOrNull()

                uiState = uiState.copy(
                    isLoading = false,
                    error = null,
                    latestWeightKg = latest?.weightKg,
                    latestHeightCm = latest?.heightCm,
                    history = records
                )
            }
    }

    fun onChildSelected(childId: String) {
        val child = uiState.children.find { it.id == childId }
        val name = child?.name ?: "Your baby"

        uiState = uiState.copy(
            selectedChildId = childId,
            childName = name,
            dateOfBirth = child?.dob
        )

        loadGrowthDataForChild(childId)
    }
}

