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
import kotlinx.coroutines.launch
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.design.components.VedikaTabTopAppBar
import com.example.vedika.core.design.theme.NotoSerif
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onNavigateToNewBooking: (Long?) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val blockedColor = MaterialTheme.colorScheme.error
    val surfaceColor = MaterialTheme.colorScheme.background

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            VedikaTabTopAppBar(
                title = "Heritage Calendar",
                actions = {
                    IconButton(onClick = {
                        val epoch = state.selectedDate?.atStartOfDay(java.time.ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                        onNavigateToNewBooking(epoch)
                    }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Booking", tint = primaryColor)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = surfaceColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        "BOOKING CALENDAR",
                        style = MaterialTheme.typography.labelSmall,
                        color = secondaryColor,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = YearMonth.of(state.selectedYear, state.selectedMonth).month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " ${state.selectedYear}",
                        style = MaterialTheme.typography.displaySmall,
                        fontFamily = NotoSerif,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Row {
                    IconButton(onClick = {
                        val prev = YearMonth.of(state.selectedYear, state.selectedMonth).minusMonths(1)
                        viewModel.onMonthChange(prev.monthValue, prev.year)
                    }) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
                    }
                    IconButton(onClick = {
                        val next = YearMonth.of(state.selectedYear, state.selectedMonth).plusMonths(1)
                        viewModel.onMonthChange(next.monthValue, next.year)
                    }) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Status Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LegendItem(label = "Available", color = Color.LightGray.copy(alpha = 0.4f))
                LegendItem(label = "Booked", color = primaryColor)
                LegendItem(label = "Partial/Pending", color = Color(0xFFFF9933)) // Heritage Orange
                LegendItem(label = "Blocked", color = blockedColor)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Calendar Grid UI
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                val yearMonth = YearMonth.of(state.selectedYear, state.selectedMonth)
                val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value
                val paddingDays = (firstDayOfMonth - 1) % 7
                val daysInMonth = yearMonth.lengthOfMonth()
                val totalCells = paddingDays + daysInMonth
                val rows = (totalCells + 6) / 7

                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                            Text(day, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    repeat(rows) { rowIndex ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            repeat(7) { dayIndex ->
                                val cellIndex = rowIndex * 7 + dayIndex
                                val dayNum = cellIndex - paddingDays + 1
                                
                                if (dayNum in 1..daysInMonth) {
                                    val date = yearMonth.atDay(dayNum)
                                    val dayState = state.monthCalendar[date]
                                    
                                    CalendarDayItem(
                                        day = dayNum.toString(),
                                        dayState = dayState,
                                        isSelected = state.selectedDate == date,
                                        primaryColor = primaryColor,
                                        blockedColor = blockedColor,
                                        onClick = {
                                            viewModel.onDateClick(date)
                                            showBottomSheet = true
                                        }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(40.dp))
                                }
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
                            val localDate = java.time.Instant.ofEpochMilli(booking.eventDate)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.onDateClick(localDate)
                            showBottomSheet = true
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Day Detail Bottom Sheet
        if (showBottomSheet && state.selectedDate != null) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.LightGray) }
            ) {
                DayDetailContent(
                    date = state.selectedDate!!,
                    dayState = state.monthCalendar[state.selectedDate],
                    onConfirmBooking = { id -> viewModel.confirmBooking(id) },
                    onCancelBooking = { id -> viewModel.cancelBooking(id) },
                    onBlockDate = { date -> viewModel.blockDate(date, "Manual Block") },
                    onUnblockDate = { date -> viewModel.unblockDate(date) },
                    onAddBooking = {
                        val epoch = state.selectedDate?.atStartOfDay(java.time.ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            showBottomSheet = false
                            onNavigateToNewBooking(epoch)
                        }
                    },
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
    dayState: com.example.vedika.core.data.model.CalendarDayState?,
    isSelected: Boolean,
    primaryColor: Color,
    blockedColor: Color,
    onClick: () -> Unit
) {
    val pendingColor = Color(0xFFFF9933) // Saffron/Orange
    val status = dayState?.status ?: com.example.vedika.core.data.model.DayAvailabilityStatus.AVAILABLE

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
                            com.example.vedika.core.data.model.DayAvailabilityStatus.BOOKED -> primaryColor
                            com.example.vedika.core.data.model.DayAvailabilityStatus.PENDING, 
                            com.example.vedika.core.data.model.DayAvailabilityStatus.LIMITED -> pendingColor
                            com.example.vedika.core.data.model.DayAvailabilityStatus.BLOCKED -> blockedColor
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
            Surface(modifier = Modifier.size(44.dp), shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)) {
                Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.padding(10.dp), tint = MaterialTheme.colorScheme.primary)
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
fun DayDetailContent(
    date: LocalDate,
    dayState: com.example.vedika.core.data.model.CalendarDayState?,
    onConfirmBooking: (String) -> Unit,
    onCancelBooking: (String) -> Unit,
    onBlockDate: (LocalDate) -> Unit,
    onUnblockDate: (LocalDate) -> Unit,
    onAddBooking: () -> Unit,
    onClose: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
    val isBlocked = dayState?.status == com.example.vedika.core.data.model.DayAvailabilityStatus.BLOCKED && dayState.manualBlocks.isNotEmpty()

    Column(modifier = Modifier.padding(24.dp).padding(bottom = 32.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("Day Details", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                Text(date.format(formatter), style = MaterialTheme.typography.headlineSmall, fontFamily = NotoSerif, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        // Capacity/Occupancy Summary
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surfaceContainer) {
            Column(modifier = Modifier.padding(16.dp)) {
                dayState?.venueOccupancy?.let { occ ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OccupancyChip(label = "Morning", isBooked = occ.morningBooked)
                        Spacer(modifier = Modifier.width(8.dp))
                        OccupancyChip(label = "Evening", isBooked = occ.eveningBooked)
                    }
                } ?: dayState?.decoratorCapacity?.let { cap ->
                    Text(
                        text = "Capacity: ${cap.busyCrew} / ${cap.totalCrew} crews busy",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                } ?: run {
                    Text("No active occupancy scheduled", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bookings List
        if (dayState?.bookings?.isNotEmpty() == true) {
            Text("Bookings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            dayState.bookings.forEach { booking ->
                BookingItem(booking, onConfirm = { onConfirmBooking(booking.id) }, onCancel = { onCancelBooking(booking.id) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text("No bookings for this date", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, fontStyle = FontStyle.Italic)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Actions
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (isBlocked) {
                Button(
                    onClick = { onUnblockDate(date) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Unblock Date")
                }
            } else {
                val hasConfirmed = dayState?.bookings?.any { it.status == com.example.vedika.core.data.model.BookingStatus.CONFIRMED } == true
                OutlinedButton(
                    onClick = { onBlockDate(date) },
                    modifier = Modifier.weight(1f).height(56.dp),
                    enabled = !hasConfirmed,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Block Date")
                }
            }
            
            Button(
                onClick = onAddBooking,
                enabled = !isBlocked,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Booking")
            }
        }
    }
}

@Composable
private fun OccupancyChip(label: String, isBooked: Boolean) {
    val color = if (isBooked) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.3f)
    val textColor = if (isBooked) MaterialTheme.colorScheme.onPrimary else Color.Gray
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun BookingItem(booking: Booking, onConfirm: () -> Unit, onCancel: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(booking.customerName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(booking.status.name, style = MaterialTheme.typography.labelSmall, color = if (booking.status == com.example.vedika.core.data.model.BookingStatus.CONFIRMED) MaterialTheme.colorScheme.primary else Color.Gray)
            }
            if (booking.status == com.example.vedika.core.data.model.BookingStatus.PENDING) {
                TextButton(onClick = onConfirm) { Text("Confirm", color = MaterialTheme.colorScheme.primary) }
                TextButton(onClick = onCancel) { Text("Cancel", color = MaterialTheme.colorScheme.error) }
            } else if (booking.status == com.example.vedika.core.data.model.BookingStatus.CONFIRMED) {
                TextButton(onClick = onCancel) { Text("Cancel", color = Color.Gray) }
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
