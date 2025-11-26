package com.example.maternal_childapp.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.GoogleAuthProvider
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


//Login screen
@Composable
fun LoginScreen(
    onLoginClick: (() -> Unit)? = null,
    onGoogleClick: (() -> Unit)? = null
) {
    // Background gradients
//    val vertical = Brush.verticalGradient(
//        listOf(
//            colorResource(R.color.baby_blue),
//            colorResource(R.color.baby_pink)
//        )
//    )
    val radial = Brush.radialGradient(
        colors = listOf(Color(0x22FF9BB3), Color.Transparent)
    )

    val context = LocalContext.current
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(stringResource(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential)
                .addOnSuccessListener { firebaseUser ->
                    val uid = firebaseUser.user!!.uid
                    val userRef = db.collection("users").document(uid)

                    userRef.get().addOnSuccessListener { document ->
                        if (!document.exists()) {
                            val newUser = hashMapOf(
                                "firstName" to firebaseUser.user!!.displayName.orEmpty(),
                                "email" to firebaseUser.user!!.email.orEmpty()
                            )
                            userRef.set(newUser)
                        }
                        Toast.makeText(context, "Welcome ${firebaseUser.user!!.displayName}!", Toast.LENGTH_SHORT).show()
                        onLoginClick?.invoke()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Google login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    // Form state
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(radial)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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
                onClick = {
                    googleSignInClient.signOut().addOnCompleteListener {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    }
                },
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

    //Google Sign-In setup
    val gso = remember {
        com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
            com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(context.getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()
    }

    //Create a GoogleSignInClient
    val googleSignInClient = remember { com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context, gso) }

    // Launcher for the Google Sign-In intent
    val launcher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val firebaseUser = auth.currentUser!!
                        val uid = firebaseUser.uid
                        val userRef = db.collection("users").document(uid)

                        userRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                // User already exists in Firestore → show message
                                Toast.makeText(context, "User already registered!", Toast.LENGTH_SHORT).show()
                            } else {
                                // New user → create document
                                val newUser = hashMapOf(
                                    "firstName" to firebaseUser.displayName.orEmpty(),
                                    "email" to firebaseUser.email.orEmpty()
                                )
                                userRef.set(newUser)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                        onRegisterClick?.invoke() // Navigate after registration
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Google Sign-In Failed: ${authTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


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
                                    onRegisterClick?.invoke()
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
                onClick = {
                    googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                  }
                },
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

