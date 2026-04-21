package com.example.vedika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vedika.core.design.theme.VedikaTheme
import com.example.vedika.core.navigation.VedikaDestination
import com.example.vedika.core.navigation.bottomNavItems
import com.example.vedika.feature.auth.LoginScreen
import com.example.vedika.feature.auth.SignupScreen
import com.example.vedika.feature.auth.OtpVerificationScreen
import com.example.vedika.feature.auth.CategorySelectionScreen
import com.example.vedika.feature.auth.VenueRegistrationScreen
import com.example.vedika.feature.auth.DecoratorRegistrationScreen
import com.example.vedika.feature.auth.PartnerSetupScreen
import com.example.vedika.feature.auth.ProfileScreen
import com.example.vedika.feature.auth.AuthViewModel
import com.example.vedika.feature.auth.SplashViewModel
import com.example.vedika.feature.auth.SplashScreen
import com.example.vedika.feature.auth.StartupState
import com.example.vedika.core.data.model.AuthFlow
import com.example.vedika.core.data.model.AccountMode
import com.example.vedika.core.data.model.RoleResolutionState
import com.example.vedika.feature.calendar.CalendarScreen
import com.example.vedika.feature.dashboard.DashboardScreen
import com.example.vedika.feature.dashboard.NewBookingScreen
import com.example.vedika.feature.finance.FinanceScreen
import com.example.vedika.feature.gallery.DecoratorsGalleryScreen
import com.example.vedika.feature.inventory.InventoryHubScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vedika.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VedikaTheme {
                VedikaAppShell()
            }
        }
    }
}

