package com.example.vedika.feature.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.InventoryItem
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.InventoryRepository
import com.example.vedika.core.data.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val vendorType: VendorType = VendorType.VENUE,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryRepository: InventoryRepository,
    private val vendorRepository: VendorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        loadInventory()
    }

    private fun loadInventory() {
        viewModelScope.launch {
            val vendorId = vendorRepository.getCurrentVendorId() ?: return@launch
            
            // First get the vendor type for UI styling
            vendorRepository.getVendorProfile(vendorId).collect { result ->
                result.onSuccess { profile ->
                    _uiState.update { it.copy(vendorType = profile.vendorType) }
                }
            }

            // Then stream inventory
            inventoryRepository.getInventoryForVendor(vendorId)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { items ->
                    _uiState.update { it.copy(items = items, isLoading = false) }
                }
        }
    }

    fun addInventoryItem(name: String, description: String, price: Double) {
        viewModelScope.launch {
            val vendorId = vendorRepository.getCurrentVendorId() ?: return@launch
            val newItem = InventoryItem(
                id = "", // Firestore will generate
                vendorId = vendorId,
                name = name,
                description = description,
                price = price,
                isAvailable = true
            )
            inventoryRepository.addInventoryItem(newItem)
        }
    }

    fun toggleAvailability(item: InventoryItem) {
        viewModelScope.launch {
            inventoryRepository.updateInventoryAvailability(item.id, !item.isAvailable)
        }
    }
}
