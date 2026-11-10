package com.example.emojimemory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Faixa roxa de título usada nas telas das abas
@Composable
fun CabecalhoRoxo(titulo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(RoxoPrincipal)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// TopAppBar com botão de voltar funcional (popBackStack)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraTopo(titulo: String, navController: NavController) {
    TopAppBar(
        title = { Text(titulo, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        // o Scaffold da TelaPrincipal já reserva o espaço da barra de status
        windowInsets = WindowInsets(0, 0, 0, 0),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = RoxoPrincipal,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
fun BotaoRoxo(
    texto: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cor: Color = RoxoPrincipal,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = cor)
    ) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

// Linha "rótulo ........ valor" usada no perfil
@Composable
fun LinhaInfo(rotulo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = rotulo, fontSize = 15.sp, color = TextoCinza)
        Text(text = valor, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextoEscuro)
    }
}

// Caixa branca arredondada que agrupa conteúdo
@Composable
fun Cartao(modifier: Modifier = Modifier, conteudo: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        conteudo()
    }
}
