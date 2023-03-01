@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.byowsigner.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.byowsigner.ui.domain.CreateWalletUIEvent
import com.example.byowsigner.ui.viewmodels.CreateWalletViewModel
import kotlinx.coroutines.launch

@Composable
fun ByowHome(modifier: Modifier = Modifier, createWalletViewModel: CreateWalletViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination?.route
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        createWalletViewModel.sharedEvent.collect { event ->
            when(event) {
                is CreateWalletUIEvent.CancelButtonClicked -> {
                    navController.navigateSingleTopTo(mainScreenRoute)
                }
                else -> {}
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            menuItems.forEach {
                MenuItem(
                    menuItemName = it.title,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigateSingleTopTo(it.route)
                    },
                    selected = it.route == currentDestination
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopBar { scope.launch { drawerState.open() } }
            }
        ) {
            NavHost(
                navController = navController,
                modifier = Modifier.padding(it),
                startDestination = mainScreenRoute
            ) {
                composable(route = mainScreenRoute) {
                    MainScreen()
                }
                composable(route = CreateWalletMenu.route) {
                    CreateWalletScreen(createWalletViewModel = createWalletViewModel)
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
