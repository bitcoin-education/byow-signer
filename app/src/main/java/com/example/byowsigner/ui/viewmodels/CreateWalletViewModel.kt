package com.example.byowsigner.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.byowsigner.api.MnemonicSeedService
import com.example.byowsigner.ui.domain.CreateWalletUIEvent
import com.example.byowsigner.ui.domain.CreateWalletUIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CreateWalletViewModel(private val mnemonicSeedService: MnemonicSeedService) : ViewModel() {
    private var _walletUIState = mutableStateOf(CreateWalletUIState())

    val walletUIState = _walletUIState

    val sharedEvent = MutableSharedFlow<CreateWalletUIEvent>()

    fun onEvent(event: CreateWalletUIEvent) {
        when(event) {
            is CreateWalletUIEvent.NameChanged -> {
                _walletUIState.value = _walletUIState.value.copy(name = event.value)
            }
            is CreateWalletUIEvent.MnemonicSeedGenerateButtonClicked -> {
                _walletUIState.value = _walletUIState.value.copy(mnemonicSeed = generateMnemonicSeed())
            }
            is CreateWalletUIEvent.CreateButtonClicked -> {
                createWallet()
            }
            is CreateWalletUIEvent.CancelButtonClicked -> {
                cancel()
            }
        }
    }

    private fun generateMnemonicSeed(): String = mnemonicSeedService.generate()

    private fun createWallet() {
        clearInputs()
    }

    private fun cancel() {
        clearInputs()
        viewModelScope.launch { sharedEvent.emit(CreateWalletUIEvent.CancelButtonClicked) }
    }

    private fun clearInputs() {
        _walletUIState.value = CreateWalletUIState()
    }
}