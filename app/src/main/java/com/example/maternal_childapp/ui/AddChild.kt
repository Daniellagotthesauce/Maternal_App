package com.example.maternal_childapp.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.text.KeyboardOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun AddChild() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth().height(60.dp),
            ) {
                Text(
                    text = "Add Child",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(15.dp)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            ChildForm()
        }
    }
}

fun saveChild(
    firstName: String,
    lastName: String,
    dob: String,
    birthWeight: Double,
    birthLength: Double,
    gender: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    // Validation
    if (firstName.isBlank()) {
        onError("First name is required")
        return
    }
    if (lastName.isBlank()) {
        onError("Last name is required")
        return
    }
    if (dob.isBlank()) {
        onError("Date of birth is required")
        return
    }
    if (birthWeight <= 0.0) {
        onError("Valid birth weight is required")
        return
    }
    if (birthLength <= 0.0) {
        onError("Valid birth length is required")
        return
    }
    if (gender.isBlank()) {
        onError("Gender is required")
        return
    }

    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()

    val motherId = auth.currentUser?.uid
    if (motherId == null) {
        Log.e("SAVE_CHILD", "User not logged in")
        onError("User not logged in")
        return
    }


    val childData = hashMapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "dob" to dob,
        "birthWeight" to birthWeight,
        "birthLength" to birthLength,
        "gender" to gender,
        "motherId" to motherId,
        "createdAt" to FieldValue.serverTimestamp()
    )

    db.collection("users")
        .document(motherId)
        .collection("children")
        .add(childData)
        .addOnSuccessListener {
            Log.d("SAVE_CHILD", "Child added successfully")
            db.collection("users")
                .document(motherId)
                .collection("children")
                .add(childData)
                .addOnSuccessListener { childRef ->

                    val childId = childRef.id

                    val weightKg = birthWeight * 0.453592
                    val heightCm = birthLength * 2.54

                    val growthData = hashMapOf(
                        "title" to "Birth",
                        "date" to FieldValue.serverTimestamp(),
                        "weightKg" to weightKg,
                        "heightCm" to heightCm,
                        "childId" to childId
                    )

                    db.collection("users")
                        .document(motherId)
                        .collection("growthLogs")
                        .add(growthData)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onError("Child saved but growth log failed: ${e.message}")
                        }
                }
                .addOnFailureListener { e ->
                    onError("Failed to save child: ${e.message}")
                }

            onSuccess()
        }
        .addOnFailureListener { exception ->
            Log.e("SAVE_CHILD", "Error adding child", exception)
            onError("Failed to save: ${exception.message}")
        }
}

@Composable
fun ChildForm() {
    var babyFirstName by remember { mutableStateOf("") }
    var babyLastName by remember { mutableStateOf("") }
    var babyDateOfBirth by remember { mutableStateOf("") }
    var birthWeight by remember { mutableStateOf("") }
    var birthLength by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = babyFirstName,
            onValueChange = { babyFirstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = babyLastName,
            onValueChange = { babyLastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = babyDateOfBirth,
            onValueChange = { babyDateOfBirth = it },
            label = { Text("Date of Birth (MM/DD/YYYY)") },
            placeholder = { Text("01/15/2024") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = birthWeight,
            onValueChange = { birthWeight = it },
            label = { Text("Birth Weight (lbs)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = birthLength,
            onValueChange = { birthLength = it },
            label = { Text("Birth Length (inches)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            placeholder = { Text("Male/Female/Other") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                isLoading = true
                val weightValue = birthWeight.toDoubleOrNull() ?: 0.0
                val lengthValue = birthLength.toDoubleOrNull() ?: 0.0

                saveChild(
                    firstName = babyFirstName.trim(),
                    lastName = babyLastName.trim(),
                    dob = babyDateOfBirth.trim(),
                    birthWeight = weightValue,
                    birthLength = lengthValue,
                    gender = gender.trim(),
                    onSuccess = {
                        isLoading = false
                        Toast.makeText(context, "Child added successfully!", Toast.LENGTH_SHORT).show()
                        // Clear form
                        babyFirstName = ""
                        babyLastName = ""
                        babyDateOfBirth = ""
                        birthWeight = ""
                        birthLength = ""
                        gender = ""
                    },
                    onError = { error ->
                        isLoading = false
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Black
                )
            } else {
                Text(text = "Save Child", textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddChildPreview() {
    ChildForm()
}