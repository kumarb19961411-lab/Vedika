package com.example.vedika.feature.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

    Scaffold(
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
            // High fidelity bottom nav replica as requested for this screen specifically
            Surface(
                modifier = Modifier.fillMaxWidth().height(80.dp),
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                tonalElevation = 8.dp,
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Help */ },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 80.dp) // Adjust for bottom nav
            ) {
                Icon(Icons.Default.SupportAgent, contentDescription = "Support")
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
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
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
                    Icon(
                        Icons.Default.FilterVintage,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 12.dp, y = (-12).dp)
                            .size(100.dp)
                            .alpha(0.05f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 1: Business Identity
            ServiceSection(
                number = 1,
                title = "Business Identity",
                color = MaterialTheme.colorScheme.primary
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Business Name", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
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
                            value = "3-7 Years",
                            onValueChange = {},
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
            ServiceSection(
                number = 2,
                title = "Areas of Expertise",
                color = MaterialTheme.colorScheme.secondary,
                isTonal = true
            ) {
                Column {
                    Text("Select all event types your team specializes in decorating.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ExpertiseCard(icon = Icons.Default.Favorite, label = "Marriage", isChecked = true, modifier = Modifier.weight(1f))
                        ExpertiseCard(icon = Icons.Default.MusicNote, label = "Sangeeth", isChecked = true, modifier = Modifier.weight(1f))
                        ExpertiseCard(icon = Icons.Default.Eco, label = "Haldi", isChecked = false, modifier = Modifier.weight(1f))
                        ExpertiseCard(icon = Icons.Default.Cake, label = "Reception", isChecked = false, modifier = Modifier.weight(1f))
                        
                        // Add More button
                        Surface(
                            modifier = Modifier.weight(1f).height(100.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.5f),
                            border = BorderStroke(2.dp, Brush.linearGradient(listOf(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), Color.Transparent)))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.outlineVariant)
                                    Text("Add More", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 3: Service Packages
            ServiceSection(
                number = 3,
                title = "Service Packages",
                color = MaterialTheme.colorScheme.tertiary
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    // Bento Row
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Essential Tier
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Essential Tier".uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
                                Text("Base Setup", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                AsyncImage(
                                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBIL32dQ_4uUyci6XipdsPScacKl7TUvyZf6hJj-hJjQV0MrWWklGNMYFB3mxfs2cMaCjwHttsQ27RSFACNVRzrfivpXFnZmh3jhEtTQq9B2lkjUZKowgDOGs56Pg8jwArmCRC3_9Q2q_ADXXFakF0ytqVy0JYeT2hIHeVF5_iFxhuDB7IiPKw3-3QkveHzZftkKRDFYUIDgjcY4v4boFKmvt-TfNZ2ziFZGcUC124IJtPMj2jH4CMuPklRqiZKz8Bj88Gnf1Uxg3eO",
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp)).alpha(0.8f),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = "",
                                    onValueChange = {},
                                    leadingIcon = { Text("₹", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
                                    placeholder = { Text("Starting Price", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }

                        // Signature Heritage (Large)
                        Surface(
                            modifier = Modifier.weight(2f),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                        ) {
                            Row(modifier = Modifier.padding(24.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Most Popular".uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                                    Text("Signature Heritage", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                    Text("Our most detailed package including custom Mandaps and floral ceilings.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    OutlinedTextField(
                                        value = "",
                                        onValueChange = {},
                                        leadingIcon = { Text("₹", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold) },
                                        placeholder = { Text("Premium Price Point", fontSize = 12.sp) },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, unfocusedBorderColor = Color.Transparent)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = "",
                                        onValueChange = {},
                                        placeholder = { Text("List key inclusions (e.g. Fresh Marigolds, 40ft Mandap, Entry Decor)", fontSize = 12.sp) },
                                        modifier = Modifier.fillMaxWidth().height(100.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, unfocusedBorderColor = Color.Transparent)
                                    )
                                }
                                AsyncImage(
                                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDBGZca3VWcg8cPHsDiD6orLSkHwVQSQzf1jB5SSg_aYxYFyoHdqj-Cw3EGRH48Ni6DLFrIdBKEBA_X61OMh1LD7Sl_c9fFLXlooaSdgJ9sIgAtzd6JTMl2RrRlMt5rRcFrVT4T7mgIQeeu35d63CPMXQRyW_N8bjapbZ1HIBFPUGstQeWmyN3imif9uWBwlwqtg2Or69Cgj0J7jNQD-k2Eaw6Koc3nYhy4NYDXYv3zfumhg_qqAl76bA2o3oUnO11EGFFD8j8tre64",
                                    contentDescription = null,
                                    modifier = Modifier.weight(0.6f).aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Section 4: Portfolio Gallery
            ServiceSection(
                number = 4,
                title = "Portfolio Gallery",
                color = MaterialTheme.colorScheme.onSurface,
                isHigh = true
            ) {
                Column {
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.4f),
                        border = BorderStroke(2.dp, Brush.linearGradient(listOf(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), Color.Transparent)))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.padding(16.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Upload Work Samples", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Drag and drop high-resolution images (Max 10 files)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {}, shape = CircleShape) {
                                Text("Browse Files", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Footer Actions
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 96.dp)) {
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)))
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "By submitting this form, you agree to our Partner Terms and Service Level Agreements.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        fontStyle = FontStyle.Italic
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        TextButton(onClick = {}) {
                            Text("Save Draft", fontWeight = FontWeight.Bold)
                        }
                        Button(onClick = onNavigateToDashboard, shape = CircleShape, modifier = Modifier.height(56.dp).padding(horizontal = 8.dp)) {
                            Text("Complete Registration", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceSection(
    number: Int,
    title: String,
    color: Color,
    isTonal: Boolean = false,
    isHigh: Boolean = false,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = when {
            isTonal -> MaterialTheme.colorScheme.surfaceContainerLow
            isHigh -> MaterialTheme.colorScheme.surfaceContainerHigh
            else -> MaterialTheme.colorScheme.surfaceContainerLowest
        },
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = color.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(number.toString(), fontWeight = FontWeight.Bold, color = color)
                    }
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
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun BottomNavIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isSelected: Boolean = false) {
    val color = if (isSelected) Color(0xFF8F4E00) else Color(0xFF78716A)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .let { if (isSelected) it.background(Color(0xFFFFF7ED), RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp) else it }
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color, letterSpacing = 1.sp)
    }
}
