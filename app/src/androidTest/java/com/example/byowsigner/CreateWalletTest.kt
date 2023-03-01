package com.example.byowsigner

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.example.byowsigner.ui.screen.ByowHome
import com.example.byowsigner.ui.viewmodels.CreateWalletViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateWalletTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        val application: ByowApplication = ApplicationProvider.getApplicationContext()
        val appContainer = application.appContainer
        composeTestRule.setContent { ByowHome(createWalletViewModel = CreateWalletViewModel(appContainer.mnemonicSeedService)) }
    }

    @Test
    fun shouldCreateWallet() {
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithText("New Wallet")
            .performClick()
        composeTestRule.onNodeWithText("Generate Mnemonic Seed")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Mnemonic Seed Words")
            .assertIsDisplayed()
    }
}