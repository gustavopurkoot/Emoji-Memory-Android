package com.example.emoji_memory_android.data.repository
import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {

    @Insert
    suspend fun inserir(usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    fun buscarTodos(): Flow<List<Usuario>>

    // devolve null quando não existe usuário com esse nome
    @Query("SELECT * FROM usuarios WHERE nome = :nome LIMIT 1")
    suspend fun buscarPorNome(nome: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Usuario?

    // devolve null quando nome ou senha estão errados
    @Query("SELECT * FROM usuarios WHERE nome = :nome AND senha = :senha LIMIT 1")
    suspend fun buscarLogin(nome: String, senha: String): Usuario?

    @Update
    suspend fun atualizar(usuario: Usuario)

    @Delete
    suspend fun deletar(usuario: Usuario)
}
