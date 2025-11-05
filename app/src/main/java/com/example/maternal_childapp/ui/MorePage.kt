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


private val IconSize = 24.dp
private val IconSpacing = 12.dp
@Composable
fun MorePage(){
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
    )
        {       Spacer(modifier = Modifier.height(6.dp))
            Image(

                painter = painterResource(id = R.drawable.danie), // replace with actual profile image later
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Faith Wangeci",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoCard("Personal Information",
                    "Name: Faith Wangeci\nDate of Birth: 1990-05-15",
                    icon = Icons.Default.Person )
            InfoCard("Health Information",
                "Child's Age: 1 year",
                icon = Icons.Default.Favorite)
            InfoCard("Medical History",
                "Blood Type O+",
                icon = Icons.Default.Check)
            InfoCard("Preferences and Settings",
                "Language: English",
                icon = Icons.Default.Settings)
            InfoCard("Support and Resources",
                "FAQ'S",
                icon = Icons.Default.Info)

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
