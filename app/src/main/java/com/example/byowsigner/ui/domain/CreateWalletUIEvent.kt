package com.example.byowsigner.ui.domain

sealed class CreateWalletUIEvent {
    data class NameChanged(val value: String): CreateWalletUIEvent()
    object MnemonicSeedGenerateButtonClicked: CreateWalletUIEvent()
    object CreateButtonClicked: CreateWalletUIEvent()
    object CancelButtonClicked: CreateWalletUIEvent()
    data class MnemonicSeedChanged(val value: String): CreateWalletUIEvent()
}