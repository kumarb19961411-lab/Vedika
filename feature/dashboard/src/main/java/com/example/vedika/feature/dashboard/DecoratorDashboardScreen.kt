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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vedika.core.design.theme.NotoSerif

@Composable
fun DecoratorDashboardScreen(
    state: DashboardUiState,
    onNavigateToGallery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val primaryColor = Color(0xFF006A6A) // Teal for Decorators
    val secondaryColor = Color(0xFF8F4E00) // Saffron accent
    val surfaceColor = Color(0xFFFFF8EF)

    Box(modifier = modifier.fillMaxSize().background(surfaceColor)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero Section (Visual Impact)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAkNYLPL9Nc7NztFYT0rx4tqk68jKvLHMQXELpIFVBLFiabHYYnxwC5bCH-pToD5koZLGpJXfQihAWaWxiFeGfdCyqrdrGeQ0dPssHgmfFTYrSMQDfHa8k0b6cbl73xKurzu40OSrphrvLefsEFaRx9GnPYA5iDJRKuIcjmXYpkzhj_twGxL_KmBRX72o6FzBnZ74E8o4-5pu_V9gMHdgZjs8RFsL2Ut6xZLd8kIlfbqboKCVpCBz5tCiuNfCuhwzj5MVSxRcgmvYmb",
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
                        color = Color(0xFF00BFA5).copy(alpha = 0.3f),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color(0xFF00BFA5).copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "PREMIUM DECOR",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFE0F2F1),
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
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.businessName.ifBlank { "Luxe Decor Partner" },
                        style = MaterialTheme.typography.headlineLarge,
                        fontFamily = NotoSerif,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 40.sp
                    )
                    Text(
                        text = if (!state.yearsExperience.isNullOrBlank()) "${state.yearsExperience} Experience • Curate your premium wedding designs." else "Curate and manage your premium wedding designs with precision and grace.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Stats row (Overlapping)
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
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DecoratorStat(label = "Designs", value = "128", color = primaryColor)
                    VerticalDivider(modifier = Modifier.height(32.dp).width(1.dp), color = Color.LightGray.copy(alpha = 0.5f))
                    DecoratorStat(label = "Ongoing", value = "05", color = primaryColor)
                    VerticalDivider(modifier = Modifier.height(32.dp).width(1.dp), color = Color.LightGray.copy(alpha = 0.5f))
                    DecoratorStat(label = "Rating", value = "4.9 ★", color = primaryColor)
                }
            }

            // Main Content
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                
                // Analytics / Social Reach
                SocialReachSection(state, primaryColor, secondaryColor)

                Spacer(modifier = Modifier.height(32.dp))

                // Couture Designs Gallery Preview
                SectionHeader(title = "Couture Designs", actionText = "Browse All", onAction = onNavigateToGallery)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1469334031218-e382a71b716b",
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1544911845-1f34a3eb46b1",
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().aspectRatio(3/4f).clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1519167758481-83f550bb49b3",
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().aspectRatio(3/4f).clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34",
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Service Collections
                SectionHeader(title = "Service Collections")
                Column(modifier = Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (state.packageTiers.isNotEmpty()) {
                        state.packageTiers.forEachIndexed { index, tier ->
                            DesignPackageCard(
                                name = tier.name, 
                                price = tier.price, 
                                tag = if (index == 0) "Essential" else "Signature", 
                                color = if (index == 0) primaryColor else secondaryColor
                            )
                        }
                    } else {
                        // Default Fallback
                        DesignPackageCard(name = "Heritage Mandap", price = "₹1,50,000", tag = "Classic", color = primaryColor)
                        DesignPackageCard(name = "Royal Celebration", price = "₹3,00,000", tag = "Premium", color = secondaryColor)
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun DecoratorStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontFamily = NotoSerif, fontWeight = FontWeight.Bold, color = color)
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
}

@Composable
private fun SocialReachSection(state: DashboardUiState, primary: Color, secondary: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF5EDDE),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Design Reach", style = MaterialTheme.typography.titleLarge, fontFamily = NotoSerif)
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SocialItem(icon = Icons.Default.Groups, label = "Followers", value = "2,482", color = primary)
                SocialItem(icon = Icons.Default.Favorite, label = "Engagement", value = "15%", color = Color(0xFFBA1A1A))
                SocialItem(icon = Icons.Default.Visibility, label = "Impressions", value = "12k+", color = secondary)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Insights, contentDescription = null, tint = primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Your designs were saved 84 times this week.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
private fun SocialItem(icon: ImageVector, label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DesignPackageCard(name: String, price: String, tag: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(60.dp), shape = RoundedCornerShape(12.dp), color = color.copy(alpha = 0.1f)) {
                Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.padding(16.dp), tint = color)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(color = color.copy(alpha = 0.1f), shape = CircleShape) {
                        Text(tag.uppercase(), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
                    }
                }
                Text("Starting from $price", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
private fun SectionHeader(title: String, actionText: String? = null, onAction: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontFamily = NotoSerif, fontStyle = FontStyle.Italic, color = Color(0xFF1E1B13))
        if (actionText != null) {
            Text(actionText, modifier = Modifier.clickable { onAction() }, style = MaterialTheme.typography.labelLarge, color = Color(0xFF8F4E00), fontWeight = FontWeight.Bold)
        }
    }
}
