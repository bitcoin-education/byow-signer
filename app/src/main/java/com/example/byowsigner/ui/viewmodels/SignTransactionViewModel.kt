package com.example.byowsigner.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.byowsigner.ByowApplication
import com.example.byowsigner.api.TransactionParserService
import com.example.byowsigner.api.TransactionSignerService
import com.example.byowsigner.database.WalletRepository
import com.example.byowsigner.ui.domain.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class SignTransactionViewModel(
    val walletRepository: WalletRepository,
    val transactionParserService: TransactionParserService,
    val transactionSignerService: TransactionSignerService
) : ViewModel() {

    private var _signTransactionUIState = mutableStateOf(SignTransactionUIState())

    val signTransactionUIState = _signTransactionUIState

    val wallets = walletRepository.getWallets().asLiveData()

    private var transactionDetailsUIEvent = MutableSharedFlow<TransactionDetailsUIState>()

    var transactionDetails = transactionDetailsUIEvent.asLiveData()

    val sharedEvent = MutableSharedFlow<SignTransactionUIEvent>()

    val signedTransaction = mutableStateOf("")

    val openDialog = mutableStateOf(false)

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
                parseTransaction()
            }
            is SignTransactionUIEvent.CancelButtonClicked -> {
                cancel()
            }
            is SignTransactionUIEvent.SignButtonClicked -> {
                signTransaction()
            }
            SignTransactionUIEvent.DoneButtonClicked -> {
                openDialog.value = false
                cancel()
            }

            SignTransactionUIEvent.ModalDismissed -> openDialog.value = false
        }
    }

    private fun signTransaction() {
        viewModelScope.launch {
            walletRepository.findWalletByName(_signTransactionUIState.value.selectedWallet).collect {
                val unsignedTransaction = parseUnsignedTransaction()
                signedTransaction.value = transactionSignerService.signTransaction(
                    unsignedTransaction.transaction,
                    it.mnemonicSeed!!,
                    _signTransactionUIState.value.password,
                    parseUnsignedTransaction().utxos
                )
                openDialog.value = true
            }
        }
    }

    private fun cancel() {
        clearInputs()
        viewModelScope.launch { sharedEvent.emit(SignTransactionUIEvent.CancelButtonClicked) }
    }

    private fun clearInputs() {
        _signTransactionUIState.value = SignTransactionUIState()
        viewModelScope.launch {
            transactionDetailsUIEvent.emit(TransactionDetailsUIState())
        }
    }

    private fun parseTransaction() {
        viewModelScope.launch {
            try {
                val unsignedTransactionPayload = parseUnsignedTransaction()
                transactionDetailsUIEvent.emit(transactionParserService.parse(unsignedTransactionPayload))
            } catch (_: Exception) {
                transactionDetailsUIEvent.emit(TransactionDetailsUIState())
            }
        }
    }

    private fun parseUnsignedTransaction() = jacksonObjectMapper().readValue(
        _signTransactionUIState.value.transactionJson,
        UnsignedTransactionPayload::class.java
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appContainer = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ByowApplication).appContainer
                SignTransactionViewModel(
                    walletRepository = appContainer.walletRepository,
                    transactionParserService = appContainer.transactionParserService,
                    transactionSignerService = appContainer.transactionSignerService
                )
            }
        }
    }
}