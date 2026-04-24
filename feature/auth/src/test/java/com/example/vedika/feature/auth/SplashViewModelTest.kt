package com.example.vedika.feature.auth

import app.cash.turbine.test
import com.example.vedika.core.data.model.AccountMode
import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.UserRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.session.SessionStorage
import com.example.vedika.core.data.util.VedikaLogger
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val authRepository = mockk<AuthRepository>()
    private val vendorRepository = mockk<VendorRepository>()
    private val userRepository = mockk<UserRepository>()
    private val sessionStorage = mockk<SessionStorage>()
    private val logger = mockk<VedikaLogger>(relaxed = true)

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
    fun `when user is not logged in, startup state is Unauthenticated`() = runTest {
        coEvery { authRepository.getCurrentUserId() } returns null

        val viewModel = SplashViewModel(authRepository, vendorRepository, userRepository, sessionStorage, logger)

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.startupState.test {
            assertEquals(StartupState.Unauthenticated, awaitItem())
        }
    }

    @Test
    fun `when user is logged in but no mode hint, startup state is Unauthenticated`() = runTest {
        coEvery { authRepository.getCurrentUserId() } returns "uid123"
        coEvery { sessionStorage.getAccountMode() } returns null

        val viewModel = SplashViewModel(authRepository, vendorRepository, userRepository, sessionStorage, logger)

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.startupState.test {
            assertEquals(StartupState.Unauthenticated, awaitItem())
        }
    }

    @Test
    fun `when user is logged in as partner and profile exists, startup state is Authenticated with profileExists true`() = runTest {
        val uid = "uid123"
        coEvery { authRepository.getCurrentUserId() } returns uid
        coEvery { sessionStorage.getAccountMode() } returns AccountMode.PARTNER
        coEvery { vendorRepository.getVendorProfile(uid) } returns Result.success(
            VendorProfile(
                id = uid,
                businessName = "Test Business",
                ownerName = "Test Owner",
                location = "Test Location",
                pricing = "Test Pricing",
                vendorType = VendorType.VENUE,
                primaryCategory = "Test Category"
            )
        )

        val viewModel = SplashViewModel(authRepository, vendorRepository, userRepository, sessionStorage, logger)

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.startupState.test {
            val state = awaitItem() as StartupState.Authenticated
            assertEquals(AccountMode.PARTNER, state.mode)
            assertEquals(true, state.profileExists)
        }
    }

    @Test
    fun `when user is logged in as partner but profile does not exist, startup state is Authenticated with profileExists false`() = runTest {
        val uid = "uid123"
        coEvery { authRepository.getCurrentUserId() } returns uid
        coEvery { sessionStorage.getAccountMode() } returns AccountMode.PARTNER
        coEvery { vendorRepository.getVendorProfile(uid) } returns Result.failure(Exception("VENDOR_NOT_FOUND"))

        val viewModel = SplashViewModel(authRepository, vendorRepository, userRepository, sessionStorage, logger)

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.startupState.test {
            val state = awaitItem() as StartupState.Authenticated
            assertEquals(AccountMode.PARTNER, state.mode)
            assertEquals(false, state.profileExists)
        }
    }
}
