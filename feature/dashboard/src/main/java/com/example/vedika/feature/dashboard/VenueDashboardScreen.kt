package com.example.vedika.feature.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.design.components.VedikaTabTopAppBar
import com.example.vedika.core.design.theme.NotoSerif
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VenueDashboardScreen(
    state: DashboardUiState,
    onNavigateToNewBooking: () -> Unit,
    onNavigateToInventoryHub: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceColor = MaterialTheme.colorScheme.background

    Scaffold(
        topBar = {
            VedikaTabTopAppBar(title = "Venue Command")
        },
        containerColor = surfaceColor,
        modifier = modifier
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Hero Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    AsyncImage(
                        model = if (!state.coverImage.isNullOrEmpty()) state.coverImage else "https://images.unsplash.com/photo-1519167758481-83f550bb49b3",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                )
                            )
                    )
                    
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                            .padding(bottom = 32.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "FEATURED VENUE",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Namaste, ${state.vendorName.ifBlank { "Partner" }}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = state.venueName ?: state.businessName,
                            style = MaterialTheme.typography.headlineLarge,
                            fontFamily = NotoSerif,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 40.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = state.location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                // Stats & Action Bar (Overlapping)
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-24).dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(label = "Capacity", value = if (!state.capacity.isNullOrBlank()) state.capacity!! else "1200 Guests", color = primaryColor)
                        VerticalDivider(modifier = Modifier.height(32.dp).width(1.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        StatItem(label = "Area", value = if (!state.area.isNullOrBlank()) state.area!! else "15,000 Sq Ft", color = primaryColor)
                        VerticalDivider(modifier = Modifier.height(32.dp).width(1.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        StatItem(label = "Type", value = if (!state.venueType.isNullOrBlank()) state.venueType!! else "Indoor/Outdoor", color = primaryColor)
                    }
                }

                // Main Content
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    AnalyticsSection(state)

                    Spacer(modifier = Modifier.height(32.dp))

                    SectionTitle(title = "Visual Heritage", actionText = "Manage Photos")
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1519167758481-83f550bb49b3",
                            contentDescription = null,
                            modifier = Modifier.weight(1f).aspectRatio(4/3f).clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1469334031218-e382a71b716b",
                            contentDescription = null,
                            modifier = Modifier.weight(1f).aspectRatio(4/3f).clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    SectionTitle(title = "Luxury Amenities")
                    Column(modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AmenityCard(icon = Icons.Default.KingBed, title = "Bridal Suite", subtitle = "Private AC sanctuary", color = primaryColor, modifier = Modifier.weight(1f))
                            AmenityCard(icon = Icons.Default.Restaurant, title = "Gourmet Kitchen", subtitle = "Industrial equipment", color = secondaryColor, modifier = Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AmenityCard(icon = Icons.Default.AcUnit, title = "Central AC", subtitle = "Climate control", color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.weight(1f))
                            Surface(
                                modifier = Modifier.weight(1f).height(72.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Transparent,
                                border = BorderStroke(2.dp, primaryColor.copy(alpha = 0.2f)),
                                onClick = {}
                            ) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = primaryColor)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Add Amenity", style = MaterialTheme.typography.titleSmall, color = primaryColor, fontWeight = FontWeight.Bold)
                                        Text("Expand listing", style = MaterialTheme.typography.bodySmall, color = primaryColor.copy(alpha = 0.6f))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    SectionTitle(title = "Upcoming Bookings", actionText = "View Calendar")
                    Column(modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (state.upcomingBookings.isNotEmpty()) {
                            state.upcomingBookings.take(2).forEach { booking ->
                                val dateStr = SimpleDateFormat("dd", Locale.getDefault()).format(Date(booking.eventDate))
                                val monthStr = SimpleDateFormat("MMM", Locale.getDefault()).format(Date(booking.eventDate))
                                UpcomingBookingItem(
                                    date = dateStr,
                                    month = monthStr,
                                    title = booking.customerName,
                                    client = booking.customerName,
                                    type = "Wedding Event",
                                    status = if (booking.status == BookingStatus.CONFIRMED) "Confirmed" else "Pending"
                                )
                            }
                        } else {
                            UpcomingBookingItem(date = "12", month = "Nov", title = "Sharma Engagement", client = "Ravi Sharma", type = "Engagement", status = "Confirmed")
                            UpcomingBookingItem(date = "28", month = "Nov", title = "Tech Corporate Meet", client = "Infosys Ltd.", type = "Corporate", status = "Pending Payment")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onNavigateToNewBooking,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Create New Booking", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column {
        Text(text = label.uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontFamily = NotoSerif, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun SectionTitle(title: String, actionText: String? = null) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall, fontFamily = NotoSerif, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurface)
        if (actionText != null) {
            Text(text = actionText, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AmenityCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun UpcomingBookingItem(date: String, month: String, title: String, client: String, type: String, status: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .width(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(month.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Client: $client", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Surface(
                shape = CircleShape,
                color = if (status.contains("Confirmed")) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = if (status.contains("Confirmed")) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Text(status.uppercase(), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
private fun AnalyticsSection(state: DashboardUiState) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Owner Insights", style = MaterialTheme.typography.titleLarge, fontFamily = NotoSerif)
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InsightItem(icon = Icons.Default.TrendingUp, label = "Profile Views", value = state.analyticsSummary?.get("views") ?: "1,284", color = MaterialTheme.colorScheme.secondary)
                InsightItem(icon = Icons.Default.PendingActions, label = "Leads", value = state.leadsCount ?: "12 New", color = MaterialTheme.colorScheme.primary)
                InsightItem(icon = Icons.Default.Star, label = "Rating", value = state.rating ?: "4.8 (212)", color = MaterialTheme.colorScheme.tertiary)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "\"Your listing is performing 20% better than average halls in your area.\"",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = Color.DarkGray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Full Analytics", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun InsightItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}
