package com.example.vedika.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.TempleHindu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen(
    onNavigateToVenueRegistration: () -> Unit,
    onNavigateToDecoratorRegistration: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "KalyanaVedika",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFC2410C), // Matching Tailwind orange-700
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFFEA580C))
                    }
                },
                actions = {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDDlTdTEHOAwAse-lMeAoY89KT2saIGqA1A9D-i6m4DLYXLTRgr5pcSGIKKgbJza640ftyEHA1jYsaJ50RT-md_7z0keb9eDHBEtY4Q9J6hDkLzF7YPp-Lc2AhKHcwj4Lis_oXPkzorYF8XZLELlymm412aynEBY22C05Bx6uG9qDcRJqDGlajvfYpCVK4Tkemynns65uDuS-MeOLGmnvnDnp5U_65_aOd39U2Qtwi9UVHv8n4W9w67PGLkVTdAgNqqakjj2UREj1fE",
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
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().height(96.dp),
                color = MaterialTheme.colorScheme.white.copy(alpha = 0.9f),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Selected Category",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = selectedCategory ?: "None",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            when (selectedCategory) {
                                "Venues" -> onNavigateToVenueRegistration()
                                "Decorators" -> onNavigateToDecoratorRegistration()
                                else -> { /* No selection */ }
                            }
                        },
                        enabled = selectedCategory != null,
                        modifier = Modifier.height(56.dp).padding(start = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text("Proceed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Mandala Watermark (Mocked)
            Icon(
                Icons.Default.SettingsSuggest,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(400.dp)
                    .alpha(0.03f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp)
            ) {
                // Hero Section
                Text(
                    text = "Step 1: Selection".uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = buildAnnotatedString {
                        append("What service are you ")
                        withStyle(SpanStyle(fontStyle = FontStyle.Normal)) {
                            append("\n")
                        }
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontStyle = FontStyle.Italic)) {
                            append("looking for today?")
                        }
                    },
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 44.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose a category to begin planning your auspicious occasion. We currently offer Venues and Decorations.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 28.sp,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Bento Grid
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    // Category: Venues (Bento Large)
                    CategoryCard(
                        title = "Venues",
                        description = "Find the perfect hall or garden for your celebration.",
                        icon = Icons.Default.TempleHindu,
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCrEryDF8by3op87O1Kc9ZbPZ7DSBbjwg7JrW6oUcy4b_HpmyfcCW61lo-bj-_ZoAhCLviMRmqztCuIjZVUsGY50R52v1o_5oL3vV9JSa09NdvsSWLk3WpRnOb9pBQAzBANovUrHypphxUcgneLPTCugFdqoiDHwD8pYdehCe9va-S9yruvUZOf7YY7yIr38Tfj1xw6mzklF0Bxqq3sYGjTewD2xsWoZstvIBgug1I-JF4VAbG3AO1EUiP83GAK3Iyatzn_EY9YlxO5",
                        isSelected = selectedCategory == "Venues",
                        onSelect = { selectedCategory = "Venues" },
                        isLarge = true
                    )

                    // Category: Decorators
                    CategoryCard(
                        title = "Decorators",
                        description = "Traditional mandaps to modern stage themes.",
                        icon = Icons.Default.FormatPaint,
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB0HDO1fFrPrvjzIJufoCMz4w7MDu29c9BAPELPdECs3WseuMGNXwHXk3Pzp-9EuZhfn8V-z6n44Mhy02AmrxBudFSp_iPxeHubvlIRF3sIbD6XAtbaE4knIDZN5V32-VyKmBzQJ1Bu-hqmxg2CBPJZn0SzlTIqUZdvXchyVpN1oh_4Qahtx4xUUhUVZW2N859HSSya95aO_-SOTziClIzrzOQYRcp9Erwq_a6Avk-GHZqUWdiCAsKOKTKUJVhDt0MqxYFAWvAlrK2X",
                        isSelected = selectedCategory == "Decorators",
                        onSelect = { selectedCategory = "Decorators" },
                        infoText = "89 Available"
                    )

                    // Disabled Category Row
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        DisabledCategoryCard(
                            title = "Catering",
                            icon = Icons.Default.Restaurant,
                            modifier = Modifier.weight(1f)
                        )
                        DisabledCategoryCard(
                            title = "Photography",
                            icon = Icons.Default.PhotoCamera,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    DisabledCategoryCard(
                        title = "Priests",
                        icon = Icons.Default.Person,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Secondary disabled categories row
                Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DisabledChip(icon = Icons.Default.FaceRetouchingNatural, label = "Makeup & Styling")
                    DisabledChip(icon = Icons.Default.MusicNote, label = "Music & DJ")
                }
                
                Spacer(modifier = Modifier.height(120.dp)) // Padding for bottom bar
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    imageUrl: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    isLarge: Boolean = false,
    infoText: String? = null
) {
    Surface(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (isLarge) 280.dp else 200.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 0.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
        ),
        shadowElevation = if (isSelected) 8.dp else 2.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier
                    .padding(if (isLarge) 32.dp else 24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Surface(
                        modifier = Modifier.size(if (isLarge) 56.dp else 48.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(if (isLarge) 36.dp else 24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(if (isLarge) 24.dp else 16.dp))
                    Text(
                        text = title,
                        style = if (isLarge) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = infoText ?: "Register your ${title.lowercase().removeSuffix("s")}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(if (isLarge) 24.dp else 16.dp)
                )
            }
        }
    }
}

@Composable
fun DisabledCategoryCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(140.dp).alpha(0.6f),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.Center) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color(0xFFE7E5E4) // Stone 200
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Color(0xFF78716A), // Stone 500
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF44403C) // Stone 700
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = CircleShape
            ) {
                Text(
                    text = "Coming Soon",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF78716A),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DisabledChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(
        modifier = Modifier.alpha(0.5f),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF78716A))
            Text(label, style = MaterialTheme.typography.labelLarge, color = Color(0xFF44403C), fontWeight = FontWeight.Bold)
            Surface(
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFFD6D3D1)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Soon",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFA8A29E),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Extension to MaterialTheme for cleaner color access
val ColorScheme.white: Color get() = Color.White
