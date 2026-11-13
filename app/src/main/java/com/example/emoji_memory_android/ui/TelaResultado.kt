package com.example.emojimemory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.emojimemory.viewmodel.JogoViewModel

@Composable
fun TelaResultado(navController: NavController, jogoVm: JogoViewModel) {
    val estado = jogoVm.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoClaro)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🏆", fontSize = 80.sp)
        Text(
            text = "Parabéns!",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = RoxoPrincipal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Você fez ${estado.tentativas} tentativas!",
            fontSize = 18.sp,
            color = TextoEscuro
        )

        Spacer(modifier = Modifier.height(20.dp))

        Cartao {
            Text(
                text = "${estado.pontuacao} pts",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = RoxoPrincipal,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = estado.mensagem ?: "Pontuação salva no seu perfil!",
            fontSize = 13.sp,
            color = if (estado.mensagem != null) VermelhoErro else TextoCinza
        )

        Spacer(modifier = Modifier.height(24.dp))

        BotaoRoxo(texto = "JOGAR NOVAMENTE") {
            jogoVm.iniciarJogo()
            // abre um jogo novo e remove o resultado da pilha (o Início continua embaixo)
            navController.navigate(Rotas.JOGO) {
                popUpTo(Rotas.INICIO)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        BotaoRoxo(texto = "VOLTAR AO MENU", cor = AzulSecundario) {
            navController.popBackStack(Rotas.INICIO, false)
        }
    }
}
