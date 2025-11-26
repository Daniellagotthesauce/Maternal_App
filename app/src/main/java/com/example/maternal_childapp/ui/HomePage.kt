package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.ui.res.colorResource
import com.example.maternal_childapp.R


@Composable
fun HomePage(
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit
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
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Surface(
            color = Color.White,
            modifier = Modifier.fillMaxWidth().height(60.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mother),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "My Health",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                IconButton(onClick = { onSettingsClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Upcoming section
        Text(
            text = "Upcoming",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(10.dp, 0.dp)
        )
        //Spacer(modifier = Modifier.height(5.dp))
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(15.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Calendar",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column {
                    Text("Antenatal Checkup", fontWeight = FontWeight.Medium)
                    Text("28th July 2024", color = colorResource(R.color.baby_pink))
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Quick Actions
        Text(
            text = "Quick Actions",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(10.dp,0.dp)
        )
        //Spacer(modifier = Modifier.height(5.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onAddClick() },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.strong_pink)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).padding(10.dp)
            ) {
                Text("Add Baby")
            }
            Spacer(modifier = Modifier.width(18.dp))
            Button(
                onClick = { /* Ask a Doctor */ },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.baby_blue)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).padding(10.dp)
            ) {
                Text("Ask a Doctor", color = colorResource(R.color.black))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Tips Section
        Text(
            text = "Tips for You",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(10.dp,0.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Week 24", color = colorResource(R.color.black))
                    Text("Healthy Eating Habits", fontWeight = FontWeight.Bold)
                    Text(
                        "Learn about the best foods to support your pregnancy.",
                        color = colorResource(R.color.black)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "Healthy food",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Week 24", color = colorResource(R.color.black))
                    Text("Healthy Eating Habits", fontWeight = FontWeight.Bold)
                    Text(
                        "Learn about the best foods to support your pregnancy.",
                        color = colorResource(R.color.black)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "Healthy food",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage(onAddClick = {}, onSettingsClick = {})
}
