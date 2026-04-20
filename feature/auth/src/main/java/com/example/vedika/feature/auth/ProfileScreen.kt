package com.example.vedika.feature.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.vedika.core.design.components.VedikaTabTopAppBar
import com.example.vedika.core.design.theme.NotoSerif
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    appVersion: String = "",
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val vendor = state.vendorProfile
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceColor = MaterialTheme.colorScheme.background

    Scaffold(
        topBar = {
            VedikaTabTopAppBar(title = "Partner Command")
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = surfaceColor
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // High Impact Header Section
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Box(modifier = Modifier.padding(24.dp)) {
                    // Background Mandala Motif (Mocked with Icon)
                    Icon(
                        imageVector = Icons.Default.SettingsAccessibility,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 24.dp, y = (-24).dp)
                            .rotate(15f),
                        tint = primaryColor.copy(alpha = 0.05f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Business Image / Avatar
                        Box {
                            Surface(
                                modifier = Modifier
                                    .size(112.dp)
                                    .rotate(-3f),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shadowElevation = 8.dp
                            ) {
                                AsyncImage(
                                    model = vendor?.coverImage ?: "https://images.unsplash.com/photo-1519167758481-83f550bb49b3",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }
                            
                            // Verified Badge
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 8.dp, y = 8.dp),
                                shape = CircleShape,
                                color = secondaryColor,
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("VERIFIED", color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Column {
                            Text(
                                "SETTINGS • ${vendor?.ownerName?.uppercase() ?: "PARTNER"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = primaryColor,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Text(
                                vendor?.businessName ?: "Luxe Heritage Partner",
                                style = MaterialTheme.typography.headlineMedium,
                                fontFamily = NotoSerif,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 32.sp
                            )
                            
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = secondaryColor, modifier = Modifier.size(16.dp))
                                Text(
                                    vendor?.location ?: "Registered Location",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bento Grid Navigation
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    BentoCard(
                        modifier = Modifier.weight(1f),
                        title = "Profile Details",
                        subtitle = "Manage business info",
                        icon = Icons.Default.Person,
                        iconContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        iconColor = MaterialTheme.colorScheme.primary,
                        onAction = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Profile editing coming in Phase 3")
                            }
                        }
                    )
                    BentoCard(
                        modifier = Modifier.weight(1f),
                        title = "Portfolio",
                        subtitle = "Curate your showcase",
                        icon = Icons.Default.Collections,
                        iconContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                        iconColor = MaterialTheme.colorScheme.secondary,
                        onAction = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Portfolio management coming in Phase 3")
                            }
                        }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    BentoCard(
                        modifier = Modifier.weight(1f),
                        title = "My Bookings",
                        subtitle = "Review reservations",
                        icon = Icons.Default.BookOnline,
                        iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.2f),
                        iconColor = MaterialTheme.colorScheme.tertiary,
                        onAction = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Booking history coming in Phase 3")
                            }
                        }
                    )
                    BentoCard(
                        modifier = Modifier.weight(1f),
                        title = "Help & Support",
                        subtitle = "FAQs & Assistance",
                        icon = Icons.Default.SupportAgent,
                        iconContainerColor = Color.Gray.copy(alpha = 0.1f),
                        iconColor = Color.DarkGray,
                        onAction = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Support portal coming in Phase 3")
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Actions
            Button(
                onClick = { viewModel.logout(onLogout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }

            Text(
                text = if (appVersion.isNotBlank()) "Version $appVersion • \u00A9 2025 KalyanaVedika"
                       else "KalyanaVedika Vendor Suite",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(80.dp)) // Bottom nav space
        }
    }
}

@Composable
private fun BentoCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconContainerColor: Color,
    iconColor: Color,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        onClick = onAction
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconContainerColor
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    tint = iconColor
                )
            }
            
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
