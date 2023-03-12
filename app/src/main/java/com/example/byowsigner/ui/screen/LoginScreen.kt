package com.example.byowsigner.ui.screen

import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.example.byowsigner.ui.viewmodels.CreateWalletViewModel
import com.example.byowsigner.ui.viewmodels.ExportWatchOnlyWalletViewModel
import com.example.byowsigner.ui.viewmodels.SignTransactionViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    createWalletViewModel: CreateWalletViewModel,
    signTransactionViewModel: SignTransactionViewModel,
    exportWatchOnlyWalletViewModel: ExportWatchOnlyWalletViewModel
) {
    val authenticated = remember { mutableStateOf(false) }
    if (!authenticated.value) {
        Authenticate { authenticated.value = it }
    } else {
        ByowHome(
            createWalletViewModel = createWalletViewModel,
            signTransactionViewModel = signTransactionViewModel,
            exportWatchOnlyWalletViewModel = exportWatchOnlyWalletViewModel
        )
    }
}

@Composable
fun Authenticate(onSuccess: (Boolean) -> Unit) {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Login")
        .setSubtitle("Log in using your device credential")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()
    val biometricPrompt = BiometricPrompt(LocalContext.current as FragmentActivity, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onSuccess(true)
        }
    })
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { biometricPrompt.authenticate(promptInfo) }) {
            Text("Authenticate")
        }
    }
    biometricPrompt.authenticate(promptInfo)
}
