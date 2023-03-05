package com.example.byowsigner.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.byowsigner.ByowApplication
import com.example.byowsigner.database.WalletRepository
import com.example.byowsigner.ui.domain.SignTransactionUIEvent
import com.example.byowsigner.ui.domain.SignTransactionUIState

class SignTransactionViewModel(
    walletRepository: WalletRepository
) : ViewModel() {

    private var _signTransactionUIState = mutableStateOf(SignTransactionUIState())

    val signTransactionUIState = _signTransactionUIState

    val wallets = walletRepository.getWallets().asLiveData()

    fun onEvent(event: SignTransactionUIEvent) {
        when(event) {
            is SignTransactionUIEvent.PasswordChanged -> {
                _signTransactionUIState.value = _signTransactionUIState.value.copy(password = event.value)
            }
            is SignTransactionUIEvent.SelectedWalletChanged -> {
                _signTransactionUIState.value = _signTransactionUIState.value.copy(selectedWallet = event.value)
            }
            is SignTransactionUIEvent.TransactionJsonChanged -> {
                _signTransactionUIState.value = _signTransactionUIState.value.copy(transactionJson = event.value)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appContainer = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ByowApplication).appContainer
                SignTransactionViewModel(
                    walletRepository = appContainer.walletRepository
                )
            }
        }
    }
}