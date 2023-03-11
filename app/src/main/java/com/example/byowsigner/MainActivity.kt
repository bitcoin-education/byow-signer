package com.example.byowsigner

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.byowsigner.ui.screen.LoginScreen
import com.example.byowsigner.ui.theme.BYOWSignerTheme
import com.example.byowsigner.ui.viewmodels.CreateWalletViewModel
import com.example.byowsigner.ui.viewmodels.ExportWatchOnlyWalletViewModel
import com.example.byowsigner.ui.viewmodels.SignTransactionViewModel
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
        setupAuthentication()

        val createWalletViewModel: CreateWalletViewModel by viewModels { CreateWalletViewModel.Factory }
        val signTransactionViewModel: SignTransactionViewModel by viewModels { SignTransactionViewModel.Factory }
        val exportWatchOnlyWalletViewModel: ExportWatchOnlyWalletViewModel by viewModels { ExportWatchOnlyWalletViewModel.Factory }

        setContent {
            BYOWSignerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        createWalletViewModel = createWalletViewModel,
                        signTransactionViewModel = signTransactionViewModel,
                        exportWatchOnlyWalletViewModel = exportWatchOnlyWalletViewModel
                    )
                }
            }
        }
    }

    private fun setupAuthentication() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, DEVICE_CREDENTIAL)
                }
                startActivityForResult(enrollIntent, 1234)
            }
            else -> {}
        }
    }

}
