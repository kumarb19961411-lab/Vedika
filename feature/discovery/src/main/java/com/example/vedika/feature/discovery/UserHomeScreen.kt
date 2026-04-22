package com.example.vedika.feature.discovery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.vedika.core.data.model.VendorProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    onNavigateToBrowse: (String) -> Unit,
    onNavigateToVendorDetail: (String) -> Unit,
    viewModel: UserHomeViewModel = hiltViewModel()
) {
    val featuredVendors by viewModel.featuredVendors.collectAsState()
    val categories = viewModel.categories

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Plan Your Perfect Event",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero / Promo Banner
            PromoBanner()

            Spacer(modifier = Modifier.height(24.dp))

            // Categories Section
            SectionHeader(title = "Browse Categories", onSeeAll = {})
            CategoryCarousel(categories = categories, onCategoryClick = onNavigateToBrowse)

            Spacer(modifier = Modifier.height(24.dp))

            // Featured Vendors
            SectionHeader(title = "Featured Venues", onSeeAll = { onNavigateToBrowse("Venue") })
            FeaturedVendorsRow(
                vendors = featuredVendors.filter { it.vendorType == com.example.vedika.core.data.model.VendorType.VENUE },
                onVendorClick = onNavigateToVendorDetail
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Decorators
            SectionHeader(title = "Top Decorators", onSeeAll = { onNavigateToBrowse("Decorator") })
            FeaturedVendorsRow(
                vendors = featuredVendors.filter { it.vendorType == com.example.vedika.core.data.model.VendorType.DECORATOR },
                onVendorClick = onNavigateToVendorDetail
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CategoryCarousel(
    categories: List<CategoryItem>,
    onCategoryClick: (String) -> Unit
) {
    // Mapping internal UI images to categories
    val categoryImages = mapOf(
        "Venue" to "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&q=60&w=400",
        "Decorator" to "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&q=60&w=400",
        "Catering" to "https://images.unsplash.com/photo-1555244162-803834f70033?auto=format&fit=crop&q=60&w=400",
        "Photography" to "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&q=60&w=400"
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onCategoryClick(category.name) }
            ) {
                AsyncImage(
                    model = categoryImages[category.name] ?: "",
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(category.name, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun FeaturedVendorsRow(
    vendors: List<VendorProfile>,
    onVendorClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(vendors) { vendor ->
            Card(
                modifier = Modifier
                    .width(240.dp)
                    .clickable { onVendorClick(vendor.id) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Column {
                    AsyncImage(
                        model = vendor.coverImage,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(vendor.businessName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(vendor.rating ?: "0.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Text(" • ", style = MaterialTheme.typography.labelSmall)
                            Text(vendor.primaryCategory, style = MaterialTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Starting from ${vendor.pricing}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun PromoBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&q=80&w=1000",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Summer Wedding Sale",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Up to 20% off on premium venues",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { /* TODO */ }) {
                    Text("Explore Now")
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onSeeAll) {
            Text("See All")
        }
    }
}
