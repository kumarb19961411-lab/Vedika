package com.example.vedika.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DecoratorRegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    // Form State
    var businessName by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("3-7 Years") }
    val selectedExpertise = remember { mutableStateListOf("Floral Mandaps") }
    var tier1Price by remember { mutableStateOf("") }
    var tier1Inclusions by remember { mutableStateOf("") }
    var tier2Price by remember { mutableStateOf("") }
    var tier2Inclusions by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isFormValid = businessName.isNotBlank()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "KalyanaVedika",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2410C),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFFEA580C))
                    }
                },
                actions = {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBQ1wWi3MBXX7jN3HsXJ4-ypu-J3mfMRWplu2nwnwrLLgk1bSOBCHjITchqp1XdAkEShMQqytV4m3gI1xKP7CTzSLb2JKGtXOh_yxLdH3tnriCR0R7yTfFjJF6jbA7THtKSszZFou01OS37qaQbXlaayxUgCgxi_tIzmHHiIuInJPKwgxvYWsOPkKicL5gOYfXPbx-C501Q05Cf3kyA6YXvuuCdaeKVUv4q2-p1bLoX51SA3Tl8HY53RpcJDG7V91gAWyM6uFtCrNjQ",
                        contentDescription = "Profile",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.8f)
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().height(80.dp),
                color = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                shadowElevation = 16.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavIcon(icon = Icons.Default.GridView, label = "Explore")
                    BottomNavIcon(icon = Icons.Default.EventAvailable, label = "Bookings")
                    BottomNavIcon(icon = Icons.Default.Favorite, label = "Saved")
                    BottomNavIcon(icon = Icons.Default.Person, label = "Profile", isSelected = true)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            // Hero Header Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
                            Text(
                                "Partner Onboarding".uppercase(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                letterSpacing = 2.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Elevate Every Celebration",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Join our curated network of premium decorators. Transform spaces into memories for thousands of hosts across the platform.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 28.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 1: Business Identity
            ServiceSection(number = 1, title = "Business Identity", color = MaterialTheme.colorScheme.primary) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Business Name", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            placeholder = { Text("e.g. Royal Blooms Decor", color = MaterialTheme.colorScheme.outlineVariant) },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Years of Experience", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        OutlinedTextField(
                            value = experience,
                            onValueChange = { experience = it },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            trailingIcon = { Icon(Icons.Default.ExpandMore, contentDescription = null) },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            readOnly = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 2: Expertise Areas
            ServiceSection(number = 2, title = "Areas of Expertise", color = MaterialTheme.colorScheme.secondary, isTonal = true) {
                Column {
                    Text("Select all event types your team specializes in decorating.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val options = listOf(
                            Icons.Default.LocalFlorist to "Floral Mandaps",
                            Icons.Default.TheaterComedy to "Thematic Stages",
                            Icons.Default.Stairs to "Entry Decor",
                            Icons.Default.Celebration to "Reception Themes",
                            Icons.Default.WbSunny to "Outdoor Lighting"
                        )
                        options.forEach { (icon, label) ->
                            val isChecked = selectedExpertise.contains(label)
                            ExpertiseCard(
                                icon = icon, 
                                label = label, 
                                isChecked = isChecked,
                                modifier = Modifier.weight(1f).clickable {
                                    if (isChecked) selectedExpertise.remove(label)
                                    else selectedExpertise.add(label)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 3: Service Packages
            ServiceSection(number = 3, title = "Service Packages", color = MaterialTheme.colorScheme.tertiary) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Essential Tier
                        Surface(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Essential Tier".uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
                                Text("Base Setup", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = tier1Price,
                                    onValueChange = { tier1Price = it },
                                    leadingIcon = { Text("₹", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
                                    placeholder = { Text("Starts at", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        }

                        // Premium Tier
                        Surface(modifier = Modifier.weight(2f), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text("Premium Tier".uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                                Text("Signature Heritage", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = tier2Price,
                                    onValueChange = { tier2Price = it },
                                    leadingIcon = { Text("₹", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold) },
                                    placeholder = { Text("Premium Price", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = tier2Inclusions,
                                    onValueChange = { tier2Inclusions = it },
                                    placeholder = { Text("Inclusions (e.g. Fresh Marigolds, 40ft Mandap)", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth().height(80.dp),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Registration Complete! Welcome to Vedika.")
                        kotlinx.coroutines.delay(1500)
                        onNavigateToDashboard()
                    }
                }, 
                enabled = isFormValid,
                shape = CircleShape, 
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Complete Registration", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ServiceSection(number: Int, title: String, color: Color, isTonal: Boolean = false, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isTonal) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = color.copy(alpha = 0.1f)) {
                    Box(contentAlignment = Alignment.Center) { Text(number.toString(), fontWeight = FontWeight.Bold, color = color) }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
            content()
        }
    }
}

@Composable
fun ExpertiseCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isChecked: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(width = if (isChecked) 2.dp else 1.dp, color = if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, contentDescription = null, tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun BottomNavIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean = false) {
    val color = if (isSelected) Color(0xFF8F4E00) else Color(0xFF78716A)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color)
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = color)
    }
}
