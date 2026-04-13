package com.example.vedika.feature.inventory

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vedika.core.design.theme.NotoSerif

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryHubScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = Color(0xFF8F4E00)
    val secondaryColor = Color(0xFF006A6A)
    val surfaceColor = Color(0xFFFFF8EF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "KalyanaVedika",
                        fontFamily = NotoSerif,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = primaryColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White.copy(alpha = 0.8f))
            )
        },
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
                        onClick = {},
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
                        color = Color(0xFFF5EDDE),
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
                InventoryStatCard(label = "Total Assets", value = "1,284", icon = Icons.Default.Category, color = primaryColor, modifier = Modifier.weight(1f))
                InventoryStatCard(label = "Booked", value = "842", icon = Icons.Default.EventAvailable, color = secondaryColor, modifier = Modifier.weight(1f))
                InventoryStatCard(label = "Maintenance", value = "12", icon = Icons.Default.Build, color = Color(0xFF795900), modifier = Modifier.weight(1f))
            }

            // Featured Asset Card (Maharaja Throne Style)
            Surface(
                modifier = Modifier.fillMaxWidth().height(450.dp),
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
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Maharaja Throne Set", style = MaterialTheme.typography.headlineLarge, fontFamily = NotoSerif, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Royal Collection • Signature Asset", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                        
                        Row(modifier = Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Price per Event", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.6f))
                                Text("₹45,000", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFFFFCC80))
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
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SupportingAssetCard(
                    title = "Crystal Chandelier",
                    subtitle = "Victorian Series",
                    price = "₹18,500",
                    stock = "20",
                    booked = "17",
                    isLowStock = true,
                    image = "https://lh3.googleusercontent.com/aida-public/AB6AXuAkNYLPL9Nc7NztFYT0rx4tqk68jKvLHMQXELpIFVBLFiabHYYnxwC5bCH-pToD5koZLGpJXfQihAWaWxiFeGfdCyqrdrGeQ0dPssHgmfFTYrSMQDfHa8k0b6cbl73xKurzu40OSrphrvLefsEFaRx9GnPYA5iDJRKuIcjmXYpkzhj_twGxL_KmBRX72o6FzBnZ74E8o4-5pu_V9gMHdgZjs8RFsL2Ut6xZLd8kIlfbqboKCVpCBz5tCiuNfCuhwzj5MVSxRcgmvYmb",
                    modifier = Modifier.weight(1f)
                )
                SupportingAssetCard(
                    title = "Mandap Frames",
                    subtitle = "Teak Wood",
                    price = "₹22,000",
                    stock = "04",
                    booked = "01",
                    isLowStock = false,
                    image = "https://lh3.googleusercontent.com/aida-public/AB6AXuBAm3kjn56oB1tLSFoh71iXQ71LMB2qU3O4GO0grnvhSkY7O_Y8uEov8UgKQndG_pKPXdbnZHPM33BIBxae4RXYtZVO2OJYoloSv_yIWsyYg7ZvlQvtyQrm8na6qb3J2zdl3Y03aCP0XSnebc3QAoo9pO30Ezbmh8Vxk-7O4rkolFCsPRb0RWmXkWS522D0xeX2m3mZtn_aR1LCYZ4Nh6Az4-KmZi1dbX53wj2dckS65cgEn36yegozEsfORZ3aog-lKfXbKhoDhm4N",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun InventoryStatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFBF3E4)
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
    stock: String,
    booked: String,
    isLowStock: Boolean,
    image: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(300.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Column {
            Box(modifier = Modifier.weight(1.2f).fillMaxWidth()) {
                AsyncImage(model = image, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                if (isLowStock) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                        color = Color(0xFFBA1A1A),
                        shape = CircleShape
                    ) {
                        Text("LOW STOCK", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f).padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Rate", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(price, style = MaterialTheme.typography.titleSmall, color = Color(0xFF8F4E00), fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = booked.toFloat() / stock.toFloat(),
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                        color = if (isLowStock) Color(0xFFBA1A1A) else Color(0xFF006A6A),
                        trackColor = Color.LightGray.copy(alpha = 0.3f)
                    )
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Stock: $stock", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
                        Text("Booked: $booked", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = if (isLowStock) Color(0xFFBA1A1A) else Color.Gray)
                    }
                }
            }
        }
    }
}
