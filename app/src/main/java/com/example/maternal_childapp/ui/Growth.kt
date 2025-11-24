package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// Temporary Child data class for Growth Tracker
// TODO: Create a shared models file for both Vaccine and Growth
data class ChildData(
    val id: Int,
    val name: String,
    val dateOfBirth: LocalDate
)

data class GrowthMeasurement(
    val id: Int,
    val childId: Int,
    val date: LocalDate,
    val weight: Double, // in kg
    val height: Double, // in cm
    val headCircumference: Double? = null, // in cm
    val notes: String? = null
)

data class Milestone(
    val id: Int,
    val name: String,
    val ageMonths: Int,
    val category: String, // Physical, Cognitive, Social
    val description: String,
    val isCompleted: Boolean = false,
    val completedDate: LocalDate? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthTracker(onBackClick: () -> Unit = {}) {
    // Sample children - use the same Child data class from Vaccine.kt
    val children = remember {
        listOf(
            ChildData(1, "Emma", LocalDate.now().minusMonths(2)),
            ChildData(2, "James", LocalDate.now().minusYears(1).minusMonths(3)),
            ChildData(3, "Sarah", LocalDate.now().minusYears(2))
        )
    }

    var selectedChild by remember { mutableStateOf(children.firstOrNull()) }
    var showChildDropdown by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Sample growth measurements
    var measurements by remember {
        mutableStateOf(
            listOf(
                GrowthMeasurement(1, 1, LocalDate.now().minusDays(30), 4.5, 55.0, 38.0),
                GrowthMeasurement(2, 1, LocalDate.now().minusDays(15), 4.8, 57.0, 39.0),
                GrowthMeasurement(3, 1, LocalDate.now(), 5.1, 58.5, 39.5)
            )
        )
    }

    // Sample milestones
    var milestones by remember {
        mutableStateOf(
            listOf(
                Milestone(1, "First Smile", 2, "Social", "Baby smiles in response to you", true, LocalDate.now().minusDays(20)),
                Milestone(2, "Holds Head Up", 2, "Physical", "Can hold head up while on tummy", false),
                Milestone(3, "Tracks Objects", 2, "Cognitive", "Follows moving objects with eyes", true, LocalDate.now().minusDays(10)),
                Milestone(4, "Rolls Over", 4, "Physical", "Rolls from tummy to back", false),
                Milestone(5, "Reaches for Toys", 4, "Physical", "Reaches for and grasps toys", false),
                Milestone(6, "Responds to Name", 6, "Cognitive", "Turns head when name is called", false)
            )
        )
    }

    // Filter data for selected child
    val childMeasurements = measurements.filter { it.childId == selectedChild?.id }.sortedByDescending { it.date }
    val latestMeasurement = childMeasurements.firstOrNull()

    // Calculate child's age in months
    val ageInMonths = selectedChild?.let {
        ChronoUnit.MONTHS.between(it.dateOfBirth, LocalDate.now()).toInt()
    } ?: 0

    val relevantMilestones = milestones.filter { it.ageMonths <= ageInMonths + 2 }
    val completedMilestones = relevantMilestones.count { it.isCompleted }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Growth Tracker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Child Selector
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showChildDropdown = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Selected Child",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = selectedChild?.name ?: "Select a child",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$ageInMonths months old",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Select child",
                            tint = colorResource(R.color.strong_pink)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showChildDropdown,
                    onDismissRequest = { showChildDropdown = false }
                ) {
                    children.forEach { child ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(child.name, fontWeight = FontWeight.Bold)
                                    Text(
                                        "DOB: ${child.dateOfBirth.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            },
                            onClick = {
                                selectedChild = child
                                showChildDropdown = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Latest Measurements Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Latest Measurements",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            IconButton(
                                onClick = { showAddDialog = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add measurement",
                                    tint = colorResource(R.color.strong_pink)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (latestMeasurement != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                MeasurementBox(
                                    label = "Weight",
                                    value = "${latestMeasurement.weight} kg",
                                    icon = "⚖️"
                                )
                                MeasurementBox(
                                    label = "Height",
                                    value = "${latestMeasurement.height} cm",
                                    icon = "📏"
                                )
                                latestMeasurement.headCircumference?.let {
                                    MeasurementBox(
                                        label = "Head",
                                        value = "$it cm",
                                        icon = "👶"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                "Recorded: ${latestMeasurement.date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        } else {
                            Text(
                                "No measurements recorded yet. Tap + to add.",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Growth Chart Placeholder
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Growth Chart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (childMeasurements.size >= 2) {
                            SimpleGrowthChart(childMeasurements.reversed())
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Add more measurements to see growth trends",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Milestones Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Milestones",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "$completedMilestones/${relevantMilestones.size} completed",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        relevantMilestones.forEach { milestone ->
                            MilestoneItem(
                                milestone = milestone,
                                onToggle = {
                                    milestones = milestones.map {
                                        if (it.id == milestone.id) {
                                            it.copy(
                                                isCompleted = !it.isCompleted,
                                                completedDate = if (!it.isCompleted) LocalDate.now() else null
                                            )
                                        } else it
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Measurement History
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Measurement History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        childMeasurements.forEach { measurement ->
                            MeasurementHistoryItem(measurement)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Add Measurement Dialog
        if (showAddDialog) {
            AddMeasurementDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { weight, height, head, date, notes ->
                    val newMeasurement = GrowthMeasurement(
                        id = measurements.maxOfOrNull { it.id }?.plus(1) ?: 1,
                        childId = selectedChild?.id ?: 0,
                        date = date,
                        weight = weight,
                        height = height,
                        headCircumference = head,
                        notes = notes
                    )
                    measurements = measurements + newMeasurement
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun MeasurementBox(label: String, value: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFFFFF0F5), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun SimpleGrowthChart(measurements: List<GrowthMeasurement>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            "Weight Trend",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        measurements.forEach { measurement ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    measurement.date.format(DateTimeFormatter.ofPattern("MMM d")),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Row {
                    Text("${measurement.weight} kg", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("${measurement.height} cm", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun MilestoneItem(milestone: Milestone, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onToggle() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (milestone.isCompleted) colorResource(R.color.soft_blue) else Color.LightGray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                milestone.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (milestone.isCompleted) Color.Gray else Color.Black
            )
            Text(
                "${milestone.ageMonths} months • ${milestone.category}",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
    Divider()
}

@Composable
fun MeasurementHistoryItem(measurement: GrowthMeasurement) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                measurement.date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Text("⚖️ ${measurement.weight} kg", fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Text("📏 ${measurement.height} cm", fontSize = 13.sp, color = Color.Gray)
            measurement.headCircumference?.let {
                Spacer(modifier = Modifier.width(16.dp))
                Text("👶 $it cm", fontSize = 13.sp, color = Color.Gray)
            }
        }
        measurement.notes?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Note: $it", fontSize = 12.sp, color = Color.DarkGray, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
    }
    Divider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeasurementDialog(
    onDismiss: () -> Unit,
    onAdd: (Double, Double, Double?, LocalDate, String) -> Unit
) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var headCircumference by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add Measurement",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg) *") },
                    placeholder = { Text("e.g., 5.1") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm) *") },
                    placeholder = { Text("e.g., 58.5") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = headCircumference,
                    onValueChange = { headCircumference = it },
                    label = { Text("Head Circumference (cm)") },
                    placeholder = { Text("e.g., 39.5") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    placeholder = { Text("Doctor's comments...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Date: ${selectedDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val w = weight.toDoubleOrNull()
                    val h = height.toDoubleOrNull()
                    val hc = headCircumference.toDoubleOrNull()
                    if (w != null && h != null) {
                        onAdd(w, h, hc, selectedDate, notes)
                    }
                },
                enabled = weight.toDoubleOrNull() != null && height.toDoubleOrNull() != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.strong_pink)
                )
            ) {
                Text("Add", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GrowthTrackerPreview() {
    GrowthTracker()
}