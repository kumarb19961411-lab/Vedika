package com.example.vedika.feature.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.InventoryItem
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState

    init {
        loadInventory()
    }

    private fun loadInventory() {
        viewModelScope.launch {
            authRepository.getActiveVendor().collect { vendor ->
                if (vendor != null) {
                    inventoryRepository.getInventoryForVendor(vendor.id).collect { items ->
                        _uiState.value = InventoryUiState(items = items, isLoading = false)
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
