package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItem(menuItemName: String, onClick: () -> Unit, selected: Boolean) {
    NavigationDrawerItem(
        label = { Text(menuItemName) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

object CreateWalletMenu : MenuItem {
    override val route: String = "create_wallet"
    override val title: String = "New Wallet"
}
object SignTransactionMenu : MenuItem {
    override val route: String = "sign_transaction"
    override val title: String = "Sign Transaction"
}

interface MenuItem {
    val route: String
    val title: String
}

val menuItems = listOf(CreateWalletMenu, SignTransactionMenu)