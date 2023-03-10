package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.byowsigner.ui.domain.CreateWalletUIEvent
import com.example.byowsigner.ui.viewmodels.CreateWalletViewModel
import io.github.bitcoineducation.bitcoinjava.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWalletScreen(
    modifier: Modifier = Modifier,
    createWalletViewModel: CreateWalletViewModel
) {
    val state = createWalletViewModel.walletUIState
    val localFocus = LocalFocusManager.current

    Column(modifier = modifier
        .padding(15.dp)
        .verticalScroll(rememberScrollState())) {

        Text(text = "Create a New Wallet", style = MaterialTheme.typography.titleLarge)
        WalletFormInput(
            text = state.value.name,
            onTextChanged = {
                createWalletViewModel.onEvent(CreateWalletUIEvent.NameChanged(it))
            },
            label = "Wallet Name",
            imeAction = ImeAction.Done,
            onDone = { localFocus.clearFocus() },
            modifier = Modifier
                .padding(vertical = 15.dp)
                .semantics { this.contentDescription = "Wallet Name" }
        )

        ElevatedButton(
            onClick = { createWalletViewModel.onEvent(CreateWalletUIEvent.MnemonicSeedGenerateButtonClicked) },
            modifier = Modifier.padding(vertical = 15.dp)
        ) {
            Text("Generate Mnemonic Seed")
        }

        Text(text = "Mnemonic Seed", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 15.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .height(150.dp)) {
            TextField(
                value = state.value.mnemonicSeed,
                onValueChange = {createWalletViewModel.onEvent(CreateWalletUIEvent.MnemonicSeedChanged(it))},
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { localFocus.clearFocus() }),
                modifier = Modifier
                    .padding(10.dp)
                    .semantics { this.contentDescription = "Mnemonic Seed Words" }
            )
        }
        Row {
            ElevatedButton(
                onClick = { createWalletViewModel.onEvent(CreateWalletUIEvent.CreateButtonClicked) },
                modifier = Modifier.padding(vertical = 15.dp),
                enabled = createWalletViewModel.formOk
            ) {
                Text("Create")
            }
            ElevatedButton(onClick = { createWalletViewModel.onEvent(CreateWalletUIEvent.CancelButtonClicked) }, modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)) {
                Text("Cancel")
            }
        }
    }
}
