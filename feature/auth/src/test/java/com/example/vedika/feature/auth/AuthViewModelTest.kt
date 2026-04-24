package com.example.vedika.feature.auth

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.vedika.core.data.model.AccountMode
import com.example.vedika.core.data.model.AuthFlow
import com.example.vedika.core.data.model.RoleResolutionState
import com.example.vedika.core.data.model.AppUser
import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.UserRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.session.SessionStorage
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val authRepository = mockk<AuthRepository>()
    private val vendorRepository = mockk<VendorRepository>()
    private val userRepository = mockk<UserRepository>()
    private val sessionStorage = mockk<SessionStorage>()
    private val savedStateHandle = SavedStateHandle()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { authRepository.getActiveVendor() } returns emptyFlow()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sendOtp with invalid phone returns error`() = runTest {
        val viewModel = AuthViewModel(authRepository, vendorRepository, userRepository, sessionStorage, savedStateHandle)
        viewModel.updatePhoneNumber("123")
        
        viewModel.sendOtp(mockk()) {}

        viewModel.uiState.test {
            val state = awaitItem()
            assertNotNull(state.error)
            assertEquals("Please enter a valid 10-digit phone number", state.error)
        }
    }

    @Test
    fun `sendOtp success updates state to OtpSent`() = runTest {
        val phoneNumber = "9876543210"
        val formattedPhone = "+91$phoneNumber"
        val verificationId = "v_id_123"
        
        coEvery { authRepository.sendOtp(formattedPhone, any()) } returns Result.success(verificationId)

        val viewModel = AuthViewModel(authRepository, vendorRepository, userRepository, sessionStorage, savedStateHandle)
        viewModel.updatePhoneNumber(phoneNumber)
        
        viewModel.sendOtp(mockk()) {}
        
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(verificationId, state.verificationId)
            assertEquals(RoleResolutionState.OtpSent, state.roleResolutionState)
        }
    }

    @Test
    fun `verifyOtp as Partner with existing profile updates to Verified with profileExists true`() = runTest {
        val uid = "uid_123"
        val otp = "123456"
        val vendorUser = VendorUser(
            id = uid,
            businessName = "Business",
            ownerName = "Owner Name",
            isVerified = true,
            primaryServiceCategory = "Marriage Garden"
        )
        
        coEvery { authRepository.verifyOtp(any(), otp) } returns Result.success(vendorUser)
        coEvery { vendorRepository.getVendorProfile(uid) } returns Result.success(
            VendorProfile(
                id = uid,
                businessName = "Business",
                ownerName = "Owner",
                location = "Kanpur",
                pricing = "1.5L",
                vendorType = VendorType.VENUE,
                primaryCategory = "Marriage Garden"
            )
        )
        every { sessionStorage.saveAccountMode(AccountMode.PARTNER) } returns Unit

        val viewModel = AuthViewModel(authRepository, vendorRepository, userRepository, sessionStorage, savedStateHandle)
        viewModel.selectAccountMode(AccountMode.PARTNER)
        viewModel.updateOtp(otp)
        
        viewModel.verifyOtp {}
        
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val resolution = state.roleResolutionState as RoleResolutionState.Verified
            assertEquals(true, resolution.profileExists)
            assertEquals(uid, resolution.uid)
        }
    }

    @Test
    fun `verifyOtp as Partner with no profile updates to Verified with profileExists false`() = runTest {
        val uid = "uid_123"
        val otp = "123456"
        val vendorUser = VendorUser(
            id = uid,
            businessName = "",
            ownerName = "Owner Name",
            isVerified = false,
            primaryServiceCategory = ""
        )
        
        coEvery { authRepository.verifyOtp(any(), otp) } returns Result.success(vendorUser)
        coEvery { vendorRepository.getVendorProfile(uid) } returns Result.failure(Exception("VENDOR_NOT_FOUND"))
        every { sessionStorage.saveAccountMode(AccountMode.PARTNER) } returns Unit

        val viewModel = AuthViewModel(authRepository, vendorRepository, userRepository, sessionStorage, savedStateHandle)
        viewModel.selectAccountMode(AccountMode.PARTNER)
        viewModel.updateOtp(otp)
        
        viewModel.verifyOtp {}
        
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val resolution = state.roleResolutionState as RoleResolutionState.Verified
            assertEquals(false, resolution.profileExists)
        }
    }

    @Test
    fun `loginAsDevBypass follows canonical role resolution path`() = runTest {
        val uid = "dev_uid"
        val vendorUser = VendorUser(
            id = uid,
            businessName = "Dev Business",
            ownerName = "Dev User",
            isVerified = true,
            primaryServiceCategory = "Marriage Garden"
        )
        
        coEvery { authRepository.loginAsDevBypass("dev") } returns Result.success(vendorUser)
        coEvery { vendorRepository.getVendorProfile(uid) } returns Result.success(
            VendorProfile(
                id = uid,
                businessName = "Dev Business",
                ownerName = "Dev Owner",
                location = "Kanpur",
                pricing = "1.5L",
                vendorType = VendorType.VENUE,
                primaryCategory = "Marriage Garden"
            )
        )
        every { sessionStorage.saveAccountMode(AccountMode.PARTNER) } returns Unit

        val viewModel = AuthViewModel(authRepository, vendorRepository, userRepository, sessionStorage, savedStateHandle)
        viewModel.selectAccountMode(AccountMode.PARTNER)
        
        viewModel.loginAsDevBypass {}
        
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val resolution = state.roleResolutionState as RoleResolutionState.Verified
            assertEquals(true, resolution.profileExists)
        }
    }
}
