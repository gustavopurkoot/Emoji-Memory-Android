package com.example.emojimemory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emojimemory.data.local.Partida
import com.example.emojimemory.data.local.Usuario
import com.example.emojimemory.data.repository.PartidaRepository
import com.example.emojimemory.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Tudo que a tela do Administrador precisa para se desenhar
data class AdminUiState(
    val partidas: List<Partida> = emptyList(),   // as 10 partidas mais recentes
    val usuarios: List<Usuario> = emptyList(),
    val mensagem: String? = null
)

class AdminViewModel(
    private val partidaRepository: PartidaRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            partidaRepository.buscarTodos().collect { lista ->
                _uiState.update { estado ->
                    // id maior = partida mais recente
                    estado.copy(partidas = lista.sortedByDescending { it.id }.take(10))
                }
            }
        }
        viewModelScope.launch {
            usuarioRepository.buscarTodos().collect { lista ->
                _uiState.update { estado ->
                    estado.copy(usuarios = lista.sortedBy { it.nome.lowercase() })
                }
            }
        }
    }

    // UPDATE: o admin edita nome, e-mail e (opcionalmente) a senha de um usuário.
    // Se a senha nova vier vazia, a senha antiga é mantida.
    fun salvarUsuario(
        usuario: Usuario,
        nome: String,
        email: String,
        novaSenha: String,
        aoSalvar: () -> Unit
    ) {
        if (nome.isBlank() || email.isBlank()) {
            _uiState.update { it.copy(mensagem = "Nome e e-mail não podem ficar vazios.") }
            return
        }
        if (!email.contains("@")) {
            _uiState.update { it.copy(mensagem = "Digite um e-mail válido.") }
            return
        }

        val atualizado = usuario.copy(
            nome = nome.trim(),
            email = email.trim(),
            senha = if (novaSenha.isBlank()) usuario.senha else novaSenha
        )

        viewModelScope.launch {
            val erro = usuarioRepository.atualizar(atualizado)
            if (erro == null) {
                _uiState.update { it.copy(mensagem = "Usuário atualizado!") }
                aoSalvar()
            } else {
                _uiState.update { it.copy(mensagem = erro) }
            }
        }
    }

    fun limparMensagem() {
        _uiState.update { it.copy(mensagem = null) }
    }
}
