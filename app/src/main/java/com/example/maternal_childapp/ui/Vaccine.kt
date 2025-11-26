package com.example.maternal_childapp.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material.icons.filled.DateRange
import java.time.Instant
import java.time.ZoneId

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
    val id: String = "",
    val childId: String = "",
    val name: String = "",
    val scheduledDate: String = "",
    val isCompleted: Boolean = false,
    val completedDate: String? = null,
    val location: String? = null,
    val notes: String? = null
) {
    // Helper functions to convert to/from LocalDate
    fun getScheduledLocalDate(): LocalDate = LocalDate.parse(scheduledDate)
    fun getCompletedLocalDate(): LocalDate? = completedDate?.let { LocalDate.parse(it) }
}

fun saveVaccine(
    userId: String,
    childId: String,
    name: String,
    scheduledDate: String,
    location: String,
    notes: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    if (name.isBlank()) {
        onError("Vaccine name is required")
        return
    }

    val db = FirebaseFirestore.getInstance()

    val vaccineData = hashMapOf(
        "name" to name,
        "scheduledDate" to scheduledDate.toString(),
        "isCompleted" to false,
        "completedDate" to null,
        "location" to location.ifEmpty { "Not specified" },
        "notes" to notes,
        "childId" to childId,
        "createdAt" to FieldValue.serverTimestamp()
    )

    db.collection("users")
        .document(userId)
        .collection("children")
        .document(childId)
        .collection("vaccine")
        .add(vaccineData)
        .addOnSuccessListener { documentReference ->
            Log.d("SAVE_VACCINE", "Vaccine added with ID: ${documentReference.id}")
            onSuccess()
        }
        .addOnFailureListener { exception ->
            Log.e("SAVE_VACCINE", "Error adding vaccine", exception)
            onError("Failed to save: ${exception.message}")
        }
}

fun loadVaccines(
    userId: String,
    childId: String,
    onSuccess: (List<VaccineRecord>) -> Unit,
    onError: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users")
        .document(userId)
        .collection("children")
        .document(childId)
        .collection("vaccine")
        .get()
        .addOnSuccessListener { snapshot ->
            val vaccines = snapshot.documents.mapNotNull { doc ->
                try {
                    VaccineRecord(
                        id = doc.id,
                        childId = doc.getString("childId") ?: "",
                        name = doc.getString("name") ?: "",
                        scheduledDate = doc.getString("scheduledDate") ?: "",
                        isCompleted = doc.getBoolean("isCompleted") ?: false,
                        completedDate = doc.getString("completedDate"),
                        location = doc.getString("location"),
                        notes = doc.getString("notes")
                    )
                } catch (e: Exception) {
                    Log.e("LOAD_VACCINES", "Error parsing vaccine document", e)
                    null
                }
            }
            Log.d("LOAD_VACCINES", "Loaded ${vaccines.size} vaccines for child $childId")
            onSuccess(vaccines)
        }
        .addOnFailureListener { exception ->
            Log.e("LOAD_VACCINES", "Error loading vaccines", exception)
            onError("Failed to load vaccines: ${exception.message}")
        }
}

