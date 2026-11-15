package com.example.emoji_memory_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.emoji_memory_android.data.local.AppDatabase
import com.example.emoji_memory_android.data.remote.EmojiApi
import com.example.emoji_memory_android.data.repository.CategoriaRepository
import com.example.emoji_memory_android.data.repository.PartidaRepository
import com.example.emoji_memory_android.data.repository.UsuarioRepository
import com.example.emoji_memory_android.ui.EmojiMemoryTheme
import com.example.emoji_memory_android.ui.TelaPrincipal
import com.example.emoji_memory_android.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        val partidaRepo = PartidaRepository(db.partidaDAO())
        val categoriaRepo = CategoriaRepository(db.categoriaDAO(), EmojiApi.service)
        val usuarioRepo = UsuarioRepository(db.usuarioDAO())
        val factory = ViewModelFactory(partidaRepo, categoriaRepo, usuarioRepo)

        setContent {
            EmojiMemoryTheme {
                TelaPrincipal(factory)
            }
        }
    }
}
