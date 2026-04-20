package com.example.vedika.feature.inventory

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.vedika.core.design.components.VedikaTabTopAppBar
import com.example.vedika.core.design.theme.NotoSerif
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vedika.core.data.model.VendorType
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryHubScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceColor = MaterialTheme.colorScheme.background

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showAddItemDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            com.example.vedika.core.design.components.VedikaTopAppBar(
                title = "Inventory Hub",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // Editorial Header
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                Text(
                    "MODERN HERITAGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = secondaryColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    "Inventory Hub",
                    style = MaterialTheme.typography.displayMedium,
                    fontFamily = NotoSerif,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Text(
                    "Curate and manage your premium wedding assets with precision and grace.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                Row(modifier = Modifier.padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { showAddItemDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        modifier = Modifier.weight(1f).height(56.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Item", fontWeight = FontWeight.Bold)
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f)),
                        modifier = Modifier.size(56.dp),
                        onClick = {}
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // High-Level Stats
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InventoryStatCard(label = "Total Assets", value = state.items.size.toString(), icon = Icons.Default.Category, color = primaryColor, modifier = Modifier.weight(1f))
                InventoryStatCard(label = "Available", value = state.items.count { it.isAvailable }.toString(), icon = Icons.Default.EventAvailable, color = secondaryColor, modifier = Modifier.weight(1f))
                InventoryStatCard(label = "Maintenance", value = "0", icon = Icons.Default.Build, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.weight(1f))
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            } else if (state.items.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.LightGray)
                        Text("No inventory items found", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                    }
                }
            }

            // Featured Asset Card (Maharaja Throne Style)
            Surface(
                modifier = Modifier.fillMaxWidth().height(320.dp),
                shape = RoundedCornerShape(32.dp),
                shadowElevation = 8.dp
            ) {
                Box {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida-public/AB6AXuD4PrzWbQGcZJwwaSdH1pkfadYe5bj-rLfHlbGouQo3SDos29dyoM-WqyHfYfHLOfoFxw_b44PvV9Acki3_39m8lvijMnbjzhscbSqcJC0yEhWtD19H3xu_knuiuSc9znAxEp3fsZqQQ5VcsRWYYMuwxfSCEa7FjYvoRpuSCSxTVpan3df_HjTeWMaT21ENfFmhRfbhTR8wmKw7hc5spFTJZDs5DGlMI1EB6T9awHMr0BP37MB3Cgj7VhHyTIewLDbqOgXtSCk0m2MD",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))))
                    
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Surface(
                            color = secondaryColor,
                            shape = CircleShape
                        ) {
                            Text(
                                "AVAILABLE",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primaryContainer,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Maharaja Throne Set", style = MaterialTheme.typography.headlineLarge, fontFamily = NotoSerif, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Royal Collection • Signature Asset", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                        
                        Row(modifier = Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Price per Event", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                                Text("₹45,000", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primaryContainer)
                            }
                            Spacer(modifier = Modifier.width(32.dp))
                            Column {
                                Text("Total Stock", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                                Text("12 Sets", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Supporting Cards
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                state.items.chunked(2).forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowItems.forEach { item ->
                            SupportingAssetCard(
                                title = item.name,
                                subtitle = item.description,
                                price = "₹${item.price.toInt()}",
                                isAvailable = item.isAvailable,
                                modifier = Modifier.weight(1f),
                                onToggle = { viewModel.toggleAvailability(item) }
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showAddItemDialog) {
        AddInventoryItemDialog(
            onDismiss = { showAddItemDialog = false },
            onConfirm = { name, desc, price ->
                viewModel.addInventoryItem(name, desc, price)
                showAddItemDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Item added to catalog")
                }
            }
        )
    }
}

@Composable
private fun InventoryStatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(value, style = MaterialTheme.typography.titleLarge, fontFamily = NotoSerif, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SupportingAssetCard(
    title: String,
    subtitle: String,
    price: String,
    isAvailable: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { onToggle() },
                        modifier = Modifier.scale(0.7f)
                    )
                }
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text(price, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                if (!isAvailable) {
                    Text("UNAVAILABLE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AddInventoryItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Asset to Catalog", fontFamily = NotoSerif) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Asset Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price per Event") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, description, price.toDoubleOrNull() ?: 0.0) },
                enabled = name.isNotEmpty() && price.isNotEmpty()
            ) {
                Text("Add Asset")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

