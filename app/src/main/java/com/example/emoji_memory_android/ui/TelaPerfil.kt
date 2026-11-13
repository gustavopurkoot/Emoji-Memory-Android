package com.example.emoji_memory_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.emojimemory.viewmodel.JogoViewModel

@Composable
fun TelaPerfil(navController: NavController, jogoVm: JogoViewModel) {
    val estado = jogoVm.uiState.collectAsState().value
    val minhas = estado.minhasPartidas

    var editando by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }

    val conquistas = listOf(
        Triple("🥇", "Primeira partida", minhas.isNotEmpty()),
        Triple("🏅", "5 partidas jogadas", minhas.size >= 5),
        Triple("🏆", "500 pontos somados", estado.pontosTotais >= 500)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoClaro)
    ) {
        CabecalhoRoxo("Meu Perfil")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(RoxoPrincipal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = estado.jogador.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = estado.jogador,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoEscuro
                    )
                    Text(
                        text = if (estado.isAdmin) "Administrador" else "Jogador",
                        fontSize = 13.sp,
                        color = TextoCinza
                    )
                    Text(text = estado.email, fontSize = 13.sp, color = TextoCinza)
                }
            }

            item {
                Cartao {
                    LinhaInfo("Pontos totais", "${estado.pontosTotais}")
                    LinhaInfo(
                        "Posição",
                        if (estado.posicao > 0) "#${estado.posicao}" else "-"
                    )
                    LinhaInfo("Jogos jogados", "${minhas.size}")
                    LinhaInfo(
                        "Melhor pontuação",
                        "${minhas.maxOfOrNull { it.pontuacao } ?: 0}"
                    )
                }
            }

            item {
                Text(
                    text = "Conquistas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoEscuro
                )
                Spacer(modifier = Modifier.height(8.dp))
                Cartao {
                    conquistas.forEach { (icone, nome, desbloqueada) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .alpha(if (desbloqueada) 1f else 0.3f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = icone, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = nome,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoEscuro
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Minhas últimas partidas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoEscuro
                )
                if (minhas.isEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Você ainda não jogou nenhuma partida.",
                        fontSize = 14.sp,
                        color = TextoCinza
                    )
                }
            }
            items(minhas.sortedByDescending { it.id }.take(5), key = { it.id }) { partida ->
                Cartao {
                    LinhaInfo(
                        "${partida.categoria} • ${partida.tentativas} tentativas",
                        "${partida.pontuacao} pts"
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                BotaoRoxo(texto = "EDITAR PERFIL", cor = AzulSecundario) {
                    // abre o diálogo já preenchido com os dados atuais
                    jogoVm.limparErros()
                    nome = estado.jogador
                    email = estado.email
                    novaSenha = ""
                    editando = true
                }
                Spacer(modifier = Modifier.height(8.dp))
                BotaoRoxo(texto = "SAIR DA CONTA", cor = VermelhoErro) {
                    jogoVm.sair()
                    navController.navigate(Rotas.LOGIN) {
                        popUpTo(Rotas.INICIO) { inclusive = true }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (editando) {
        AlertDialog(
            onDismissRequest = { editando = false },
            title = { Text("Editar perfil") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = novaSenha,
                        onValueChange = { novaSenha = it },
                        label = { Text("Nova senha (opcional)") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (estado.erroPerfil != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = estado.erroPerfil, color = VermelhoErro, fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        jogoVm.editarPerfil(nome, email, novaSenha) { editando = false }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { editando = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
