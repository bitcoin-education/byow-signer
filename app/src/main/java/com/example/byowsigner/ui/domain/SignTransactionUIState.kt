package com.example.byowsigner.ui.domain

data class SignTransactionUIState(
    var selectedWallet: String = "Select a wallet",
    val password: String = "",
    val transactionJson: String = ""
)

