package com.example.maternal_childapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import androidx.compose.ui.res.painterResource

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(vertical)
            .background(radial)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(28.dp))

            //  Title
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

            //  Form state
            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var showPassword by rememberSaveable { mutableStateOf(false) }

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                placeholder = { Text("email@domain.com") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Password (with Show/Hide)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
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

            //  Black Login button
            Button(
                onClick = { onLoginClick?.invoke() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) { Text("Login", fontWeight = FontWeight.SemiBold) }

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

            // 9) Google button
            OutlinedButton(
                onClick = { onGoogleClick?.invoke() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
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

@Composable
fun SignUpScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Sign Up Screen")
    }
}
