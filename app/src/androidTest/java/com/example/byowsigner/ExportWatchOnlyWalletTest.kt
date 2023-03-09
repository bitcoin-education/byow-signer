package com.example.byowsigner

import androidx.compose.ui.test.*
import com.example.byowsigner.database.Wallet
import org.junit.Test
import java.util.*

class ExportWatchOnlyWalletTest : BaseTest() {

    val name = "Test Wallet"

    override fun setup() {
        super.setup()
        val mnemonicSeed = appContainer.mnemonicSeedService.generate()
        val wallet = Wallet(name, mnemonicSeed, Date())
        appContainer.walletRepository.insertWallet(wallet)
    }

    @Test
    fun shouldExportWatchOnlyWallet() {
        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithText("Export Extended Pubkeys")
            .performClick()
        composeTestRule.onNodeWithText("Select a wallet")
            .performScrollTo()
            .performClick()
        composeTestRule.onNodeWithText(name)
            .performClick()
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Wallet password")
            .performScrollTo()
            .performClick()
            .performTextInput("password")
        composeTestRule.onNodeWithText("Export")
            .performScrollTo()
            .performClick()
        composeTestRule.onNodeWithContentDescription("Extended pubkeys")
            .assertIsDisplayed()
    }
}