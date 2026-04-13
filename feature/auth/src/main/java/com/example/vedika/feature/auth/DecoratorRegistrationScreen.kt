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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.ui.draw.drawBehind
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DecoratorRegistrationScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isFormValid = uiState.decoratorBusinessName.isNotBlank()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "KalyanaVedika",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF8F4E00), // Brown Saffron
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF8F4E00)
                        )
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
                            style = MaterialTheme.typography.displaySmall.copy(fontFamily = FontFamily.Serif),
                            color = Color(0xFF8F4E00),
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
                        RegistrationField(
                            label = "Business Name", 
                            placeholder = "e.g. Royal Blooms Decor",
                            value = uiState.decoratorBusinessName,
                            onValueChange = { viewModel.updateDecoratorBusinessName(it) }
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Years of Experience", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        OutlinedTextField(
                            value = uiState.decoratorExperience,
                            onValueChange = { viewModel.updateDecoratorExperience(it) },
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
                            val isChecked = uiState.decoratorExpertise.contains(label)
                            ExpertiseCard(
                                icon = icon, 
                                label = label, 
                                isChecked = isChecked,
                                modifier = Modifier.weight(1f).clickable {
                                    viewModel.updateDecoratorExpertise(label)
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
                                Text("ESSENTIAL TIER", style = MaterialTheme.typography.labelSmall, color = Color(0xFF8F4E00).copy(alpha = 0.6f), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Text("Base Setup", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = uiState.decoratorTier1Price,
                                    onValueChange = { viewModel.updateDecoratorTier1Price(it) },
                                    leadingIcon = { Text("₹", color = Color(0xFF8F4E00), fontWeight = FontWeight.Bold) },
                                    placeholder = { Text("Starts at", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        }

                        // Premium Tier
                        Surface(modifier = Modifier.weight(2f), shape = RoundedCornerShape(16.dp), color = Color(0xFFEDFDFD)) { // Light cyan-ish as per visual
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text("MOST POPULAR", style = MaterialTheme.typography.labelSmall, color = Color(0xFF0D9488), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                Text("Signature Heritage", style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.Serif), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = uiState.decoratorTier2Price,
                                    onValueChange = { viewModel.updateDecoratorTier2Price(it) },
                                    leadingIcon = { Text("Premium Price", fontSize = 12.sp, color = Color(0xFF0D9488)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = uiState.decoratorTier2Inclusions,
                                    onValueChange = { viewModel.updateDecoratorTier2Inclusions(it) },
                                    placeholder = { Text("List key inclusions (e.g. Fresh Marigolds, 40ft Mandap)", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth().height(80.dp),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 4: Portfolio Gallery
            ServiceSection(number = 4, title = "Portfolio Gallery", color = Color(0xFF8F4E00)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp))
                        .drawBehind {
                            drawRoundRect(
                                color = Color.Gray.copy(alpha = 0.5f),
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape,
                            color = Color(0xFF8F4E00).copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF8F4E00), modifier = Modifier.size(32.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Upload Work Samples", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Drag and drop high-resolution images of your best decor projects (Max 10 files, 5MB each).", 
                            style = MaterialTheme.typography.bodySmall, 
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { /* Browse */ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8F4E00))
                        ) {
                            Text("Browse Files")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Button(
                onClick = {
                    viewModel.saveRegistrationData {
                        scope.launch {
                            snackbarHostState.showSnackbar("Registration Complete! Welcome to Vedika.")
                            kotlinx.coroutines.delay(1500)
                            onNavigateToDashboard()
                        }
                    }
                }, 
                enabled = isFormValid,
                shape = CircleShape, 
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8F4E00),
                    contentColor = Color.White
                ),
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
                Text(title, style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.Serif), fontWeight = FontWeight.Bold)
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
