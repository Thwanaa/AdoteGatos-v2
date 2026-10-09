package com.tauana.adotegatos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaEscuro = darkColorScheme(
    primary = Laranja,
    secondary = LaranjaClaro,
    tertiary = LaranjaEscuro
)

@Composable
fun AdoteGatosTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = EsquemaEscuro

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}