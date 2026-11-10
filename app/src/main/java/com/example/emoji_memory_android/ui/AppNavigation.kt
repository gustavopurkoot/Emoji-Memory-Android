package com.example.emoji_memory_android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emojimemory.viewmodel.AdminViewModel
import com.example.emojimemory.viewmodel.JogoViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    jogoVm: JogoViewModel,
    adminVm: AdminViewModel,
    modifier: Modifier = Modifier
) {
    val isAdmin = jogoVm.uiState.collectAsState().value.isAdmin

    NavHost(
        navController = navController,
        startDestination = Rotas.LOGIN,
        modifier = modifier
    ) {
        composable(Rotas.LOGIN) { TelaLogin(navController, jogoVm) }

        composable(Rotas.CRIAR_CONTA) { TelaCriarConta(navController, jogoVm) }

        composable(Rotas.INICIO) { TelaInicio(navController, jogoVm) }

        composable(Rotas.JOGO) { TelaJogo(navController, jogoVm) }

        composable(Rotas.RESULTADO) { TelaResultado(navController, jogoVm) }

        composable(Rotas.PERFIL) { TelaPerfil(navController, jogoVm) }

        composable(Rotas.ADMIN) { TelaAdmin(adminVm, isAdmin) }
    }
}
