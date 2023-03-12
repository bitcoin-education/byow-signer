package com.example.byowsigner

import androidx.compose.ui.test.*
import com.example.byowsigner.database.Wallet
import org.junit.Test
import java.util.*

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

    @Test
    fun shouldImportWallet() {
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithText("New Wallet")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Wallet Name")
            .performTextInput("Test Wallet")
        composeTestRule.onNodeWithContentDescription("Mnemonic Seed Words")
            .performClick()
            .performTextInput("about ".repeat(24).trimEnd())
        composeTestRule.onNodeWithText("Create")
            .performClick()
    }

    @Test
    fun shouldNotCreateWalletWithRepeatedName() {
        val name = "Test Wallet"
        val mnemonicSeed = appContainer.mnemonicSeedService.generate()
        val wallet = Wallet(name, mnemonicSeed, Date())
        appContainer.walletRepository.insertWallet(wallet)

        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithText("New Wallet")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Wallet Name")
            .performTextInput(name)
        composeTestRule.onNodeWithText("Generate Mnemonic Seed")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Mnemonic Seed Words")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Create")
            .performClick()
    }
}