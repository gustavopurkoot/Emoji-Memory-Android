package com.example.emoji_memory_android.data.local

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidaDAO {

    @Insert
    suspend fun inserir(partida: Partida)

    @Query("SELECT * FROM partidas")
    fun buscarTodos(): Flow<List<Partida>>

    @Update
    suspend fun atualizar(partida: Partida)

    @Delete
    suspend fun deletar(partida: Partida)
}
