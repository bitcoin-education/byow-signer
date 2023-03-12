package com.example.byowsigner.ui.viewmodels

import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.byowsigner.ByowApplication
import com.example.byowsigner.api.MnemonicSeedService
import com.example.byowsigner.database.Wallet
import com.example.byowsigner.database.WalletRepository
import com.example.byowsigner.ui.domain.CreateWalletUIEvent
import com.example.byowsigner.ui.domain.CreateWalletUIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.*

class CreateWalletViewModel(
    private val mnemonicSeedService: MnemonicSeedService,
    private val walletRepository: WalletRepository
) : ViewModel() {
    private var _walletUIState = mutableStateOf(CreateWalletUIState())

    val walletUIState = _walletUIState

    val sharedEvent = MutableSharedFlow<CreateWalletUIEvent>()

    val formOk: Boolean
        get() = _walletUIState.value.name.isNotBlank() && _walletUIState.value.mnemonicSeed.isNotBlank()

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
            is CreateWalletUIEvent.MnemonicSeedChanged -> {
                _walletUIState.value = _walletUIState.value.copy(mnemonicSeed = event.value)
            }
            else -> {}
        }
    }

    private fun generateMnemonicSeed(): String = mnemonicSeedService.generate()

    private fun createWallet() {
        val wallet = _walletUIState.value.run { Wallet(name, mnemonicSeed, Date()) }
        walletRepository.insertWallet(
            wallet = wallet,
            onError = {
                val message = when(it) {
                    is SQLiteConstraintException -> "Could not create wallet: repeated name."
                    else -> "Could not create wallet: unknown error"
                }
                viewModelScope.launch { sharedEvent.emit(CreateWalletUIEvent.RepeatedNameError(message)) }
            },
            onSuccess = {
                clearInputs()
                viewModelScope.launch { sharedEvent.emit(CreateWalletUIEvent.CreateButtonClicked) }
            }
        )
    }

    private fun cancel() {
        clearInputs()
        viewModelScope.launch { sharedEvent.emit(CreateWalletUIEvent.CancelButtonClicked) }
    }

    private fun clearInputs() {
        _walletUIState.value = CreateWalletUIState()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appContainer = (this[APPLICATION_KEY] as ByowApplication).appContainer
                CreateWalletViewModel(
                    mnemonicSeedService = appContainer.mnemonicSeedService,
                    walletRepository = appContainer.walletRepository
                )
            }
        }
    }
}