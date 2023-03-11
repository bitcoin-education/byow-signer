package com.example.byowsigner.ui.domain

sealed class ExportWatchOnlyWalletUIEvent {
    data class SelectedWalletChanged(val value: String): ExportWatchOnlyWalletUIEvent()

    data class PasswordChanged(val value: String): ExportWatchOnlyWalletUIEvent()

    object ExportButtonClicked: ExportWatchOnlyWalletUIEvent()

    object CancelButtonClicked: ExportWatchOnlyWalletUIEvent()

    object DoneButtonClicked: ExportWatchOnlyWalletUIEvent()

    object ModalDismissed: ExportWatchOnlyWalletUIEvent()
    object QRCodeButtonClicked : ExportWatchOnlyWalletUIEvent()
}