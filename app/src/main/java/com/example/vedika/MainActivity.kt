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
import androidx.navigation.compose.NavHost
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
import com.example.vedika.feature.auth.AuthFlow
import com.example.vedika.feature.auth.AccountMode
import com.example.vedika.feature.calendar.CalendarScreen
import com.example.vedika.feature.dashboard.DashboardScreen
import com.example.vedika.feature.dashboard.NewBookingScreen
import com.example.vedika.feature.finance.FinanceScreen
import com.example.vedika.feature.gallery.DecoratorsGalleryScreen
import com.example.vedika.feature.inventory.InventoryScreen
import com.example.vedika.feature.inventory.InventoryHubScreen
import androidx.hilt.navigation.compose.hiltViewModel
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
            startDestination = VedikaDestination.AuthGraph.route,
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
            navigation(
                startDestination = VedikaDestination.Login.route,
                route = VedikaDestination.AuthGraph.route
            ) {
                composable(VedikaDestination.Login.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(VedikaDestination.AuthGraph.route)
                    }
                    val authViewModel: AuthViewModel = hiltViewModel(parentEntry)
                    LoginScreen(
                        viewModel = authViewModel,
                        onNavigateToOtp = { navController.navigate(VedikaDestination.OtpVerification.route) },
                        onNavigateToSignUp = { navController.navigate(VedikaDestination.SignUp.route) },
                        onDevBypassSuccess = {
                            navController.navigate(VedikaDestination.Dashboard.route) {
                                popUpTo(VedikaDestination.AuthGraph.route) { inclusive = true }
                            }
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
                            val route = when {
                                currentAuthState.accountMode == AccountMode.USER -> {
                                    VedikaDestination.Dashboard.route
                                }
                                currentAuthState.accountMode == AccountMode.PARTNER && 
                                        currentAuthState.authFlow == AuthFlow.SIGN_IN -> {
                                    VedikaDestination.Dashboard.route
                                }
                                currentAuthState.accountMode == AccountMode.PARTNER && 
                                        currentAuthState.authFlow == AuthFlow.SIGN_UP -> {
                                    VedikaDestination.CategorySelection.route
                                }
                                else -> VedikaDestination.Dashboard.route
                            }
                            navController.navigate(route) {
                                popUpTo(VedikaDestination.AuthGraph.route) { inclusive = true }
                            }
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
                CalendarScreen()
            }
            composable(VedikaDestination.Inventory.route) {
                InventoryScreen()
            }
            composable(VedikaDestination.DecoratorsGallery.route) {
                DecoratorsGalleryScreen()
            }
            composable(VedikaDestination.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(VedikaDestination.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(VedikaDestination.NewBooking.route) {
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