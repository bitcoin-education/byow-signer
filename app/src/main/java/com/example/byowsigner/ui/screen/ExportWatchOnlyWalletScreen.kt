package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.byowsigner.database.Wallet


@Composable
fun ExportWatchOnlyWalletScreen(
    modifier: Modifier = Modifier,
    wallets: LiveData<List<Wallet>>
) {

    Column(modifier = modifier
        .padding(15.dp)
        .verticalScroll(rememberScrollState())) {
        Text(text = "Export Extended Pubkeys", style = MaterialTheme.typography.titleLarge)
    }
}