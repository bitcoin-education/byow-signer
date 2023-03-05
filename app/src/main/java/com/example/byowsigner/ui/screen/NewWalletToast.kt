package com.example.byowsigner.ui.screen

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.LiveData
import com.example.byowsigner.database.Wallet

@Composable
fun NewWalletToast(wallets: LiveData<List<Wallet>>) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var toastActive by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = context) {
        wallets.observe(lifecycleOwner) {
            if (!toastActive) {
                toastActive = !toastActive
                return@observe
            }
            Toast.makeText(context,"Wallet created!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}