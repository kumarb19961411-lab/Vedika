package com.example.vedika.feature.dashboard

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.CalendarRepository
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
class NewBookingViewModelTest {

    private val authRepository = mockk<AuthRepository>()
    private val bookingRepository = mockk<BookingRepository>()
    private val vendorRepository = mockk<VendorRepository>()
    private val calendarRepository = mockk<CalendarRepository>()
    private val savedStateHandle = SavedStateHandle()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { authRepository.getActiveVendor() } returns flowOf(null)
        coEvery { bookingRepository.checkConflict(any(), any(), any()) } returns Result.success(false)
        coEvery { calendarRepository.isDateBlocked(any(), any()) } returns false
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `submitBooking with empty name returns validation error`() = runTest {
        val viewModel = NewBookingViewModel(authRepository, bookingRepository, vendorRepository, calendarRepository, savedStateHandle)
        viewModel.onCustomerNameChange("")
        viewModel.onTotalAmountChange("100")
        
        viewModel.submitBooking {}

        viewModel.formState.test {
            val state = awaitItem()
            assertEquals("Customer name is required", state.customerNameError)
            assertEquals(false, state.isSubmitting)
        }
    }

    @Test
    fun `submitBooking prevents duplicate submissions when already submitting`() = runTest {
        val vendor = VendorUser(id = "v1", businessName = "Biz", ownerName = "Owner", isVerified = true, primaryServiceCategory = "Venue")
        every { authRepository.getActiveVendor() } returns flowOf(vendor)
        coEvery { bookingRepository.createBooking(any()) } coAnswers {
            kotlinx.coroutines.delay(1000)
            Result.success(Unit)
        }

        val viewModel = NewBookingViewModel(authRepository, bookingRepository, vendorRepository, calendarRepository, savedStateHandle)
        viewModel.onCustomerNameChange("Customer")
        viewModel.onTotalAmountChange("100")
        
        // First call
        viewModel.submitBooking {}
        
        // Second call while first is pending
        viewModel.submitBooking {}

        viewModel.formState.test {
            val state = awaitItem()
            assertEquals(true, state.isSubmitting)
            // No more items should be emitted until the first submission completes
        }
    }

    @Test
    fun `submitBooking success updates state and triggers callback`() = runTest {
        val vendor = VendorUser(id = "v1", businessName = "Biz", ownerName = "Owner", isVerified = true, primaryServiceCategory = "Venue")
        every { authRepository.getActiveVendor() } returns flowOf(vendor)
        coEvery { bookingRepository.createBooking(any()) } returns Result.success(Unit)

        val viewModel = NewBookingViewModel(authRepository, bookingRepository, vendorRepository, calendarRepository, savedStateHandle)
        viewModel.onCustomerNameChange("Customer")
        viewModel.onTotalAmountChange("100")
        
        var successTriggered = false
        viewModel.submitBooking { successTriggered = true }
        
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.formState.test {
            val state = awaitItem()
            assertEquals(true, state.isSubmitSuccess)
            assertEquals(false, state.isSubmitting)
            assertEquals(true, successTriggered)
        }
    }
}
