package com.example.emoji_memory_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emoji_memory_android.data.repository.CategoriaRepository
import com.example.emoji_memory_android.data.repository.PartidaRepository
import com.example.emoji_memory_android.data.repository.UsuarioRepository

class ViewModelFactory(
    private val partidaRepository: PartidaRepository,
    private val categoriaRepository: CategoriaRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(JogoViewModel::class.java) ->
            JogoViewModel(partidaRepository, categoriaRepository, usuarioRepository) as T
        modelClass.isAssignableFrom(AdminViewModel::class.java) ->
            AdminViewModel(partidaRepository, usuarioRepository) as T
        else -> throw IllegalArgumentException("ViewModel desconhecido")
    }
}
