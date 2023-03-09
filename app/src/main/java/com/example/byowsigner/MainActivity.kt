package com.example.byowsigner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.byowsigner.ui.screen.ByowHome
import com.example.byowsigner.ui.theme.BYOWSignerTheme
import com.example.byowsigner.ui.viewmodels.CreateWalletViewModel
import com.example.byowsigner.ui.viewmodels.ExportWatchOnlyWalletViewModel
import com.example.byowsigner.ui.viewmodels.SignTransactionViewModel
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())

        val createWalletViewModel: CreateWalletViewModel by viewModels { CreateWalletViewModel.Factory }
        val signTransactionViewModel: SignTransactionViewModel by viewModels { SignTransactionViewModel.Factory }
        val exportWatchOnlyWalletViewModel: ExportWatchOnlyWalletViewModel by viewModels { ExportWatchOnlyWalletViewModel.Factory }

        setContent {
            BYOWSignerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ByowHome(
                        createWalletViewModel = createWalletViewModel,
                        signTransactionViewModel = signTransactionViewModel,
                        exportWatchOnlyWalletViewModel = exportWatchOnlyWalletViewModel
                    )
                }
            }
        }
    }
}
