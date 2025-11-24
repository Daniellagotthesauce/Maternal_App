package com.example.maternal_childapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import androidx.compose.ui.res.colorResource
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

data class Child(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dob: String = "",
    val birthWeight: Double = 0.0,
    val birthLength: Double = 0.0,
    val gender: String = ""
)

data class VaccineRecord(
    val id: Int,
    val childId: String,
    val name: String,
    val scheduledDate: LocalDate,
    val isCompleted: Boolean = false,
    val completedDate: LocalDate? = null,
    val location: String? = null,
    val notes: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineListItem(
    vaccine: VaccineRecord,
    onMarkComplete: () -> Unit
) {
    var showDetails by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDetails = !showDetails }
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    vaccine.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (vaccine.isCompleted) Color.Gray else Color.Black
                )
                Text(
                    vaccine.scheduledDate.format(
                        DateTimeFormatter.ofPattern("MMMM d, yyyy")
                    ),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            if (vaccine.isCompleted) {
                Surface(
                    color = colorResource(R.color.baby_pink),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Done",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            } else {
                Button(
                    onClick = onMarkComplete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.baby_blue)
                    ),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Mark Done", fontSize = 12.sp)
                }
            }
        }

        // Expandable details
        if (showDetails) {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                vaccine.location?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📍 ", fontSize = 14.sp)
                        Text(
                            "Location: $it",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (vaccine.isCompleted && vaccine.completedDate != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✅ ", fontSize = 14.sp)
                        Text(
                            "Completed: ${vaccine.completedDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (!vaccine.notes.isNullOrEmpty()) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text("📝 ", fontSize = 14.sp)
                        Text(
                            "Notes: ${vaccine.notes}",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }

    Divider(modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun Vaccine(onBackClick: () -> Unit = {}) {
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var children by remember { mutableStateOf(listOf<Child>()) }
    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var showChildDropdown by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Load children from Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users")
                .document(userId)
                .collection("children")
                .get()
                .addOnSuccessListener { snapshot ->
                    val loadedChildren = snapshot.documents.map { doc ->
                        Child(
                            id = doc.id,
                            firstName = doc.getString("firstName") ?: "",
                            lastName = doc.getString("lastName") ?: "",
                            dob = doc.getString("dob") ?: "",
                            birthWeight = doc.getDouble("birthWeight") ?: 0.0,
                            birthLength = doc.getDouble("birthLength") ?: 0.0,
                            gender = doc.getString("gender") ?: ""
                        )
                    }
                    children = loadedChildren
                    if (loadedChildren.isNotEmpty()) selectedChild = loadedChildren.first()
                }
                .addOnFailureListener { e ->
                    Log.e("Vaccine", "Error loading children", e)
                }
        }
    }

    // Sample vaccine records (you'll load this from database based on selected child)
    var vaccineRecords by remember {
        mutableStateOf(
            listOf(
                VaccineRecord(1, "1", "BCG", LocalDate.now().plusDays(5)),
                VaccineRecord(2, "1", "Polio (OPV 0)", LocalDate.now().plusDays(5)),
                VaccineRecord(3, "1", "Hepatitis B", LocalDate.now().minusDays(3)),
                VaccineRecord(4, "1", "DTP 1", LocalDate.now().plusDays(42)),
                VaccineRecord(5, "1", "Polio 1", LocalDate.now().plusDays(42))
            )
        )
    }

    // Filter vaccines for selected child
    val childVaccines = vaccineRecords.filter { it.childId == selectedChild?.id }
    val vaccineDays = childVaccines.associate { it.scheduledDate to it.name }
    var selectedVaccine by remember { mutableStateOf<String?>(null) }

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
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
            ) {
            // Top Bar with Back Button and Title
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
                    text = "Vaccine Tracker",
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
                // Child Selector Dropdown
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
                                text = selectedChild?.firstName ?: "Select a child",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Select child",
                            tint = colorResource(R.color.royal_blue)
                        )
                    }
                }

                // Dropdown Menu
                DropdownMenu(
                    expanded = showChildDropdown,
                    onDismissRequest = { showChildDropdown = false }
                ) {
                    children.forEach { child ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(child.firstName, fontWeight = FontWeight.Bold)
                                    Text(
                                        "DOB: ${child.dob}",
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

                // Calendar
                SimpleCalendarView(
                    vaccineDays = vaccineDays,
                    onVaccineClick = { date, vaccine ->
                        selectedVaccine = "${date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}: $vaccine"
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Legend
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Legend",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(colorResource(R.color.strong_pink), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Today", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(colorResource(R.color.soft_blue), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Vaccine Day", fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Selected Vaccine Info
                selectedVaccine?.let { message ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.soft_blue)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Selected Vaccine",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = message,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Upcoming vaccines list
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
                                text = "Upcoming Vaccines",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            IconButton(
                                onClick = { showAddDialog = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add vaccine",
                                    tint = colorResource(R.color.baby_pink)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (childVaccines.isEmpty()) {
                            Text(
                                "No vaccines scheduled. Tap + to add.",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            childVaccines.sortedBy { it.scheduledDate }.forEach { vaccine ->
                                VaccineListItem(
                                    vaccine = vaccine,
                                    onMarkComplete = {
                                        vaccineRecords = vaccineRecords.map {
                                            if (it.id == vaccine.id) {
                                                it.copy(
                                                    isCompleted = true,
                                                    completedDate = LocalDate.now()
                                                )
                                            } else it
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add Vaccine Dialog
        if (showAddDialog) {
            AddVaccineDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, date, location, notes ->
                    // Add new vaccine record
                    val newVaccine = VaccineRecord(
                        id = vaccineRecords.maxOfOrNull { it.id }?.plus(1) ?: 1,
                        childId = selectedChild?.id ?: "",
                        name = name,
                        scheduledDate = date,
                        location = location,
                        notes = notes
                    )
                    vaccineRecords = vaccineRecords + newVaccine
                    showAddDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccineDialog(
    onDismiss: () -> Unit,
    onAdd: (String, LocalDate, String, String) -> Unit
) {
    var vaccineName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add Vaccine Schedule",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Vaccine Name
                OutlinedTextField(
                    value = vaccineName,
                    onValueChange = { vaccineName = it },
                    label = { Text("Vaccine Name *") },
                    placeholder = { Text("e.g., BCG, Polio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(R.color.strong_pink)
                    )
                ) {
                    Text("Date: ${selectedDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Location
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    placeholder = { Text("e.g., Nairobi Hospital") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes ") },
                    placeholder = { Text("Any additional information...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    maxLines = 3
                )

                if (showDatePicker) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Use device calendar or enter date manually",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAdd(
                        vaccineName,
                        selectedDate,
                        location.ifEmpty { "Not specified" },
                        notes.ifEmpty { "" }
                    )
                },
                enabled = vaccineName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.soft_blue),
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text("Add Vaccine", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SimpleCalendarView(
    vaccineDays: Map<LocalDate, String>,
    onVaccineClick: (LocalDate, String) -> Unit
) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    Column {
        // Month navigation header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { currentMonth = currentMonth.minusMonths(1) }
            ) {
                Text("◀", fontSize = 20.sp, color = colorResource(R.color.strong_pink))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentMonth.year.toString(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = { currentMonth = currentMonth.plusMonths(1) }
            ) {
                Text("▶", fontSize = 20.sp, color = colorResource(R.color.soft_blue))
            }
        }

        // "Today" button to jump back to current month
        if (currentMonth != YearMonth.now()) {
            TextButton(
                onClick = { currentMonth = YearMonth.now() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "Go to Today",
                    fontSize = 12.sp,
                    color = Color(0xFFFEA3C9)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var dayCounter = 1
        for (week in 0..5) {
            if (dayCounter > daysInMonth) break

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 0..6) {
                    if (week == 0 && dayOfWeek < firstDayOfWeek) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else if (dayCounter <= daysInMonth) {
                        val currentDate = currentMonth.atDay(dayCounter)
                        val isToday = currentDate == today
                        val isVaccineDay = vaccineDays.containsKey(currentDate)

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .background(
                                    when {
                                        isToday -> colorResource(R.color.strong_pink)
                                        isVaccineDay -> colorResource(R.color.soft_blue)
                                        else -> Color.Transparent
                                    },
                                    CircleShape
                                )
                                .clickable {
                                    if (isVaccineDay) {
                                        vaccineDays[currentDate]?.let { vaccine ->
                                            onVaccineClick(currentDate, vaccine)
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayCounter.toString(),
                                color = when {
                                    isToday || isVaccineDay -> Color.White
                                    else -> Color.Black
                                },
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                        dayCounter++
                    } else {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VaccinePreview() {
    Vaccine()
}