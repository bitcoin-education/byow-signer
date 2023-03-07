package com.example.byowsigner

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.byowsigner.database.AppDatabase
import com.example.byowsigner.database.WalletRepository
import com.example.byowsigner.ui.screen.ByowHome
import com.example.byowsigner.ui.viewmodels.CreateWalletViewModel
import com.example.byowsigner.ui.viewmodels.SignTransactionViewModel
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.Before
import org.junit.Rule
import java.security.Security

abstract class BaseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var appContainer: AppContainer

    @Before
    open fun setup() {
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
        val application: ByowApplication = ApplicationProvider.getApplicationContext()
        appContainer = application.appContainer
        appContainer.db = Room.inMemoryDatabaseBuilder(application.applicationContext, AppDatabase::class.java).build()
        appContainer.walletRepository = WalletRepository(appContainer.db.walletDao())

        composeTestRule.setContent {
            ByowHome(
                createWalletViewModel = CreateWalletViewModel(
                    appContainer.mnemonicSeedService,
                    appContainer.walletRepository
                ),
                signTransactionViewModel = SignTransactionViewModel(
                    appContainer.walletRepository,
                    appContainer.transactionParserService,
                    appContainer.transactionSignerService
                )
            )
        }
    }
}