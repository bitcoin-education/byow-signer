package com.example.byowsigner.ui.domain

data class ExportWatchOnlyWalletUIState(
    var selectedWallet: String = "Select a wallet",
    val password: String = "",
)
