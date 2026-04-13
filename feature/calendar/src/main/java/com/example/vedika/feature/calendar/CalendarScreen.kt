package com.example.vedika.feature.calendar

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.design.theme.NotoSerif
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedBooking by remember { mutableStateOf<Booking?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF8F4E00)
    val secondaryColor = Color(0xFF006A6A)
    val blockedColor = Color(0xFFBA1A1A)
    val surfaceColor = Color(0xFFFFF8EF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "KalyanaVedika",
                        fontFamily = NotoSerif,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513)
                    )
                },
                actions = {
                    IconButton(onClick = { /* Add booking */ }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add", tint = primaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White.copy(alpha = 0.8f))
            )
        },
        containerColor = surfaceColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Header
            Text(
                "BOOKING CALENDAR",
                style = MaterialTheme.typography.labelSmall,
                color = secondaryColor,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Text(
                "November 2024",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = NotoSerif,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Status Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LegendItem(label = "Available", color = Color.LightGray.copy(alpha = 0.4f))
                LegendItem(label = "Booked", color = primaryColor)
                LegendItem(label = "Blocked", color = blockedColor)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Calendar Grid UI (Mocked for Visuals)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Weekdays
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        "MTWTFSS".forEach { day ->
                            Text(day.toString(), style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Grid Rows (Mocked 5 weeks)
                    repeat(5) { weekIndex ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            repeat(7) { dayIndex ->
                                val dayNum = (weekIndex * 7 + dayIndex + 1) % 31
                                CalendarDayItem(
                                    day = if (dayNum == 0) "31" else dayNum.toString(),
                                    status = when {
                                        dayNum == 12 || dayNum == 28 -> "Booked"
                                        dayNum == 15 -> "Blocked"
                                        else -> "Available"
                                    },
                                    isSelected = dayNum == 12,
                                    primaryColor = primaryColor,
                                    blockedColor = blockedColor,
                                    onClick = {
                                        if (dayNum == 12) {
                                            selectedBooking = state.allBookings.firstOrNull()
                                            showBottomSheet = true
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Booking List Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Today's Schedule", style = MaterialTheme.typography.titleLarge, fontFamily = NotoSerif, fontWeight = FontWeight.Bold)
                Text("See All", style = MaterialTheme.typography.labelLarge, color = secondaryColor, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Booking List
            if (state.allBookings.isEmpty()) {
                EmptyCalendarState()
            } else {
                state.allBookings.take(2).forEach { booking ->
                    BookingLogCard(
                        booking = booking,
                        onClick = {
                            selectedBooking = booking
                            showBottomSheet = true
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Booking Detail Bottom Sheet
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }
            ) {
                BookingDetailContent(
                    booking = selectedBooking,
                    onClose = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    day: String,
    status: String,
    isSelected: Boolean,
    primaryColor: Color,
    blockedColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isSelected) primaryColor.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(day, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(
                        when (status) {
                            "Booked" -> primaryColor
                            "Blocked" -> blockedColor
                            else -> Color.Transparent
                        }
                    )
            )
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun BookingLogCard(booking: Booking, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(44.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFF8F4E00).copy(alpha = 0.05f)) {
                Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.padding(10.dp), tint = Color(0xFF8F4E00))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(booking.customerName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Wedding Event • 4:00 PM", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun BookingDetailContent(booking: Booking?, onClose: () -> Unit) {
    Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Booking Details", style = MaterialTheme.typography.headlineSmall, fontFamily = NotoSerif, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color(0xFFF5EDDE)) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(booking?.customerName ?: "Ravi Sharma", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Sharma Engagement Party", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                Surface(color = Color(0xFF006A6A), shape = CircleShape) {
                    Text("CONFIRMED", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        DetailField(icon = Icons.Default.Event, label = "Event Date", value = "Tuesday, November 12, 2024")
        DetailField(icon = Icons.Default.Schedule, label = "Time Slot", value = "04:00 PM - 11:00 PM")
        DetailField(icon = Icons.Default.Group, label = "Guest Count", value = "450 Guests")
        DetailField(icon = Icons.Default.CurrencyRupee, label = "Total Amount", value = "₹2,50,000")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Message, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chat")
            }
            Button(
                onClick = {},
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006A6A))
            ) {
                Icon(Icons.Default.Call, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Call Client")
            }
        }
    }
}

@Composable
private fun DetailField(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun EmptyCalendarState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.EventBusy,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Bookings Found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Share your profile to start receiving inquiries.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
