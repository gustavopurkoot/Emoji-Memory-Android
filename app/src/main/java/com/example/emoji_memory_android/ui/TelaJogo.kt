package com.example.emoji-memory-android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.emoji-memory-android.viewmodel.Carta
import com.example.emoji-memory-android.viewmodel.JogoViewModel

@Composable
fun TelaJogo(navController: NavController, jogoVm: JogoViewModel) {
    val estado = jogoVm.uiState.collectAsState().value

    LaunchedEffect(estado.terminou) {
        if (estado.terminou) {
            navController.navigate(Rotas.RESULTADO) {
                popUpTo(Rotas.JOGO) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoClaro)
    ) {
        BarraTopo("Jogo da Memória", navController)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🏆 ${estado.pontuacao} pts",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RoxoPrincipal
            )
            Text(
                text = "Tentativas: ${estado.tentativas}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoEscuro
            )
        }

        Text(
            text = "Categoria: ${estado.categoriaAtual} • encontre os pares!",
            fontSize = 14.sp,
            color = TextoCinza,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // tabuleiro
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(estado.cartas, key = { it.id }) { carta ->
                CartaView(carta) { jogoVm.virarCarta(carta.id) }
            }
        }
    }
}

@Composable
fun CartaView(carta: Carta, onClick: () -> Unit) {
    val mostrarEmoji = carta.virada || carta.encontrada
    val cor = when {
        carta.encontrada -> VerdeClaro
        carta.virada -> Color.White
        else -> RoxoPrincipal
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(cor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (mostrarEmoji) {
            Text(text = carta.emoji, fontSize = 30.sp)
        } else {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Carta virada para baixo",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
