package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.byowsigner.ui.viewmodels.SignTransactionViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.byowsigner.ui.domain.SignTransactionUIEvent

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
    }
}