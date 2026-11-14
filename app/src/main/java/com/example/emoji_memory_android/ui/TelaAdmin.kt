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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emoji_memory_android.data.local.Usuario
import com.example.emoji_memory_android.viewmodel.AdminViewModel

@Composable
fun TelaAdmin(adminVm: AdminViewModel, isAdmin: Boolean) {
    val estado = adminVm.uiState.collectAsState().value

    var usuarioEditando by remember { mutableStateOf<Usuario?>(null) }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }

    if (!isAdmin) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FundoClaro),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Acesso restrito ao administrador.", color = TextoCinza)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoClaro)
    ) {
        CabecalhoRoxo("Painel ADM")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (estado.mensagem != null) {
                item {
                    Text(text = estado.mensagem, color = RoxoPrincipal, fontWeight = FontWeight.Bold)
                }
            }

            item {
                Text(
                    text = "Últimas Partidas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoEscuro
                )
                if (estado.partidas.isEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Nenhuma partida ainda.", fontSize = 14.sp, color = TextoCinza)
                }
            }
            items(estado.partidas, key = { "partida_${it.id}" }) { partida ->
                Cartao {
                    LinhaInfo(
                        "${partida.jogador} • ${partida.categoria}",
                        "${partida.pontuacao} pts"
                    )
                    Text(
                        text = "${partida.tentativas} tentativas",
                        fontSize = 12.sp,
                        color = TextoCinza
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Gestão de Usuários",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoEscuro
                )
            }
            items(estado.usuarios, key = { "usuario_${it.id}" }) { usuario ->
                Cartao {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (usuario.admin) "${usuario.nome} (admin)" else usuario.nome,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoEscuro
                            )
                            Text(text = usuario.email, fontSize = 13.sp, color = TextoCinza)
                        }
                        IconButton(
                            onClick = {
                                adminVm.limparMensagem()
                                nome = usuario.nome
                                email = usuario.email
                                novaSenha = ""
                                usuarioEditando = usuario
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar ${usuario.nome}",
                                tint = RoxoPrincipal
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }

    val editando = usuarioEditando
    if (editando != null) {
        AlertDialog(
            onDismissRequest = { usuarioEditando = null },
            title = { Text("Editar usuário") },
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
                    if (estado.mensagem != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = estado.mensagem, color = VermelhoErro, fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        adminVm.salvarUsuario(editando, nome, email, novaSenha) {
                            usuarioEditando = null
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { usuarioEditando = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
