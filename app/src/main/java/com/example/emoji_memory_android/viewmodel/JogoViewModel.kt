package com.example.emoji-memory-android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emoji-memory-android.data.local.Categoria
import com.example.emoji-memory-android.data.local.Partida
import com.example.emoji-memory-android.data.local.Usuario
import com.example.emoji-memory-android.data.repository.CategoriaRepository
import com.example.emoji-memory-android.data.repository.PartidaRepository
import com.example.emoji-memory-android.data.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PARES = 8

private val EMOJIS_PADRAO = listOf(
    "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐨", "🐯", "🦁", "🐸"
)

data class Carta(
    val id: Int,
    val emoji: String,
    val virada: Boolean = false,
    val encontrada: Boolean = false
)

data class JogoUiState(
    // tela-login
    val logado: Boolean = false,
    val usuarioId: Int = 0,
    val jogador: String = "",
    val email: String = "",
    val isAdmin: Boolean = false,
    val erroLogin: String? = null,
    val erroPerfil: String? = null,
    val mensagemLogin: String? = null,

    // tela-novas-contas
    val erroCadastro: String? = null,
    val contaCriada: Boolean = false,

    // dados-room
    val partidas: List<Partida> = emptyList(),
    val categorias: List<Categoria> = emptyList(),
    val avisoCategorias: String? = null,

    // logica-jogo-andando
    val categoriaAtual: String = "",
    val cartas: List<Carta> = emptyList(),
    val tentativas: Int = 0,
    val pontuacao: Int = 0,
    val bloqueado: Boolean = false,
    val terminou: Boolean = false,
    val mensagem: String? = null
) {
    // lista-partidas
    val minhasPartidas: List<Partida>
        get() = partidas.filter { it.usuarioId == usuarioId }

    val pontosTotais: Int
        get() = minhasPartidas.sumOf { it.pontuacao }

    // posição da minha melhor partida entre todas (0 = ainda não joguei)
    val posicao: Int
        get() = partidas.indexOfFirst { it.usuarioId == usuarioId } + 1
}

