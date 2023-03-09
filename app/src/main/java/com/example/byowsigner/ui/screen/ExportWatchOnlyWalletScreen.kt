package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import com.example.byowsigner.database.Wallet
import com.example.byowsigner.ui.domain.ExportWatchOnlyWalletUIEvent
import com.example.byowsigner.ui.domain.SignTransactionUIEvent
import com.example.byowsigner.ui.viewmodels.ExportWatchOnlyWalletViewModel


@Composable
fun ExportWatchOnlyWalletScreen(
    modifier: Modifier = Modifier,
    wallets: LiveData<List<Wallet>>,
    exportWatchOnlyWalletViewModel: ExportWatchOnlyWalletViewModel
) {
    val state = exportWatchOnlyWalletViewModel.exportWatchOnlyWalletUIState
    val localFocus = LocalFocusManager.current
    val walletList by wallets.observeAsState(initial = emptyList())

    Column(modifier = modifier
        .padding(15.dp)
        .verticalScroll(rememberScrollState())) {
        Text(text = "Export Extended Pubkeys", style = MaterialTheme.typography.titleLarge)

        WalletSelect(
            selectedWallet = state.value.selectedWallet,
            wallets = walletList,
            onClick = {
                exportWatchOnlyWalletViewModel.onEvent(ExportWatchOnlyWalletUIEvent.SelectedWalletChanged(it))
             }
        )
        WalletFormInput(
            text = state.value.password,
            onTextChanged = {
                exportWatchOnlyWalletViewModel.onEvent(ExportWatchOnlyWalletUIEvent.PasswordChanged(it))
            },
            label = "Wallet Password",
            imeAction = ImeAction.Next,
            onNext = { localFocus.clearFocus() },
            keyboardType = KeyboardType.Password,
            modifier = Modifier
                .padding(vertical = 15.dp)
                .semantics { this.contentDescription = "Wallet password" }
        )
        Row {
            ElevatedButton(onClick = {exportWatchOnlyWalletViewModel.onEvent(ExportWatchOnlyWalletUIEvent.ExportButtonClicked)}, modifier = Modifier.padding(vertical = 15.dp)) {
                Text("Export")
            }
            ElevatedButton(onClick = {exportWatchOnlyWalletViewModel.onEvent(ExportWatchOnlyWalletUIEvent.CancelButtonClicked)}, modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)) {
                Text("Cancel")
            }
        }
    }

    if (exportWatchOnlyWalletViewModel.openDialog.value) {
        Dialog(onDismissRequest = {
            exportWatchOnlyWalletViewModel.onEvent(ExportWatchOnlyWalletUIEvent.ModalDismissed)
        }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())) {
                    Text(text = "Extended Pubkeys", style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { this.contentDescription = "Extended pubkeys" })
                    Spacer(modifier = Modifier.height(15.dp))
                    SelectionContainer {
                        Text(exportWatchOnlyWalletViewModel.extendedPubkeys.value)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            exportWatchOnlyWalletViewModel.onEvent(ExportWatchOnlyWalletUIEvent.DoneButtonClicked)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}