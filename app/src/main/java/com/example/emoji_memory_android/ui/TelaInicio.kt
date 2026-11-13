package com.example.emoji_memory_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.emojimemory.viewmodel.JogoViewModel

@Composable
fun TelaInicio(navController: NavController, jogoVm: JogoViewModel) {
    val estado = jogoVm.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoClaro)
    ) {
        CabecalhoRoxo("Olá, ${estado.jogador}!")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(RoxoPrincipal)
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🏆", fontSize = 48.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Seu Rank", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text(
                        text = "${estado.pontosTotais} pts",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (estado.posicao > 0) "Posição #${estado.posicao} entre as partidas"
                        else "Jogue sua primeira partida!",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Como funciona",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoEscuro
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Cada partida tem 16 cartas e uma categoria de emojis sorteada. " +
                        "Cada par vale +100 pontos e cada erro tira 10. " +
                        "Toda partida entra na sua pontuação.",
                fontSize = 14.sp,
                color = TextoCinza
            )

            Spacer(modifier = Modifier.height(24.dp))

            BotaoRoxo(texto = "▶  JOGAR", cor = VerdeSucesso) {
                jogoVm.iniciarJogo()
                navController.navigate(Rotas.JOGO)
            }

            if (estado.avisoCategorias != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = estado.avisoCategorias, fontSize = 12.sp, color = TextoCinza)
            }
        }
    }
}
