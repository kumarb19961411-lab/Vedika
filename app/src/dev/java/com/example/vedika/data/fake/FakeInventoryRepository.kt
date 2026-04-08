package com.example.vedika.data.fake

import com.example.vedika.core.data.model.InventoryItem
import com.example.vedika.core.data.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeInventoryRepository @Inject constructor() : InventoryRepository {
    private val inventory = MutableStateFlow<List<InventoryItem>>(
        listOf(
            InventoryItem("i1", "dev_vendor_001", "Grand Heritage Hall", "A massive AC hall accommodating up to 500 guests with dining facility attached.", 75000.0, true),
            InventoryItem("i2", "dev_vendor_001", "South Indian Premium Catering", "Per plate cost includes 5 main courses, 2 sweets, and welcome drinks.", 450.0, true)
        )
    )

    override fun getInventoryForVendor(vendorId: String): Flow<List<InventoryItem>> {
        return inventory.map { it.filter { item -> item.vendorId == vendorId } }
    }

    override suspend fun addInventoryItem(item: InventoryItem): Result<Unit> {
        inventory.value = inventory.value + item
        return Result.success(Unit)
    }

    override suspend fun updateInventoryAvailability(itemId: String, isAvailable: Boolean): Result<Unit> {
        val current = inventory.value.toMutableList()
        val index = current.indexOfFirst { it.id == itemId }
        if (index != -1) {
            val updated = current[index].copy(isAvailable = isAvailable)
            current[index] = updated
            inventory.value = current
            return Result.success(Unit)
        }
        return Result.failure(Exception("Inventory item not found"))
    }
}
