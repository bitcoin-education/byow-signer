package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.imeAction
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.byowsigner.database.Wallet


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletSelect(selectedWallet: String, wallets: List<Wallet>, onClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(vertical = 15.dp)
    ) {
        TextField(
            readOnly = true,
            value = selectedWallet,
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
                        onClick(wallet.name)
                        expanded = false
                    },
                )
            }
        }
    }
}