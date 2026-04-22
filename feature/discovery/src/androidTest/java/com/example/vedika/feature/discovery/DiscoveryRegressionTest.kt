package com.example.vedika.feature.discovery

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.vedika.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class DiscoveryRegressionTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testDiscoveryBrowseFlow() {
        // Start on Home Screen (assuming we are bypassed/logged in or starting at home)
        // Note: In a real CI, we'd use a @TestInstallIn to swap with a mock repository
        // but for this stabilization milestone, we verify the "Fake" path works as intended.
        
        // 1. Verify Category "Marriage Garden" is visible
        composeTestRule.onNodeWithText("Marriage Garden").assertIsDisplayed()
        
        // 2. Click "Marriage Garden"
        composeTestRule.onNodeWithText("Marriage Garden").performClick()
        
        // 3. Verify we see a vendor from that category (e.g., "Royal Orchid Garden")
        composeTestRule.onNodeWithText("Royal Orchid Garden").assertIsDisplayed()
        
        // 4. Click the vendor card
        composeTestRule.onNodeWithText("Royal Orchid Garden").performClick()
        
        // 5. Verify Detail Screen title
        composeTestRule.onNodeWithText("Royal Orchid Garden").assertIsDisplayed()
        composeTestRule.onNodeWithText("Civil Lines, Kanpur").assertIsDisplayed()
    }
}
