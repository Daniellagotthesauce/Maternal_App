package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextAlign
import com.example.maternal_childapp.R


private val IconSize = 24.dp
private val IconSpacing = 12.dp

@Composable
fun MorePageRoute() {
    val viewModel: ProfileViewModel = viewModel()
    val state = viewModel.uiState

    MorePage(
        userName = state.userName,
        userDob = state.userDob,
        userEmail = state.userEmail,
        phone = state.phone,
        numberOfChildren = state.numberOfChildren,
        isLoading = state.isLoading,
        error = state.error,
        onRefresh = { viewModel.refresh() }
    )
}
@Composable
fun MorePage(
    userName: String = "User",
    userDob: String = "Not set",
    userEmail: String = "Not set",
    phone: String = "Not set",
    numberOfChildren: Int = 0,
    isLoading: Boolean = false,
    error: String? = null,
    onRefresh: () -> Unit = {}
){
    Box(
        Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.background1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
        Column(
            modifier = Modifier
                .fillMaxSize(),
                //.background(brush =  backgroundBrush),
                //.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
             ){
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth().height(60.dp),
            )
            {
                Text(
                    text = "Edit Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(15.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = R.drawable.mother),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (error != null) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Text(
                text = userName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard(
                "Personal Information",
                    "Name: $userName\nDate of Birth: $userDob",
                    icon = Icons.Default.Person
            )

            InfoCard(
                "Contact Information",
                "Email: $userEmail\nPhone: $phone",
                icon = Icons.Default.Phone
            )

            InfoCard(
                "Health Information",
                "Number of Children: $numberOfChildren",
                icon = Icons.Default.Favorite)

            InfoCard(
                "Preferences and Settings",
                "Language: English",
                icon = Icons.Default.Settings
            )
            InfoCard(
                "Support and Resources",
                "FAQ'S",
                icon = Icons.Default.Info
            )

        }
    }

@Composable
fun InfoCard(
    title: String,
    text: String,
    icon: ImageVector = Icons.Default.Info
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

        ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )


            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
        }
            Row(
                modifier = Modifier.fillMaxWidth()
            )
            {
                Spacer(modifier = Modifier.width(IconSize + IconSpacing))

                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray

                )
            }
        }
    }
 }


@Preview(showBackground = true)
@Composable
fun MorePagePreview() {
    MorePage()
}
