package com.example.vedika.feature.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vedika.core.data.model.VendorType

@Composable
fun DashboardScreen(
    onNavigateToNewBooking: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToInventoryHub: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    when (state.vendorType) {
        VendorType.VENUE -> {
            VenueDashboardScreen(
                state = state,
                onNavigateToNewBooking = onNavigateToNewBooking,
                onNavigateToInventoryHub = onNavigateToInventoryHub,
                modifier = modifier
            )
        }
        VendorType.DECORATOR -> {
            DecoratorDashboardScreen(
                state = state,
                onNavigateToGallery = onNavigateToGallery,
                modifier = modifier
            )
        }
    }
}
