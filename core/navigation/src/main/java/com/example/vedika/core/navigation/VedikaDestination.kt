package com.example.vedika.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class VedikaDestination(val route: String) {
    object Dashboard  : VedikaDestination("dashboard")
    object Calendar   : VedikaDestination("calendar")
    object Inventory  : VedikaDestination("inventory")
    object Finance    : VedikaDestination("finance")
    object Profile    : VedikaDestination("profile")
    object Login      : VedikaDestination("login")
    object NewBooking : VedikaDestination("new_booking")
}

data class BottomNavItem(
    val destination: VedikaDestination,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(VedikaDestination.Dashboard, "Dashboard", Icons.Default.Dashboard),
    BottomNavItem(VedikaDestination.Calendar,  "Calendar",  Icons.Default.CalendarMonth),
    BottomNavItem(VedikaDestination.Inventory, "Inventory", Icons.Default.Inventory2),
    BottomNavItem(VedikaDestination.Finance,   "Finance",   Icons.Default.AccountBalanceWallet),
    BottomNavItem(VedikaDestination.Profile,   "Profile",   Icons.Default.Person),
)