class JogoViewModel(
    private val partidaRepository: PartidaRepository,
    private val categoriaRepository: CategoriaRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JogoUiState())
    val uiState: StateFlow<JogoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            partidaRepository.buscarTodos().collect { lista ->
                _uiState.update { estado ->
                    estado.copy(partidas = lista.sortedByDescending { it.pontuacao })
                }
            }
        }
        viewModelScope.launch {
            categoriaRepository.buscarTodos().collect { lista ->
                _uiState.update { it.copy(categorias = lista) }
            }
        }
       
        viewModelScope.launch {
            val erroAdmin = usuarioRepository.garantirAdmin()
            val erroBanco = categoriaRepository.prepararCategorias()
            val erroApi = categoriaRepository.importarCategoriasDaApi()
            val aviso = erroAdmin ?: erroBanco ?: erroApi
            if (aviso != null) {
                _uiState.update { it.copy(avisoCategorias = aviso) }
            }
        }
    }

    // login-e-novas-contas

    fun entrar(usuario: String, senha: String) {
        val nome = usuario.trim()
        if (nome.isBlank() || senha.isBlank()) {
            _uiState.update { it.copy(erroLogin = "Preencha usuário e senha.", mensagemLogin = null) }
            return
        }
        viewModelScope.launch {
            val resultado = usuarioRepository.login(nome, senha)
            val encontrado = resultado.usuario

            if (resultado.erro != null) {
                _uiState.update { it.copy(erroLogin = resultado.erro, mensagemLogin = null) }
            } else if (encontrado == null) {
                _uiState.update {
                    it.copy(erroLogin = "Usuário ou senha incorretos.", mensagemLogin = null)
                }
            } else {
                _uiState.update {
                    it.copy(
                        logado = true,
                        usuarioId = encontrado.id,
                        jogador = encontrado.nome,
                        email = encontrado.email,
                        isAdmin = encontrado.admin,
                        erroLogin = null,
                        mensagemLogin = null
                    )
                }
            }
        }
    }

    fun criarConta(nome: String, email: String, senha: String, confirmarSenha: String) {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim()

        val erro = when {
            nomeLimpo.isBlank() || emailLimpo.isBlank() || senha.isBlank() ->
                "Preencha todos os campos."
            !emailLimpo.contains("@") ->
                "Digite um e-mail válido."
            senha.length < 4 ->
                "A senha precisa ter pelo menos 4 caracteres."
            senha != confirmarSenha ->
                "As senhas não são iguais."
            else -> null
        }
        if (erro != null) {
            _uiState.update { it.copy(erroCadastro = erro) }
            return
        }

        viewModelScope.launch {
            val erroBanco = usuarioRepository.inserir(
                Usuario(nome = nomeLimpo, email = emailLimpo, senha = senha)
            )
            if (erroBanco != null) {
                _uiState.update { it.copy(erroCadastro = erroBanco) }
            } else {
                _uiState.update {
                    it.copy(
                        erroCadastro = null,
                        contaCriada = true,
                        mensagemLogin = "Conta criada com sucesso! Faça login."
                    )
                }
            }
        }
    }

    fun consumirContaCriada() {
        _uiState.update { it.copy(contaCriada = false) }
    }

    fun limparErros() {
        _uiState.update { it.copy(erroLogin = null, erroCadastro = null, erroPerfil = null) }
    }

    // edição-de-perfil
    fun editarPerfil(nome: String, email: String, novaSenha: String, aoSalvar: () -> Unit) {
        val nomeLimpo = nome.trim()
        val emailLimpo = email.trim()

        if (nomeLimpo.isBlank() || emailLimpo.isBlank()) {
            _uiState.update { it.copy(erroPerfil = "Nome e e-mail não podem ficar vazios.") }
            return
        }
        if (!emailLimpo.contains("@")) {
            _uiState.update { it.copy(erroPerfil = "Digite um e-mail válido.") }
            return
        }
        if (novaSenha.isNotBlank() && novaSenha.length < 4) {
            _uiState.update { it.copy(erroPerfil = "A senha precisa ter pelo menos 4 caracteres.") }
            return
        }

        viewModelScope.launch {
            val erro = usuarioRepository.atualizarPerfil(
                _uiState.value.usuarioId, nomeLimpo, emailLimpo, novaSenha
            )
            if (erro == null) {
                _uiState.update {
                    it.copy(jogador = nomeLimpo, email = emailLimpo, erroPerfil = null)
                }
                aoSalvar()
            } else {
                _uiState.update { it.copy(erroPerfil = erro) }
            }
        }
    }

    fun sair() {
        _uiState.update {
            it.copy(
                logado = false,
                usuarioId = 0,
                jogador = "",
                email = "",
                isAdmin = false,
                erroLogin = null,
                erroPerfil = null,
                mensagemLogin = null,
                cartas = emptyList(),
                terminou = false,
                mensagem = null
            )
        }
    }

    // logica-do-mini-jogo

    private fun listaDeEmojis(categoria: Categoria): List<String> {
        return categoria.emojis.split(" ").filter { it.isNotBlank() }.distinct()
    }

    fun iniciarJogo() {
        val estado = _uiState.value

        // randomização-do-jogo
        val sorteada = estado.categorias
            .filter { listaDeEmojis(it).size >= PARES }
            .randomOrNull()

        val lista = if (sorteada != null) listaDeEmojis(sorteada) else EMOJIS_PADRAO
        val nomeCategoria = sorteada?.nome ?: "Padrão"

        val escolhidos = lista.shuffled().take(PARES)
        
        val baralho = (escolhidos + escolhidos)
            .shuffled()
            .mapIndexed { indice, emoji -> Carta(id = indice, emoji = emoji) }

        _uiState.update {
            it.copy(
                categoriaAtual = nomeCategoria,
                cartas = baralho,
                tentativas = 0,
                pontuacao = 0,
                bloqueado = false,
                terminou = false,
                mensagem = null
            )
        }
    }

    fun virarCarta(id: Int) {
        val estado = _uiState.value
        if (estado.bloqueado) return

        val carta = estado.cartas.find { it.id == id } ?: return
        if (carta.virada || carta.encontrada) return

        val cartas = estado.cartas.map { if (it.id == id) it.copy(virada = true) else it }
        val viradas = cartas.filter { it.virada && !it.encontrada }

        if (viradas.size < 2) {
            // primeira carta da tentativa
            _uiState.update { it.copy(cartas = cartas) }
            return
        }

        // checagem-cartas
        _uiState.update {
            it.copy(cartas = cartas, tentativas = it.tentativas + 1, bloqueado = true)
        }
        viewModelScope.launch {
            delay(700)
            conferirPar()
        }
    }

    private fun conferirPar() {
        val estado = _uiState.value
        val viradas = estado.cartas.filter { it.virada && !it.encontrada }
        val acertou = viradas.size == 2 && viradas[0].emoji == viradas[1].emoji

        val cartas = estado.cartas.map { carta ->
            when {
                carta.virada && !carta.encontrada && acertou -> carta.copy(encontrada = true)
                carta.virada && !carta.encontrada -> carta.copy(virada = false)
                else -> carta
            }
        }

        // pontos
        val pontos = if (acertou) estado.pontuacao + 100 else maxOf(0, estado.pontuacao - 10)
        val terminou = cartas.all { it.encontrada }

        _uiState.update {
            it.copy(cartas = cartas, pontuacao = pontos, bloqueado = false, terminou = terminou)
        }

        if (terminou) salvarPartida()
    }

    // salvamento-das-partidas
    private fun salvarPartida() {
        val estado = _uiState.value
        viewModelScope.launch {
            val erro = partidaRepository.inserir(
                Partida(
                    usuarioId = estado.usuarioId,
                    jogador = estado.jogador,
                    pontuacao = estado.pontuacao,
                    tentativas = estado.tentativas,
                    categoria = estado.categoriaAtual
                )
            )
            if (erro != null) {
                _uiState.update { it.copy(mensagem = erro) }
            }
        }
    }
}
