package com.example.byowsigner.ui.domain

sealed class SignTransactionUIEvent {
    data class SelectedWalletChanged(val value: String): SignTransactionUIEvent()
    data class PasswordChanged(val value: String): SignTransactionUIEvent()
    data class TransactionJsonChanged(val value: String): SignTransactionUIEvent()
    object SignButtonClicked: SignTransactionUIEvent()
    object CancelButtonClicked: SignTransactionUIEvent()
    object DoneButtonClicked: SignTransactionUIEvent()
    object ModalDismissed: SignTransactionUIEvent()
}