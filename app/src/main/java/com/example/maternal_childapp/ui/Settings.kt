package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
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
import com.example.maternal_childapp.R
import kotlinx.coroutines.channels.ticker


private val IconSize = 24.dp
private val IconSpacing = 12.dp

@Composable
fun Settings(){
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
                //.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))

            SettingsInfoCard(
                title = "Account Details",
                text = "Manage your personal information",
                icon = Icons.Default.Person
            )
            SettingsInfoCard(
                title = "Notifications Settings",
                text = "Customise your notifications preference",
                icon = Icons.Default.Person
            )
            SettingsInfoCard(
                title = "Account Details",
                text = "Manage your personal information",
                icon = Icons.Default.Person
            )
            SettingsInfoCard(
                title = "Help & Support",
                text = "Manage your personal information",
                icon = Icons.Default.Person
            )


        }
    }


@Composable
fun SettingsInfoCard(
    title: String,
    text: String,
    icon: ImageVector = Icons.Default.Info
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
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
fun SettingsPreview() {
    Settings()
}
