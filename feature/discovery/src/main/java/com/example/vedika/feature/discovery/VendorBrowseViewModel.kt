package com.example.vedika.feature.discovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VendorBrowseViewModel @Inject constructor(
    private val vendorRepository: VendorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val category: String = checkNotNull(savedStateHandle["category"])

    val vendors: StateFlow<List<VendorProfile>> = vendorRepository.getVendorsByCategory(category)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
