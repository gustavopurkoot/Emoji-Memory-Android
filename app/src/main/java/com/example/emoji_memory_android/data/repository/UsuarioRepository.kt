package com.example.emoji_memory_android.data.repository

import com.example.emoji_memory_android.data.local.Usuario
import com.example.emoji_memory_android.data.local.UsuarioDAO
import kotlinx.coroutines.flow.Flow

data class ResultadoLogin(
    val usuario: Usuario? = null,
    val erro: String? = null
)

class UsuarioRep5ository(private val dao: UsuarioDAO) {

    fun buscarTodos(): Flow<List<Usuario>> = dao.buscarTodos()

    suspend fun login(nome: String, senha: String): ResultadoLogin {
        return try {
            ResultadoLogin(usuario = dao.buscarLogin(nome, senha))
        } catch (e: Exception) {
            ResultadoLogin(erro = "Erro ao acessar o banco de dados.")
        }
    }

    suspend fun inserir(usuario: Usuario): String? {
        return try {
            if (dao.buscarPorNome(usuario.nome) != null) {
                "Esse nome de usuário já existe."
            } else {
                dao.inserir(usuario)
                null
            }
        } catch (e: Exception) {
            "Não foi possível criar a conta."
        }
    }

    suspend fun atualizar(usuario: Usuario): String? {
        return try {
            val existente = dao.buscarPorNome(usuario.nome)
            if (existente != null && existente.id != usuario.id) {
                "Esse nome de usuário já está em uso."
            } else {
                dao.atualizar(usuario)
                null
            }
        } catch (e: Exception) {
            "Não foi possível atualizar o usuário."
        }
    }

    suspend fun atualizarPerfil(id: Int, nome: String, email: String, novaSenha: String): String? {
        return try {
            val atual = dao.buscarPorId(id)
            if (atual == null) {
                "Usuário não encontrado."
            } else {
                atualizar(
                    atual.copy(
                        nome = nome,
                        email = email,
                        senha = if (novaSenha.isBlank()) atual.senha else novaSenha
                    )
                )
            }
        } catch (e: Exception) {
            "Não foi possível atualizar o perfil."
        }
    }

    suspend fun garantirAdmin(): String? {
        return try {
            if (dao.buscarPorNome("admin") == null) {
                dao.inserir(
                    Usuario(nome = "admin", email = "admin@email.com", senha = "admin", admin = true)
                )
            }
            null
        } catch (e: Exception) {
            "Não foi possível preparar o administrador."
        }
    }
}

