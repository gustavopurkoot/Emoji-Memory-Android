package com.example.emoji_memory_android.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.emojimemory.ui.RoxoPrincipal

data class ItemAba(
    val rota: String,
    val rotulo: String,
    val icone: ImageVector,
    val somenteAdmin: Boolean = false
)

val abas = listOf(
    ItemAba(Rotas.INICIO, "Início", Icons.Default.Home),
    ItemAba(Rotas.PERFIL, "Perfil", Icons.Default.Person),
    ItemAba(Rotas.ADMIN, "Admin", Icons.Default.Build, somenteAdmin = true)
)

@Composable
fun BottomNavBar(navController: NavHostController, isAdmin: Boolean) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    // bifurcação de perfil: a aba Admin só existe para o administrador
    val abasVisiveis = abas.filter { !it.somenteAdmin || isAdmin }

    NavigationBar {
        abasVisiveis.forEach { aba ->
            NavigationBarItem(
                selected = rotaAtual == aba.rota,
                onClick = {
                    navController.navigate(aba.rota) {
                        popUpTo(Rotas.INICIO) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(aba.icone, contentDescription = aba.rotulo) },
                label = { Text(aba.rotulo) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    indicatorColor = RoxoPrincipal
                )
            )
        }
    }
}
