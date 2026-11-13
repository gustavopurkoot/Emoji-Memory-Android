package com.example.emoji_memory_android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emojimemory.viewmodel.AdminViewModel
import com.example.emojimemory.viewmodel.JogoViewModel
import com.example.emojimemory.viewmodel.ViewModelFactory

@Composable
fun TelaPrincipal(factory: ViewModelFactory) {
    val navController = rememberNavController()
    val jogoVm: JogoViewModel = viewModel(factory = factory)
    val adminVm: AdminViewModel = viewModel(factory = factory)
    val isAdmin = jogoVm.uiState.collectAsState().value.isAdmin

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    val mostrarBarra = abas.any { it.rota == rotaAtual }

    Scaffold(
        containerColor = FundoClaro,
        bottomBar = { if (mostrarBarra) BottomNavBar(navController, isAdmin) }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            jogoVm = jogoVm,
            adminVm = adminVm,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
