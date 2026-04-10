package com.example.vedika.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueRegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    // Form State
    var venueName by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var basePrice by remember { mutableStateOf("") }
    val selectedAmenities = remember { mutableStateListOf("Catering") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isFormValid = venueName.isNotBlank() && location.isNotBlank()

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
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF78716A))
                    }
                },
                actions = {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuA42USpVY4-8rz-iSPvU0t5daMoVhCaWjnFj7q92X-KwfX4eM1hAv1-RPXjzyEQVUdhatLI2fKcg6hyuedW5_dCPA1H7jTtW1vXggdTXKs9Uf8T7yfRzRx6phzFuLSmYdJhHu9F55HjUnKnFTzi9x4jKFLIgOMOmEg_Z8wwSXX3nM2rLVtHsPYIiW3ZTwYFZ-9ryY4gtKWb93TyiU_JnWTK8-OJ474iUb4MOPtit0fwAMBISqvA9lsjfuIL1KSC6YVX97xS-lTMivcU",
                        contentDescription = "Profile",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
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
                modifier = Modifier.fillMaxWidth(),
                color = Color.White.copy(alpha = 0.9f),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { 
                        scope.launch { 
                            snackbarHostState.showSnackbar("Draft saved successfully") 
                        } 
                    }) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Draft", fontWeight = FontWeight.SemiBold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "Skip", 
                            style = MaterialTheme.typography.bodyMedium, 
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickable { onNavigateToDashboard() }
                        )
                        Button(
                            onClick = onNavigateToDashboard,
                            enabled = isFormValid,
                            modifier = Modifier.height(56.dp).padding(start = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("Continue", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null)
                        }
                    }
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
            // Progress Stepper
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepItem(number = 1, label = "Venue Details", isActive = true)
                Box(modifier = Modifier.width(40.dp).height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
                StepItem(number = 2, label = "Pricing", isActive = false)
                Box(modifier = Modifier.width(40.dp).height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
                StepItem(number = 3, label = "Review", isActive = false)
            }

            // Hero Title
            Text(
                text = "Host your heritage venue",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Step 1: Tell us about the heart of your celebrations.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Form Content
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                
                // General Info Bento Section
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                            RegistrationField(
                                label = "Venue Identity", 
                                placeholder = "The Royal Orchid Garden",
                                value = venueName,
                                onValueChange = { venueName = it }
                            )
                            RegistrationField(
                                label = "Guest Capacity", 
                                placeholder = "Number of guests", 
                                icon = Icons.Default.Groups, 
                                keyboardType = KeyboardType.Number,
                                value = capacity,
                                onValueChange = { capacity = it }
                            )
                        }
                    }
                    
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            RegistrationField(
                                label = "Location", 
                                placeholder = "Street address, City, State", 
                                actionIcon = Icons.Default.Map,
                                value = location,
                                onValueChange = { location = it }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AsyncImage(
                                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBdcaJxP1V6Q5uLYCpXK-g0fAAZOPtOT-g2m_jX00O14H-r2pfWIyPuDr0vvCik_8cNesFcDyiR3GtBd31gdQy1-JIFeVLXx7klOm_8YvisZz7vWqIkz7xl1P0QbNPm4aWL8Ji8aJnj8oAqhNBS9awa-0244U4_a5z4BKtjaoZBamXmPyd9q34uOvOBkRQxDjZY6jLSS772Z7zDQqmK7L2mgpoMhT8GLlDVfenirBgjCvr9hh234onaAhzhppxylmUENesj75r60qfv",
                                contentDescription = "Map Preview",
                                modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)).alpha(0.6f),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // Amenities
                Text(
                    text = "Premium Amenities",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val amenities = listOf(
                        Icons.Default.AcUnit to "Central AC",
                        Icons.Default.Restaurant to "Catering",
                        Icons.Default.DirectionsCar to "Valet",
                        Icons.Default.NightShelter to "Guest Rooms",
                        Icons.Default.CameraEnhance to "Stage Setup",
                        Icons.Default.Wifi to "Free Wi-Fi",
                        Icons.Default.LocalFlorist to "Decoration",
                        Icons.Default.Bolt to "Backup Power"
                    )
                    
                    amenities.forEach { (icon, label) ->
                        AmenityChip(
                            icon = icon, 
                            label = label, 
                            isSelected = selectedAmenities.contains(label),
                            onClick = {
                                if (selectedAmenities.contains(label)) selectedAmenities.remove(label)
                                else selectedAmenities.add(label)
                            }
                        )
                    }
                }

                // Pricing Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Text("Standard Pricing", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Base price per day for typical event bookings.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedTextField(
                                value = basePrice,
                                onValueChange = { basePrice = it },
                                placeholder = { Text("50,000", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                                leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = Color.Gray) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    unfocusedBorderColor = Color.Transparent
                                )
                            )
                            Text("/ Day", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Photos
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Venue Showcase", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Max 4 Photos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    repeat(4) { index ->
                        Surface(
                            modifier = Modifier.weight(1f).aspectRatio(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            border = BorderStroke(2.dp, Brush.linearGradient(listOf(MaterialTheme.colorScheme.outlineVariant, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (index == 0) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Text("Add Cover", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                                    }
                                } else {
                                    Icon(Icons.Default.Image, contentDescription = null, tint = Color.LightGray)
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "* High-quality photos of the main hall, outdoor areas, and amenities increase booking chances by 45%.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(0.9f)
                )
                
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
fun StepItem(number: Int, label: String, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest,
            border = if (isActive) BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontSize = 9.sp
        )
    }
}

@Composable
fun RegistrationField(
    label: String,
    placeholder: String,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Box(contentAlignment = Alignment.CenterEnd) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedBorderColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = Color.LightGray, modifier = Modifier.padding(end = 16.dp))
            }
            if (actionIcon != null) {
                Surface(
                    modifier = Modifier.padding(end = 8.dp).size(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(actionIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AmenityChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector, 
    label: String, 
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Text(label, style = MaterialTheme.typography.labelLarge, color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}