@Composable
fun VedikaAppShell() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val topLevelRoutes = bottomNavItems.map { it.destination.route }
    val showBottomBar = currentRoute in topLevelRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.destination.route,
                            onClick = {
                                navController.navigate(item.destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = VedikaDestination.Splash.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(250)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            }
        ) {
            composable(VedikaDestination.Splash.route) {
                val viewModel: SplashViewModel = hiltViewModel()
                val state by viewModel.startupState.collectAsState()

                SplashScreen(
                    state = state,
                    onRetry = { viewModel.resolveUserSession() },
                    onGoToLogin = {
                        navController.navigate(VedikaDestination.AuthGraph.route) {
                            popUpTo(VedikaDestination.Splash.route) { inclusive = true }
                        }
                    }
                )

                LaunchedEffect(state) {
                    when (state) {
                        is StartupState.Unauthenticated -> {
                            navController.navigate(VedikaDestination.AuthGraph.route) {
                                popUpTo(VedikaDestination.Splash.route) { inclusive = true }
                            }
                        }
                        is StartupState.Authenticated -> {
                            val authenticatedState = state as StartupState.Authenticated
                            val destRoute = when {
                                authenticatedState.mode == AccountMode.PARTNER -> {
                                    if (authenticatedState.profileExists) VedikaDestination.Dashboard.route
                                    else VedikaDestination.CategorySelection.route
                                }
                                else -> VedikaDestination.DecoratorsGallery.route
                            }
                            navController.navigate(destRoute) {
                                popUpTo(VedikaDestination.Splash.route) { inclusive = true }
                            }
                        }
                        else -> {}
                    }
                }
            }

            navigation(
                startDestination = VedikaDestination.Login.route,
                route = VedikaDestination.AuthGraph.route
            ) {
                composable(VedikaDestination.Login.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(VedikaDestination.AuthGraph.route)
                    }
                    val authViewModel: AuthViewModel = hiltViewModel(parentEntry)
                    val currentAuthState by authViewModel.uiState.collectAsState()
                    LoginScreen(
                        viewModel = authViewModel,
                        onNavigateToOtp = { navController.navigate(VedikaDestination.OtpVerification.route) },
                        onNavigateToSignUp = { navController.navigate(VedikaDestination.SignUp.route) },
                        onDevBypassSuccess = {
                            val state = authViewModel.uiState.value
                            navigateToResolution(
                                navController = navController,
                                accountMode = state.accountMode,
                                resolutionState = state.roleResolutionState
                            )
                        }
                    )
                }
                composable(VedikaDestination.SignUp.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(VedikaDestination.AuthGraph.route)
                    }
                    val authViewModel: AuthViewModel = hiltViewModel(parentEntry)
                    SignupScreen(
                        viewModel = authViewModel,
                        onNavigateToOtp = { navController.navigate(VedikaDestination.OtpVerification.route) },
                        onNavigateToSignIn = { navController.navigate(VedikaDestination.Login.route) }
                    )
                }
                composable(VedikaDestination.OtpVerification.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(VedikaDestination.AuthGraph.route)
                    }
                    val authViewModel: AuthViewModel = hiltViewModel(parentEntry)
                    val currentAuthState by authViewModel.uiState.collectAsState()
                    OtpVerificationScreen(
                        viewModel = authViewModel,
                        onVerificationSuccess = {
                            val state = authViewModel.uiState.value
                            navigateToResolution(
                                navController = navController,
                                accountMode = state.accountMode,
                                resolutionState = state.roleResolutionState
                            )
                        },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(VedikaDestination.CategorySelection.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(VedikaDestination.AuthGraph.route)
                    }
                    val authViewModel: AuthViewModel = hiltViewModel(parentEntry)
                    CategorySelectionScreen(
                        viewModel = authViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToVenueRegistration = { navController.navigate(VedikaDestination.VenueRegistration.route) },
                        onNavigateToDecoratorRegistration = { navController.navigate(VedikaDestination.DecoratorRegistration.route) },
                        onNavigateToDashboard = {
                            navController.navigate(VedikaDestination.Dashboard.route) {
                                popUpTo(VedikaDestination.AuthGraph.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(VedikaDestination.VenueRegistration.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(VedikaDestination.AuthGraph.route)
                    }
                    val authViewModel: AuthViewModel = hiltViewModel(parentEntry)
                    VenueRegistrationScreen(
                        viewModel = authViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToDashboard = {
                            navController.navigate(VedikaDestination.Dashboard.route) {
                                popUpTo(VedikaDestination.AuthGraph.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(VedikaDestination.DecoratorRegistration.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(VedikaDestination.AuthGraph.route)
                    }
                    val authViewModel: AuthViewModel = hiltViewModel(parentEntry)
                    DecoratorRegistrationScreen(
                        viewModel = authViewModel,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToDashboard = {
                            navController.navigate(VedikaDestination.Dashboard.route) {
                                popUpTo(VedikaDestination.AuthGraph.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
            composable(VedikaDestination.PartnerSetup.route) {
                PartnerSetupScreen(
                    onSetupComplete = {
                        navController.navigate(VedikaDestination.Dashboard.route) {
                            popUpTo(VedikaDestination.PartnerSetup.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(VedikaDestination.Dashboard.route) {
                DashboardScreen(
                    onNavigateToNewBooking = {
                        navController.navigate(VedikaDestination.NewBooking.route)
                    },
                    onNavigateToInventoryHub = {
                        navController.navigate(VedikaDestination.InventoryHub.route)
                    },
                    onNavigateToGallery = {
                        navController.navigate(VedikaDestination.DecoratorsGallery.route)
                    }
                )
            }
            composable(VedikaDestination.Calendar.route) {
                CalendarScreen(
                    onNavigateToNewBooking = { epoch ->
                        navController.navigate(VedikaDestination.NewBooking.createRoute(epoch))
                    }
                )
            }
            composable(VedikaDestination.Inventory.route) {
                // Tab shell: InventoryHubScreen is the canonical, premium inventory view.
                // onNavigateBack is a no-op here since root tabs do not have back navigation.
                InventoryHubScreen(
                    onNavigateBack = { /* Root tab — no back action */ }
                )
            }
            composable(VedikaDestination.DecoratorsGallery.route) {
                DecoratorsGalleryScreen()
            }
            composable(VedikaDestination.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(VedikaDestination.AuthGraph.route) {
                            popUpTo(VedikaDestination.AuthGraph.route) { inclusive = true }
                        }
                    },
                    appVersion = BuildConfig.VERSION_NAME
                )
            }
            composable(
                route = VedikaDestination.NewBooking.route,
                arguments = listOf(
                    navArgument("selectedDate") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) {
                NewBookingScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(VedikaDestination.InventoryHub.route) {
                InventoryHubScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

/**
 * Shared routing logic for both real OTP verification and developer bypass flows.
 * Ensures that both flows resolve profile status before landing on terminal screens.
 */
private fun navigateToResolution(
    navController: androidx.navigation.NavController,
    accountMode: AccountMode,
    resolutionState: RoleResolutionState
) {
    if (resolutionState !is RoleResolutionState.Verified) return

    val profileExists = resolutionState.profileExists
    val destination = when {
        accountMode == AccountMode.USER -> {
            VedikaDestination.DecoratorsGallery.route
        }
        accountMode == AccountMode.PARTNER -> {
            if (profileExists) VedikaDestination.Dashboard.route
            else VedikaDestination.CategorySelection.route
        }
        else -> VedikaDestination.DecoratorsGallery.route
    }

    navController.navigate(destination) {
        popUpTo(VedikaDestination.AuthGraph.route) { inclusive = true }
    }
}