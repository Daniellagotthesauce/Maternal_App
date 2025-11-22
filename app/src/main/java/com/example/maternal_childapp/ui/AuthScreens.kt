package com.example.maternal_childapp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

//Login screen
@Composable
fun LoginScreen(
    onLoginClick: (() -> Unit)? = null,
    onGoogleClick: (() -> Unit)? = null
) {
    // Background gradients
    val vertical = Brush.verticalGradient(
        listOf(
            colorResource(R.color.baby_blue),
            colorResource(R.color.baby_pink)
        )
    )
    val radial = Brush.radialGradient(
        colors = listOf(Color(0x22FF9BB3), Color.Transparent)
    )

    val context = LocalContext.current
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()

    // Form state
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(vertical)
            .background(radial)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))

            // Title
            Text(
                "BumpToBaby",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(36.dp))

            Text(
                "Login to account",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black.copy(alpha = 0.75f)
            )

            Spacer(Modifier.height(20.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                placeholder = { Text("email@domain.com") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Password field with show/hide
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                placeholder = { Text("Password") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Text(
                        if (showPassword) "Hide" else "Show",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showPassword = !showPassword }
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Login button
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            val uid = result.user!!.uid
                            db.collection("users").document(uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val firstName = document.getString("firstName")
                                        Toast.makeText(context, "Welcome $firstName!", Toast.LENGTH_SHORT).show()
                                       onLoginClick?.invoke() // Navigate to home screen here
                                    } else {
                                        Toast.makeText(context, "User data not found!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to fetch data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Login", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(14.dp))

            // Divider with "or"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(Modifier.weight(1f), color = Color.White.copy(alpha = 0.6f))
                Text("  or  ", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                Divider(Modifier.weight(1f), color = Color.White.copy(alpha = 0.6f))
            }

            Spacer(Modifier.height(18.dp))

            // Google login button (placeholder)
            OutlinedButton(
                onClick = { onGoogleClick?.invoke() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = "Google",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Login with Google", fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(18.dp))

            // Terms text
            Text(
                text = "By clicking continue, you agree to our Terms of Service and Privacy Policy",
                color = Color.Black.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}


//SIGN UP SCREEN

@Composable
fun SignUpScreen(
    onRegisterClick: (() -> Unit)? = null,
    onGoogleClick: (() -> Unit)? = null
) {
    // Gradient background
    val background = Brush.verticalGradient(
        listOf(
            colorResource(R.color.baby_pink),
            Color.White,
            colorResource(R.color.baby_blue)
        )
    )

    // Firebase instances
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // Form state
    var firstName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val fieldShape = RoundedCornerShape(10.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))

            // Title
            Text(
                "BumpToBaby",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(18.dp))

            Text(
                "Create an account",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(18.dp))

            // TextFields
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("First Name") },
                singleLine = true,
                shape = fieldShape
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Phone Number") },
                singleLine = true,
                shape = fieldShape
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("DOB") },
                singleLine = true,
                shape = fieldShape
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("email@domain.com") },
                singleLine = true,
                shape = fieldShape
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = fieldShape
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = fieldShape
            )
            Spacer(Modifier.height(16.dp))

            // Register button
            Button(
                onClick = {
                    // Validation
                    if (firstName.isBlank() || phone.isBlank() || dob.isBlank() ||
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank()
                    ) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Prepare user data
                    val userData = mapOf(
                        "firstName" to firstName,
                        "phone" to phone,
                        "dob" to dob,
                        "email" to email
                    )

                    // Create user in Firebase Auth
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->
                            val uid = result.user!!.uid
                            db.collection("users")
                                .document(uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Auth failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Register", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(Modifier.weight(1f), color = Color.LightGray)
                Text("  or  ", color = Color.Gray, fontSize = 12.sp)
                Divider(Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(Modifier.height(16.dp))

            // Continue with Google
            OutlinedButton(
                onClick = { onGoogleClick?.invoke() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = "Google",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Continue with Google", fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "By clicking continue, you agree to our Terms of Service and Privacy Policy",
                color = Color.Black.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(12.dp))
        }
    }
}

