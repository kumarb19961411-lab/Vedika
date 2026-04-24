package com.example.vedika.feature.inventory

import app.cash.turbine.test
import com.example.vedika.core.data.model.InventoryItem
import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.InventoryRepository
import com.example.vedika.core.data.repository.VendorRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InventoryViewModelTest {

    private val authRepository = mockk<AuthRepository>()
    private val inventoryRepository = mockk<InventoryRepository>()
    private val vendorRepository = mockk<VendorRepository>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadInventory success updates state with items`() = runTest {
        val vendorId = "vendor123"
        val items = listOf(
            InventoryItem("1", vendorId, "Item 1", "Desc 1", 100.0, true)
        )
        val profile = VendorProfile(
            id = vendorId,
            businessName = "Biz",
            ownerName = "Owner",
            location = "City",
            pricing = "100",
            vendorType = VendorType.VENUE,
            primaryCategory = "Venue"
        )

        coEvery { authRepository.getCurrentUserId() } returns vendorId
        coEvery { vendorRepository.getVendorProfile(vendorId) } returns Result.success(profile)
        every { inventoryRepository.getInventoryForVendor(vendorId) } returns flowOf(items)

        val viewModel = InventoryViewModel(authRepository, inventoryRepository, vendorRepository)
        
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(items, state.items)
            assertEquals(false, state.isLoading)
            assertEquals(null, state.error)
        }
    }

    @Test
    fun `retry resets error and reloads inventory`() = runTest {
        val vendorId = "vendor123"
        coEvery { authRepository.getCurrentUserId() } returns vendorId
        coEvery { vendorRepository.getVendorProfile(vendorId) } returns Result.failure(Exception("Network Error"))
        every { inventoryRepository.getInventoryForVendor(vendorId) } returns flowOf(emptyList())

        val viewModel = InventoryViewModel(authRepository, inventoryRepository, vendorRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Ensure error state
        assertEquals("Profile load failed: Network Error", viewModel.uiState.value.error)

        // Setup success for retry
        coEvery { vendorRepository.getVendorProfile(vendorId) } returns Result.success(
            VendorProfile(
                id = vendorId,
                businessName = "Biz",
                ownerName = "Owner",
                location = "City",
                pricing = "100",
                vendorType = VendorType.VENUE,
                primaryCategory = "Venue"
            )
        )

        viewModel.retry()
        
        viewModel.uiState.test {
            // Initial state from retry() call (isLoading=true, error=null)
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)
            assertEquals(null, loadingState.error)
            
            // Final success state
            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(null, successState.error)
        }
    }
}
