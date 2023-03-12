package com.example.byowsigner.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletRepository(private val walletDao: WalletDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun getWallets() = walletDao.getAll()

    fun findWalletByName(name: String) = walletDao.findByName(name)

    fun insertWallet(wallet: Wallet, onError: (Throwable) -> Unit = {}, onSuccess: () -> Unit = {}) {
        coroutineScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                walletDao.insertAll(wallet)
            }.onSuccess { onSuccess() }
            .onFailure { e -> onError(e) }
        }
    }
}