package com.example.byowsigner

import androidx.compose.ui.test.*
import com.example.byowsigner.database.Wallet
import org.junit.Test
import java.util.*

class SignTransactionTest : BaseTest() {

    val name = "Test Wallet"

    override fun setup() {
        super.setup()
        val mnemonicSeed = appContainer.mnemonicSeedService.generate()
        val wallet = Wallet(name, mnemonicSeed, Date())
        appContainer.walletRepository.insertWallet(wallet)
    }

    @Test
    fun shouldSignTransactionWithPassword() {
        val transactionJson = """
            {"utxos":[{"derivationPath":"84'/0'/0'/0/0","amount":1.00000000,"addressType":"SEGWIT"},{"derivationPath":"84'/0'/0'/0/1","amount":1.00000000,"addressType":"SEGWIT"}],"transaction":"01000000000102973168d4e20414383feb515f6f1db1d7ede5f96ba2d651397c3694fd8b1f32f70100000000ffffffffbf57e10c388bed6d6d7206f15117526f68f567f82dbda1e458f18e2741b8e5ba0100000000ffffffff0280d1f008000000001600147f3998191a879ce07b0e0adc73328dd77ad73ec5fde0fa0200000000160014f010f387ffb93967e5e5e206b597ace6b52f5d2d02480000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002100000000000000000000000000000000000000000000000000000000000000000002480000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002100000000000000000000000000000000000000000000000000000000000000000000000000"}
        """.trimIndent()

        composeTestRule.onNodeWithContentDescription("Menu")
            .performClick()
        composeTestRule.onNodeWithText("Sign Transaction")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Transaction json")
            .performTextInput(transactionJson)
        composeTestRule.onNodeWithText("Select a wallet")
            .performClick()
        composeTestRule.onNodeWithText(name)
            .performClick()
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Wallet password")
            .performTextInput("password")
        composeTestRule.onNodeWithText("Sign")
            .performClick()
        composeTestRule.onNodeWithContentDescription("Signed transaction")
            .assertIsDisplayed()
    }
}