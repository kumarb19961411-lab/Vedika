package com.example.vedika.feature.discovery

import androidx.lifecycle.SavedStateHandle
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
class VendorDetailViewModel @Inject constructor(
    private val vendorRepository: VendorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val vendorId: String = checkNotNull(savedStateHandle["vendorId"])

    val vendorProfile: StateFlow<VendorProfile?> = vendorRepository.getVendorProfileStream(vendorId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
