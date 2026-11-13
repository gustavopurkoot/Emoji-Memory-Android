package com.example.emoji_memory_android.data.repository

import com.example.emoji_memory_android.data.local.Categoria
import com.example.emoji_memory_android.data.local.CategoriaDAO
import com.example.emoji_memory_android.data.remote.EmojiApiService
import kotlinx.coroutines.flow.Flow

data class ResultadoEmojis(
    val emojis: List<String> = emptyList(),
    val erro: String? = null
)

private val CATEGORIAS_PADRAO = listOf(
    Categoria(nome = "Animais", emojis = "🐶 🐱 🐭 🐹 🐰 🦊 🐻 🐼 🐨 🐯"),
    Categoria(nome = "Comidas", emojis = "🍎 🍌 🍇 🍓 🍕 🍔 🍟 🍩 🍪 🍦"),
    Categoria(nome = "Esportes", emojis = "⚽ 🏀 🏈 ⚾ 🎾 🏐 🏉 🎱 🏓 🥊"),
    Categoria(nome = "Veículos", emojis = "🚗 🚕 🚌 🚓 🚑 🚒 🚜 🚲 🚂 🚀"),
    Categoria(nome = "Natureza", emojis = "🌵 🌲 🌴 🌸 🌻 🍀 🍄 🌈 🌙 🔥"),
    Categoria(nome = "Carinhas", emojis = "😀 😂 😍 😎 😴 😡 😱 🤔 🥳 😇")
)

private val GRUPOS_API = listOf(
    "Animais (API)" to "animals-and-nature",
    "Comidas (API)" to "food-and-drink",
    "Viagens (API)" to "travel-and-places",
    "Atividades (API)" to "activities"
)

class CategoriaRepository(
    private val dao: CategoriaDAO,
    private val api: EmojiApiService
) {

    fun buscarTodos(): Flow<List<Categoria>> = dao.buscarTodos()

    // Cadastra as categorias locais que ainda não existem no banco
    suspend fun prepararCategorias(): String? {
        return try {
            for (categoria in CATEGORIAS_PADRAO) {
                if (dao.contarPorNome(categoria.nome) == 0) {
                    dao.inserir(categoria)
                }
            }
            null
        } catch (e: Exception) {
            "Não foi possível carregar as categorias."
        }
    }


    suspend fun importarCategoriasDaApi(): String? {
        return try {
            var erro: String? = null
            for ((nome, grupo) in GRUPOS_API) {
                if (dao.contarPorNome(nome) > 0) continue

                val resultado = buscarEmojisDaApi(grupo)
                if (resultado.erro != null) {
                    erro = resultado.erro
                    break
                }
                dao.inserir(Categoria(nome = nome, emojis = resultado.emojis.joinToString(" ")))
            }
            erro
        } catch (e: Exception) {
            "Não foi possível salvar as categorias da API."
        }
    }

    private suspend fun buscarEmojisDaApi(grupo: String): ResultadoEmojis {
        return try {
            val emojis = api.buscarPorCategoria(grupo)
                .filter { it.unicode.size == 1 }
                .map { converterParaEmoji(it.unicode[0]) }
                .distinct()
                .shuffled()
                .take(10)

            if (emojis.size < 8) {
                ResultadoEmojis(erro = "A API não devolveu emojis suficientes.")
            } else {
                ResultadoEmojis(emojis = emojis)
            }
        } catch (e: Exception) {
            ResultadoEmojis(erro = "Sem conexão com a API de emojis: usando só as categorias do app.")
        }
    }

    private fun converterParaEmoji(codigo: String): String {
        val numero = codigo.removePrefix("U+").toInt(16)
        return String(Character.toChars(numero))
    }
}
