package com.example.vedika.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val vendorRepository: VendorRepository
) : ViewModel() {

    val featuredVendors: StateFlow<List<VendorProfile>> = vendorRepository.getFeaturedVendors()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories = listOf(
        CategoryItem("Venue", "VENUE"),
        CategoryItem("Decorator", "DECORATOR"),
        CategoryItem("Catering", "CATERING"),
        CategoryItem("Photography", "PHOTOGRAPHY")
    )
}

data class CategoryItem(val name: String, val id: String)
