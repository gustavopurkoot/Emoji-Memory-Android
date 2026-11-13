package com.example.emoji_memory_android.data.repository

import com.example.emoji_memory_android.data.local.Partida
import com.example.emoji_memory_android.data.local.PartidaDAO
import kotlinx.coroutines.flow.Flow

class PartidaRepository(private val dao: PartidaDAO) {

    fun buscarTodos(): Flow<List<Partida>> = dao.buscarTodos()

    suspend fun inserir(partida: Partida): String? {
        return try {
            dao.inserir(partida)
            null
        } catch (e: Exception) {
            "Não foi possível salvar a partida."
        }
    }
}
