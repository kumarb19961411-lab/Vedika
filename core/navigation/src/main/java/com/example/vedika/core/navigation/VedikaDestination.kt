package com.example.vedika.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vedika.core.data.model.AccountMode

sealed class VedikaDestination(val route: String) {
    object Splash     : VedikaDestination("splash")
    object Dashboard  : VedikaDestination("dashboard")
    object Calendar   : VedikaDestination("calendar")
    object Inventory  : VedikaDestination("inventory")
    object Finance    : VedikaDestination("finance")
    object Profile    : VedikaDestination("profile")
    object Login      : VedikaDestination("login")
    object SignUp     : VedikaDestination("signup")
    object OtpVerification : VedikaDestination("otp_verification")
    object PartnerSetup : VedikaDestination("partner_setup")
    object CategorySelection : VedikaDestination("category_selection")
    object VenueRegistration : VedikaDestination("venue_registration")
    object DecoratorRegistration : VedikaDestination("decorator_registration")
    data object NewBooking : VedikaDestination("new_booking?selectedDate={selectedDate}") {
        fun createRoute(selectedDate: Long? = null): String {
            return if (selectedDate != null) "new_booking?selectedDate=$selectedDate" else "new_booking"
        }
    }
    object AuthGraph  : VedikaDestination("auth_graph")
    object DecoratorsGallery : VedikaDestination("decorators_gallery")
    object InventoryHub : VedikaDestination("inventory_hub")

    // Consumer Milestone 2 Routes
    object UserHome : VedikaDestination("user_home")
    object Discovery : VedikaDestination("vendor_browse/All")
    data object VendorBrowse : VedikaDestination("vendor_browse/{category}") {
        fun createRoute(category: String) = "vendor_browse/$category"
    }
    data object VendorDetail : VedikaDestination("vendor_detail/{id}") {
        fun createRoute(id: String) = "vendor_detail/$id"
    }
    data object InquiryForm : VedikaDestination("inquiry_form/{id}") {
        fun createRoute(id: String) = "inquiry_form/$id"
    }

    // Deep Link Constants
    companion object {
        const val DEEP_LINK_SCHEME = "vedika"
        const val DEEP_LINK_HOST = "app"
        const val DEEP_LINK_PREFIX = "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST"

        const val DEEP_LINK_DASHBOARD = "$DEEP_LINK_PREFIX/dashboard"
        const val DEEP_LINK_FINANCE = "$DEEP_LINK_PREFIX/finance"
        const val DEEP_LINK_DISCOVERY = "$DEEP_LINK_PREFIX/discovery"
        const val DEEP_LINK_VENDOR_DETAIL = "$DEEP_LINK_PREFIX/vendor/{id}"
        const val DEEP_LINK_PROFILE = "$DEEP_LINK_PREFIX/profile"
    }
}

data class BottomNavItem(
    val destination: VedikaDestination,
    val label: String,
    val icon: ImageVector
)

fun getBottomNavItems(mode: AccountMode) = when (mode) {
    AccountMode.PARTNER -> listOf(
        BottomNavItem(VedikaDestination.Dashboard, "Dashboard", Icons.Default.Dashboard),
        BottomNavItem(VedikaDestination.Calendar,  "Calendar",  Icons.Default.CalendarMonth),
        BottomNavItem(VedikaDestination.DecoratorsGallery, "Gallery", Icons.Default.Collections),
        BottomNavItem(VedikaDestination.Inventory, "Inventory", Icons.Default.Inventory2),
        BottomNavItem(VedikaDestination.Profile,   "Profile",   Icons.Default.Person),
    )
    AccountMode.USER -> listOf(
        BottomNavItem(VedikaDestination.UserHome, "Home", Icons.Default.Dashboard),
        BottomNavItem(VedikaDestination.Discovery, "Discovery", Icons.Default.Collections),
        BottomNavItem(VedikaDestination.Profile,  "Profile",   Icons.Default.Person),
    )
}
