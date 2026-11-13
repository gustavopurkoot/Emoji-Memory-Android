package com.example.emoji_memory_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.emojimemory.viewmodel.JogoViewModel

@Composable
fun TelaLogin(navController: NavController, jogoVm: JogoViewModel) {
    val estado = jogoVm.uiState.collectAsState().value
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    LaunchedEffect(estado.logado) {
        if (estado.logado) {
            navController.navigate(Rotas.INICIO) {
                popUpTo(Rotas.LOGIN) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoClaro)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🧠", fontSize = 72.sp)
        Text(
            text = "Emoji Memory",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = RoxoPrincipal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Faça login para começar a jogar!",
            fontSize = 15.sp,
            color = TextoCinza
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuário") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        if (estado.mensagemLogin != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = estado.mensagemLogin, color = VerdeSucesso, fontSize = 14.sp)
        }
        if (estado.erroLogin != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = estado.erroLogin, color = VermelhoErro, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        BotaoRoxo(texto = "Entrar") {
            jogoVm.entrar(usuario, senha)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { navController.navigate(Rotas.CRIAR_CONTA) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar Conta", fontWeight = FontWeight.Bold, color = RoxoPrincipal)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Administrador padrão: usuário admin, senha admin.",
            fontSize = 12.sp,
            color = TextoCinza
        )
    }
}
