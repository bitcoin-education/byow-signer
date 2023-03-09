package com.example.byowsigner.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.byowsigner.ByowApplication
import com.example.byowsigner.api.ExtendedPubkeyService
import com.example.byowsigner.database.WalletRepository
import com.example.byowsigner.ui.domain.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ExportWatchOnlyWalletViewModel(
    val walletRepository: WalletRepository,
    val extendedPubkeyService: ExtendedPubkeyService
) : ViewModel() {
    private var _exportWatchOnlyWalletUIState = mutableStateOf(ExportWatchOnlyWalletUIState())

    val exportWatchOnlyWalletUIState = _exportWatchOnlyWalletUIState

    val openDialog = mutableStateOf(false)

    val extendedPubkeys = mutableStateOf("")

    val sharedEvent = MutableSharedFlow<ExportWatchOnlyWalletUIEvent>()

    fun onEvent(event: ExportWatchOnlyWalletUIEvent) {
        when(event) {
            is ExportWatchOnlyWalletUIEvent.PasswordChanged -> {
                _exportWatchOnlyWalletUIState.value = _exportWatchOnlyWalletUIState.value.copy(password = event.value)
            }
            is ExportWatchOnlyWalletUIEvent.SelectedWalletChanged -> {
                _exportWatchOnlyWalletUIState.value = _exportWatchOnlyWalletUIState.value.copy(selectedWallet = event.value)
            }
            ExportWatchOnlyWalletUIEvent.CancelButtonClicked -> {
                cancel()
            }
            ExportWatchOnlyWalletUIEvent.DoneButtonClicked -> {
                openDialog.value = false
                cancel()
            }
            ExportWatchOnlyWalletUIEvent.ExportButtonClicked -> {
                exportExtendedPubkeys()
            }
            ExportWatchOnlyWalletUIEvent.ModalDismissed -> {
                openDialog.value = false
            }
        }
    }

    private fun exportExtendedPubkeys() {
        viewModelScope.launch {
            walletRepository.findWalletByName(_exportWatchOnlyWalletUIState.value.selectedWallet).collect {
                extendedPubkeys.value = extendedPubkeyService.create(it.mnemonicSeed!!, _exportWatchOnlyWalletUIState.value.password)
                openDialog.value = true
            }
        }
    }

    private fun cancel() {
        clearInputs()
        viewModelScope.launch { sharedEvent.emit(ExportWatchOnlyWalletUIEvent.CancelButtonClicked) }
    }

    private fun clearInputs() {
        _exportWatchOnlyWalletUIState.value = ExportWatchOnlyWalletUIState()
        extendedPubkeys.value = ""
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appContainer = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ByowApplication).appContainer
                ExportWatchOnlyWalletViewModel(appContainer.walletRepository, appContainer.extendedPubkeyService)
            }
        }
    }
}