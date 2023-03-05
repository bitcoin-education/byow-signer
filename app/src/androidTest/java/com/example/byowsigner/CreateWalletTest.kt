package com.example.byowsigner

import androidx.compose.ui.test.*
import org.junit.Test

class CreateWalletTest : BaseTest() {

    @Test
    fun shouldCreateWallet() {
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithText("New Wallet")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Wallet Name")
            .performTextInput("Test Wallet")
        composeTestRule.onNodeWithText("Generate Mnemonic Seed")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Mnemonic Seed Words")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Create")
            .performClick()
    }
}