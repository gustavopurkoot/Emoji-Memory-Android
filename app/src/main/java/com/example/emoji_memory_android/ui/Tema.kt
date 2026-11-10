package com.example.emoji_memory_android.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun EmojiMemoryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = RoxoPrincipal,
            secondary = AzulSecundario
        ),
        content = content
    )
}