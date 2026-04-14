package com.example.vedika.feature.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.InventoryItem
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.InventoryRepository
import com.example.vedika.core.data.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val vendorType: VendorType = VendorType.VENUE,
    val isLoading: Boolean = true
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val inventoryRepository: InventoryRepository,
    private val vendorRepository: VendorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState

    init {
        loadInventory()
    }

    private fun loadInventory() {
        viewModelScope.launch {
            vendorRepository.getMockVendor().collect { vendor ->
                if (vendor != null) {
                    val mockId = "mock_vendor_123"
                    inventoryRepository.getInventoryForVendor(mockId).collect { items ->
                        val finalItems = if (items.isEmpty()) {
                            // Seed demo data based on type
                            if (vendor.vendorType == VendorType.VENUE) {
                                listOf(
                                    InventoryItem("v_i1", mockId, "Grand Heritage Hall", "A massive AC hall accommodating up to 500 guests.", 75000.0, true),
                                    InventoryItem("v_i2", mockId, "Swimming Pool Lawn", "Outdoor lawn perfect for evening cocktails and sangeet.", 35000.0, true),
                                    InventoryItem("v_i3", mockId, "Buffet Catering Service", "Standard North Indian menu with 12 items.", 650.0, true)
                                )
                            } else {
                                listOf(
                                    InventoryItem("d_i1", mockId, "Entrance Floral Arch", "Premium roses and jasmine work with LED focus.", 15000.0, true),
                                    InventoryItem("d_i2", mockId, "Crystal Mandap", "Glass pillars with floral ceiling and royal seating.", 85000.0, true),
                                    InventoryItem("d_i3", mockId, "Photo Booth Kit", "Thematic props and backdrop with soft lighting.", 12000.0, true)
                                )
                            }
                        } else {
                            items
                        }
                        _uiState.value = InventoryUiState(
                            items = finalItems, 
                            vendorType = vendor.vendorType,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun toggleAvailability(itemId: String, currentValue: Boolean) {
        viewModelScope.launch {
            inventoryRepository.updateInventoryAvailability(itemId, !currentValue)
        }
    }
}
