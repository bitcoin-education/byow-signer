package com.example.byowsigner

import android.content.Context
import com.example.byowsigner.api.MnemonicSeedService
import com.example.byowsigner.database.AppDatabase
import com.example.byowsigner.database.WalletRepository

class AppContainer(applicationContext: Context) {
    var db = AppDatabase(applicationContext)

    val mnemonicSeedService: MnemonicSeedService = MnemonicSeedService()
    var walletRepository = WalletRepository(db.walletDao())
}