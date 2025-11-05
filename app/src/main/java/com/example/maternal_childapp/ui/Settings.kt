package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.material3.Button
import androidx.compose.ui.text.style.TextAlign


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
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Account",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

       ReusableSettingsCard {
           SettingsInfoCard(
               title = "Account Details",
               text = "Manage your personal information",
               icon = Icons.Default.Person
           )
           SettingsInfoCard(
               title = "Password",
               text = "Manage your personal information",
               icon = Icons.Default.Lock
           )
       }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Notifications",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        SettingsInfoCard(
            title = "Notifications Settings",
            text = "Customise your notifications preference",
            icon = Icons.Default.Notifications
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "App Preferences",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ReusableSettingsCard {
            SettingsInfoCard(
                title = "Dark Mode",
                text = "Manage your personal information",
                icon = Icons.Default.Settings
            )
            SettingsInfoCard(
                title = "Language",
                text = "Manage your personal information",
                icon = Icons.Default.Place
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Help & Support",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReusableSettingsCard {
            SettingsInfoCard(
                title = "Help Center",
                text = "Manage your personal information",
                icon = Icons.Default.Call
            )
            SettingsInfoCard(
                title = "Contact Us",
                text = "Manage your personal information",
                icon = Icons.Default.Email
            )
        }
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
            .fillMaxWidth(),
            //.padding(vertical = 6.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

@Composable
fun ReusableSettingsCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
            //.padding(vertical = 6.dp),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    Settings()
}