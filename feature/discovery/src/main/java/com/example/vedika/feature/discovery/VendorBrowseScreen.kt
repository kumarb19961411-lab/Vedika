package com.example.vedika.feature.discovery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorBrowseScreen(
    category: String,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    // Mock data based on category
    val vendors = remember(category) {
        listOf(
            VendorSummary("1", "Grand Plaza Royale", "Venue", "4.8", "₹50,000", "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&q=60&w=400"),
            VendorSummary("2", "Crystal Gardens", "Venue", "4.6", "₹35,000", "https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&q=60&w=400"),
            VendorSummary("3", "Royal Heritage Hall", "Venue", "4.9", "₹75,000", "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=60&w=400"),
            VendorSummary("4", "The Marigold Deck", "Venue", "4.5", "₹40,000", "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?auto=format&fit=crop&q=60&w=400")
        ).filter { it.type == category || category == "All" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$category Listings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vendors) { vendor ->
                VendorListCard(vendor = vendor, onClick = { onNavigateToDetail(vendor.id) })
            }
        }
    }
}

@Composable
fun VendorListCard(vendor: VendorSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = vendor.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1.dp)) {
                Text(
                    text = vendor.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${vendor.type} • ${vendor.rating} ★",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Starting from ${vendor.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
