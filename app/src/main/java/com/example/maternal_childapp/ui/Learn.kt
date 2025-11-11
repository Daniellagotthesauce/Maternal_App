package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import android.opengl.Matrix.length
import android.text.TextUtils.replace
//import SearchBarDefaults.InputField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Learn() {
    Box(
        Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
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
                    IconButton(
                        onClick = { /* Handle back action */ },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Learn",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    IconButton(
                        onClick = { /* Handle back action */ },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

//            var expanded by remember { mutableStateOf(false) }
//            var searchQuery by remember { mutableStateOf("") }
//            var searchText by remember { mutableStateOf("") }
//
//            SearchBar(
//                modifier = Modifier.semantics { traversalIndex = 0f },
//                inputField = {
//                    SearchBarDefaults.InputField(
//                        query = textFieldState.text.toString(),
//                        onQueryChange = { textFieldState.edit { replace(0, length, it) } },
//                        onSearch = {
//                            onSearch(textFieldState.text.toString())
//                            expanded = false
//                        },
//                        expanded = expanded,
//                        onExpandedChange = { expanded = it },
//                        placeholder = { Text("Search") }
//                    )
//                },
//                expanded = expanded,
//                onExpandedChange = { expanded = it },
//            ) {
//                // Display search results in a scrollable column
//                Column(Modifier.verticalScroll(rememberScrollState())) {
//                    searchResults.forEach { result ->
//                        ListItem(
//                            headlineContent = { Text(result) },
//                            modifier = Modifier
//                                .clickable {
//                                    textFieldState.edit { replace(0, length, result) }
//                                    expanded = false
//                                }
//                                .fillMaxWidth()
//                        )
//                    }
//                }
//            }

            Text(
                text = "Featured",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(10.dp)
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
            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Article",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.width(180.dp).padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Image(
                                painter = painterResource(R.drawable.img),
                                contentDescription = "Healthy food",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                            Text("Week 24", color = colorResource(R.color.black))
                            Text("Healthy Eating Habits", fontWeight = FontWeight.Bold)
                            Text(
                                "Learn about the best foods to support your pregnancy.",
                                color = colorResource(R.color.black)
                            )
                        }

                    }
                }
                Card(
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.width(180.dp).padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Image(
                                painter = painterResource(R.drawable.img),
                                contentDescription = "Healthy food",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                            Text("Week 24", color = colorResource(R.color.black))
                            Text("Healthy Eating Habits", fontWeight = FontWeight.Bold)
                            Text(
                                "Learn about the best foods to support your pregnancy.",
                                color = colorResource(R.color.black)
                            )
                        }
                    }
                }
            }
                //Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.width(180.dp).padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Image(
                                    painter = painterResource(R.drawable.img),
                                    contentDescription = "Healthy food",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                )
                                Text("Week 24", color = colorResource(R.color.black))
                                Text("Healthy Eating Habits", fontWeight = FontWeight.Bold)
                                Text(
                                    "Learn about the best foods to support your pregnancy.",
                                    color = colorResource(R.color.black)
                                )
                            }

                        }
                    }
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.width(180.dp).padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Image(
                                    painter = painterResource(R.drawable.img),
                                    contentDescription = "Healthy food",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                )
                                Text("Week 24", color = colorResource(R.color.black))
                                Text("Healthy Eating Habits", fontWeight = FontWeight.Bold)
                                Text(
                                    "Learn about the best foods to support your pregnancy.",
                                    color = colorResource(R.color.black)
                                )
                            }
                        }
                    }
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun LearnPreview() {
    Learn()
}