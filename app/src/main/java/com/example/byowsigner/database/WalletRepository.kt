package com.example.byowsigner.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletRepository(private val walletDao: WalletDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getWallets() = walletDao.getAll()

    fun insertWallet(wallet: Wallet) {
        coroutineScope.launch(Dispatchers.IO) {
            walletDao.insertAll(wallet)
        }
    }
}