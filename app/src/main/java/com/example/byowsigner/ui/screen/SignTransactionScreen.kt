package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.byowsigner.ui.viewmodels.SignTransactionViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.imeAction
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.byowsigner.ui.domain.SignTransactionUIEvent
import com.example.byowsigner.ui.domain.TransactionDetailsUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignTransactionScreen(
    modifier: Modifier = Modifier,
    signTransactionViewModel: SignTransactionViewModel
) {
    val state = signTransactionViewModel.signTransactionUIState
    val wallets by signTransactionViewModel.wallets.observeAsState(initial = emptyList())
    var expanded by remember { mutableStateOf(false) }
    val localFocus = LocalFocusManager.current
    val transactionDetails: TransactionDetailsUIState? by signTransactionViewModel.transactionDetails.observeAsState()

    Column(modifier = modifier
        .padding(15.dp)
        .verticalScroll(rememberScrollState())) {
        Text(text = "Sign a Transaction", style = MaterialTheme.typography.titleLarge)
        WalletFormInput(
            text = state.value.transactionJson,
            onTextChanged = {
                signTransactionViewModel.onEvent(SignTransactionUIEvent.TransactionJsonChanged(it))
            },
            label = "Transaction JSON",
            imeAction = ImeAction.Next,
            onNext = { localFocus.clearFocus() },
            modifier = Modifier
                .padding(vertical = 15.dp)
                .semantics { this.contentDescription = "Transaction json" }
        )
        if (transactionDetails?.inputs?.isNotEmpty() == true) {
            Card(Modifier.fillMaxWidth()) {
                Text(text = "Transaction Details", modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.bodyLarge)
                Text(text = "Inputs", modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium)
                transactionDetails?.inputs?.forEach {
                    Text(text = "${it.txId}:${it.index}", modifier = Modifier.padding(horizontal = 10.dp), style = MaterialTheme.typography.bodySmall)
                    Text(text = "${it.utxoDto.amount} BTC", modifier = Modifier.padding(horizontal = 10.dp), style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Text(text = "Outputs", modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium)
                transactionDetails?.outputs?.forEach {
                    Text(text = it.address, modifier = Modifier.padding(horizontal = 10.dp), style = MaterialTheme.typography.bodySmall)
                    Text(text = "${it.amount} BTC", modifier = Modifier.padding(horizontal = 10.dp), style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.padding(vertical = 15.dp)
        ) {
            TextField(
                readOnly = true,
                value = state.value.selectedWallet,
                onValueChange = {},
                label = { Text("Select a wallet") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.semantics { this.imeAction = ImeAction.Done }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                wallets.forEach { wallet ->
                    DropdownMenuItem(
                        text = { Text(wallet.name) },
                        onClick = {
                            signTransactionViewModel.onEvent(SignTransactionUIEvent.SelectedWalletChanged(wallet.name))
                            expanded = false
                        },
                    )
                }
            }
        }
        WalletFormInput(
            text = state.value.password,
            onTextChanged = {
                signTransactionViewModel.onEvent(SignTransactionUIEvent.PasswordChanged(it))
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
            ElevatedButton(onClick = { signTransactionViewModel.onEvent(SignTransactionUIEvent.SignButtonClicked) }, modifier = Modifier.padding(vertical = 15.dp)) {
                Text("Sign")
            }
            ElevatedButton(onClick = { signTransactionViewModel.onEvent(SignTransactionUIEvent.CancelButtonClicked) }, modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)) {
                Text("Cancel")
            }
        }
    }

    if (signTransactionViewModel.openDialog.value) {
        Dialog(onDismissRequest = {
            signTransactionViewModel.onEvent(SignTransactionUIEvent.ModalDismissed)
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
                    Text(text = "Signed Transaction", style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { this.contentDescription = "Signed transaction" })
                    Spacer(modifier = Modifier.height(15.dp))
                    SelectionContainer {
                        Text(text = signTransactionViewModel.signedTransaction.value)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            signTransactionViewModel.onEvent(SignTransactionUIEvent.DoneButtonClicked)
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