fun markVaccineComplete(
    userId: String,
    childId: String,
    vaccineId: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    val updates = hashMapOf<String, Any>(
        "isCompleted" to true,
        "completedDate" to LocalDate.now().toString()
    )

    db.collection("users")
        .document(userId)
        .collection("children")
        .document(childId)
        .collection("vaccine")
        .document(vaccineId)
        .update(updates)
        .addOnSuccessListener {
            Log.d("UPDATE_VACCINE", "Vaccine marked complete")
            onSuccess()
        }
        .addOnFailureListener { exception ->
            Log.e("UPDATE_VACCINE", "Error marking vaccine complete", exception)
            onError("Failed to update: ${exception.message}")
        }
}

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
                    vaccine.getScheduledLocalDate().format(
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
                        Text(
                            "Completed: ${vaccine.getCompletedLocalDate()?.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (!vaccine.notes.isNullOrEmpty()) {
                    Row(verticalAlignment = Alignment.Top) {
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

    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun Vaccine(onBackClick: () -> Unit = {}) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    var children by remember { mutableStateOf(listOf<Child>()) }
    var selectedChild by remember { mutableStateOf<Child?>(null) }
    var showChildDropdown by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var vaccineRecords by remember { mutableStateOf(listOf<VaccineRecord>()) }
    var isLoadingVaccines by remember { mutableStateOf(false) }

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

    LaunchedEffect(selectedChild?.id) {
        if (userId != null && selectedChild?.id != null) {
            isLoadingVaccines = true
            loadVaccines(
                userId = userId,
                childId = selectedChild!!.id,
                onSuccess = { vaccines ->
                    vaccineRecords = vaccines
                    isLoadingVaccines = false
                },
                onError = { error ->
                    isLoadingVaccines = false
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    val childVaccines = vaccineRecords.filter { it.childId == selectedChild?.id }
    val vaccineDays = childVaccines.associate { it.getScheduledLocalDate() to it.name }
    var selectedVaccine by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

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
                modifier = Modifier.fillMaxWidth()
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
                        text = "Vaccine Tracker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

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

                // The Calendar
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SimpleCalendarView(
                        vaccines = childVaccines,
                        onVaccineClick = { date, vaccine ->
                            selectedVaccine = "${date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}: $vaccine"
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Legend
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Dynamic Legend
                    val hasPendingVaccine = childVaccines.any { !it.isCompleted }
                    val hasCompletedVaccine = childVaccines.any { it.isCompleted }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Legend",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Today
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

                        // Vaccine Day (pending)
                        if (hasPendingVaccine) {
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
                        Spacer(modifier = Modifier.height(8.dp))
                        // Completed
                        if (hasCompletedVaccine) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(colorResource(R.color.grayish), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Completed", fontSize = 14.sp)
                            }
                        }
                    }

                }

                // Selected Vaccine Info
                selectedVaccine?.let { message ->
                    Spacer(modifier = Modifier.height(12.dp))
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
                            childVaccines.sortedBy { it.getScheduledLocalDate() }
                                .forEach { vaccine ->
                                    VaccineListItem(
                                        vaccine = vaccine,
                                        onMarkComplete = {
                                            if (userId != null && selectedChild != null) {
                                                markVaccineComplete(
                                                    userId = userId,
                                                    childId = selectedChild!!.id,
                                                    vaccineId = vaccine.id,
                                                    onSuccess = {
                                                        Toast.makeText(
                                                            context,
                                                            "Vaccine marked complete!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        // Reload vaccines
                                                        loadVaccines(
                                                            userId = userId,
                                                            childId = selectedChild!!.id,
                                                            onSuccess = { vaccines ->
                                                                vaccineRecords = vaccines
                                                            },
                                                            onError = { }
                                                        )
                                                    },
                                                    onError = { error ->
                                                        Toast.makeText(
                                                            context,
                                                            error,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                )
                                            }
                                        }
                                    )
                                }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Add Vaccine Dialog
        if (showAddDialog && selectedChild != null && userId != null) {
            AddVaccineDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, date, location, notes ->
                    saveVaccine(
                        userId = userId,
                        childId = selectedChild!!.id,
                        name = name,
                        scheduledDate = date,
                        location = location,
                        notes = notes,
                        onSuccess = {
                            Toast.makeText(context, "Vaccine added successfully!", Toast.LENGTH_SHORT).show()
                            // Reload vaccines after adding
                            loadVaccines(
                                userId = userId,
                                childId = selectedChild!!.id,
                                onSuccess = { vaccines ->
                                    vaccineRecords = vaccines
                                },
                                onError = { }
                            )
                            showAddDialog = false
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccineDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String) -> Unit
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
                OutlinedTextField(
                    value = vaccineName,
                    onValueChange = { vaccineName = it },
                    label = { Text("Vaccine Name *") },
                    placeholder = { Text("e.g., BCG, Polio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                ) {
                    OutlinedTextField(
                        value = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                        onValueChange = { },
                        label = { Text("Scheduled Date") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date",
                                tint = colorResource(R.color.soft_blue)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.Gray,
                            disabledTrailingIconColor = colorResource(R.color.soft_blue)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    placeholder = { Text("e.g., Nairobi Hospital") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    placeholder = { Text("Any additional information...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAdd(
                        vaccineName,
                        selectedDate.toString(),
                        location.ifEmpty { "Not specified" },
                        notes.ifEmpty { "" }
                    )
                },
                enabled = vaccineName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.soft_blue)
                )
            ) {
                Text("Add Vaccine", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
@Composable
fun SimpleCalendarView(
    vaccines: List<VaccineRecord>,
    onVaccineClick: (LocalDate, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    Column(modifier = modifier) {
        Row(
            modifier = Modifier .fillMaxWidth() .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically )
        { IconButton(
            onClick = { currentMonth = currentMonth.minusMonths(1) }
        ){ Text("◀", fontSize = 20.sp, color = colorResource(R.color.strong_pink))
        }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold )
                Text(
                    text = currentMonth.year.toString(),
                    fontSize = 14.sp,
                    color = Color.Gray ) }
            IconButton( onClick = { currentMonth = currentMonth.plusMonths(1) } )
            { Text("▶", fontSize = 20.sp, color = colorResource(R.color.soft_blue)) } }
        // Days of week
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach{ day ->
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f) ) } }
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
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else if (dayCounter <= daysInMonth) {
                        val currentDate = currentMonth.atDay(dayCounter)
                        val isToday = currentDate == today
                        val vaccineForDay = vaccines.find { it.getScheduledLocalDate() == currentDate }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    when {
                                        isToday -> colorResource(R.color.strong_pink)
                                        vaccineForDay != null && !vaccineForDay.isCompleted -> colorResource(R.color.soft_blue)
                                        vaccineForDay != null && vaccineForDay.isCompleted -> colorResource(R.color.grayish)
                                        else -> Color.Transparent
                                    },
                                    CircleShape
                                )
                                .clickable(enabled = vaccineForDay != null) {
                                    vaccineForDay?.let { v ->
                                        onVaccineClick(currentDate, v.name)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayCounter.toString(),
                                color = when {
                                    isToday || vaccineForDay != null -> Color.White
                                    else -> Color.Black
                                },
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                        dayCounter++
                    } else {
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
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