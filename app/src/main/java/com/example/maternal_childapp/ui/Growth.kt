package com.example.maternal_childapp.ui

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GrowthScreenRoute(
    onBack: () -> Unit,
    onAddMeasurement: () -> Unit = {}
) {
    val vm: GrowthViewModel = viewModel()
    val state = vm.uiState

    GrowthScreen(
        childName = state.childName,
        childAgeLabel = state.childAgeLabel,
        latestWeightKg = state.latestWeightKg,
        latestHeightCm = state.latestHeightCm,
        dateOfBirth = state.dateOfBirth,
        history = state.history,
        children = state.children,
        selectedChildId = state.selectedChildId,
        onBack = onBack,
        onAddMeasurement = onAddMeasurement,
        onChildSelected = { id -> vm.onChildSelected(id) },
        isLoading = state.isLoading,
        error = state.error
    )
}

@Composable
fun GrowthScreen(
    childName: String,
    childAgeLabel: String,
    dateOfBirth : String?,
    latestWeightKg: Double?,
    latestHeightCm: Double?,
    history: List<GrowthRecord>,
    children: List<ChildOption>,
    selectedChildId: String?,
    onBack: () -> Unit,
    onAddMeasurement: () -> Unit,
    onChildSelected: (String) -> Unit,
    isLoading: Boolean,
    error: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFDFEB),
                        Color(0xFFE5D4FF),
                        Color(0xFFBFD9FF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar: back, title, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Growth",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onAddMeasurement) {
                    Icon(Icons.Filled.Add, contentDescription = "Add measurement")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ChildSelectorCard(
                childName = childName,
                ageLabel = childAgeLabel,
                dateOfBirth = dateOfBirth,
                children = children,
                selectedChildId = selectedChildId,
                onChildSelected = onChildSelected
            )
            Spacer(modifier = Modifier.height(16.dp))

            LatestMeasurementsCard(
                weightKg = latestWeightKg,
                heightCm = latestHeightCm
            )
            Spacer(modifier = Modifier.height(16.dp))

            HistoryCard(history = history)

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = error, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ChildSelectorCard(
    childName: String,
    ageLabel: String,
    dateOfBirth: String?,
    children: List<ChildOption>,
    selectedChildId: String?,
    onChildSelected: (String) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .clickable(enabled = children.size > 1) { if (children.size > 1) menuExpanded = true }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC7DE)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = childName.firstOrNull()?.uppercase() ?: "",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = childName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = ageLabel,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }

                if (children.size > 1) {
                    Text(
                        text = "Change",
                        color = Color(0xFF7C4DFF),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Dropdown with children list (only if more than one child)
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                children.forEach { child ->
                    DropdownMenuItem(
                        text = { Text(child.name) },
                        onClick = {
                            menuExpanded = false
                            onChildSelected(child.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LatestMeasurementsCard(weightKg: Double?, heightCm: Double?) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Latest Measurements",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Weight", color = Color.Gray)
                    Text(
                        text = if (weightKg != null) "${"%.1f".format(weightKg)} kg" else "--",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Height", color = Color.Gray)
                    Text(
                        text = if (heightCm != null) "${heightCm.toInt()} cm" else "--",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryCard(history: List<GrowthRecord>) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "History",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (history.isEmpty()) {
                Text("No records yet", color = Color.Gray)
            } else {
                history.forEach { record ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                record.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(record.date, color = Color.Gray)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Weight: ${"%.1f".format(record.weightKg)} kg, " +
                                        "Height: ${record.heightCm.toInt()} cm"
                            )
                        }
                    }
                }
            }
        }
    }
}
