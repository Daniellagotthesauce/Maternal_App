package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.maternal_childapp.R
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
@Composable
fun OnboardingScreen(
    userName: String = "Faith",
    onContinueClick: (() -> Unit)? = null
) {
    // Fetch username from Firebase
    var firebaseUserName by remember { mutableStateOf("") }
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        auth.currentUser?.uid?.let { userId ->
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    firebaseUserName = document.getString("firstName") ?: "User"
                }
                .addOnFailureListener {
                    firebaseUserName = "User"
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)

    ) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top bar: Welcome + settings icon
                Text(
                    text = "Welcome ${firebaseUserName.ifEmpty { userName }}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.Black
                )

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.Black
                )
            }

        }


        //  Form section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Tell us about yourself",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Form state
            var pregnancyStatus by remember { mutableStateOf("") }
            var weeksPregnant by remember { mutableStateOf("") }
            var childAge by remember { mutableStateOf("") }
            var conditions by remember { mutableStateOf("") }
            var language by remember { mutableStateOf("") }

            val shape = RoundedCornerShape(12.dp)

            // Input fields
            OutlinedTextField(
                value = pregnancyStatus,
                onValueChange = { pregnancyStatus = it },
                placeholder = { Text("Are you pregnant or have a child?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = shape
            )

            OutlinedTextField(
                value = weeksPregnant,
                onValueChange = { weeksPregnant = it },
                placeholder = { Text("If pregnant, how many weeks?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = shape
            )

            OutlinedTextField(
                value = childAge,
                onValueChange = { childAge = it },
                placeholder = { Text("If you have a child, how old is your child?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = shape
            )

            OutlinedTextField(
                value = conditions,
                onValueChange = { conditions = it },
                placeholder = { Text("Do you have any known medical conditions?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = shape
            )

            OutlinedTextField(
                value = language,
                onValueChange = { language = it },
                placeholder = { Text("What is your primary language?") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = shape
            )

            Spacer(Modifier.height(250.dp))

            // Continue button
            Button(
                onClick = { onContinueClick?.invoke() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F51F5),
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Continue",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun onBooardPreview() {
    OnboardingScreen()
}