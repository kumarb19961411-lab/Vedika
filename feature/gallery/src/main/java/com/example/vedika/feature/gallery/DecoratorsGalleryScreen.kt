package com.example.vedika.feature.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.design.components.VedikaTabTopAppBar
import com.example.vedika.core.design.theme.NotoSerif

@Composable
fun DecoratorsGalleryScreen(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            VedikaTabTopAppBar(title = "Visual Heritage")
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column {
                        Text(
                            text = if (uiState.vendorType == VendorType.VENUE) "VENUE SHOWCASE" else "CURATED PORTFOLIO",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFD4AF37), // Heritage Gold
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (uiState.vendorType == VendorType.VENUE) "Property Views" else "Design Portfolio",
                            style = MaterialTheme.typography.headlineLarge,
                            fontFamily = NotoSerif,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(uiState.albums) { album ->
                    GalleryAccordion(
                        title = album.title,
                        icon = getIconForName(album.iconName),
                        images = album.images
                    )
                }

                item {
                    PortfolioAnalytics(uiState.vendorType)
                }
            }
        }
    }
}

private fun getIconForName(name: String): ImageVector {
    return when (name) {
        "Home" -> Icons.Default.Home
        "Spa" -> Icons.Default.Spa
        "Checkroom" -> Icons.Default.Checkroom
        "TempleHindu" -> Icons.Default.TempleHindu
        "Face" -> Icons.Default.Face
        "WaterDrop" -> Icons.Default.WaterDrop
        "Restaurant" -> Icons.Default.Restaurant
        else -> Icons.Default.Collections
    }
}

@Composable
fun GalleryAccordion(
    title: String,
    icon: ImageVector,
    images: List<String>,
    initiallyExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFD4AF37).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title, 
                        style = MaterialTheme.typography.titleLarge, 
                        fontFamily = NotoSerif,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Photos", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (images.isNotEmpty()) {
                            AsyncImage(
                                model = images[0],
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16/9f)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            if (images.size > 1) {
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    images.drop(1).forEach { img ->
                                        AsyncImage(
                                            model = img,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PortfolioAnalytics(vendorType: VendorType) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF006064).copy(alpha = 0.05f), // Heritage Teal
        border = BorderStroke(1.dp, Color(0xFF006064).copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = if (vendorType == VendorType.VENUE) "Property Analytics" else "Portfolio Analytics", 
                style = MaterialTheme.typography.titleLarge, 
                fontFamily = NotoSerif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF006064)
            )
            Text(
                text = if (vendorType == VendorType.VENUE) 
                    "Your property views are up 25% this month. Clear hall photos and amenity shots are driving inquiries." 
                    else "Your design portfolio is trending! High-quality thematic shots are increasing booking inquiries by up to 40%.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AnalyticsItem(value = if (vendorType == VendorType.VENUE) "48" else "124", label = "Photos", color = Color(0xFFD4AF37))
                AnalyticsItem(value = if (vendorType == VendorType.VENUE) "6" else "12", label = "Albums", color = Color(0xFF006064))
                AnalyticsItem(value = if (vendorType == VendorType.VENUE) "4" else "15", label = "Featured", color = Color(0xFF7B1FA2))
            }
        }
    }
}

@Composable
fun AnalyticsItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
        Text(text = label.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, letterSpacing = 1.sp)
    }
}
