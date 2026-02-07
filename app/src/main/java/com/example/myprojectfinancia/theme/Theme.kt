package com.example.myprojectfinancia.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/*
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Background,
    secondaryContainer = SecondaryContainer,



)*/
/*
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40



    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)*/

private val LightColors = lightColorScheme(
    // 1. EL COLOR PRINCIPAL (Botones grandes, Barras de navegación activas)
    // Mantenemos tu azul oscuro elegante, pero el texto sobre él DEBE ser blanco.
    primary = Color(0xFF1D416C),
    onPrimary = Color.White,

    // 2. CONTENEDORES PRIMARIOS (Botones suaves, selecciones)
    // Un azul muy clarito derivado del principal
    primaryContainer = Color(0xFFD4E3FF),
    onPrimaryContainer = Color(0xFF001C3A), // Texto oscuro sobre azul clarito

    // 3. COLOR SECUNDARIO (Elementos de menor importancia)
    secondary = Color(0xFF535F70),
    onSecondary = Color.White,

    secondaryContainer = Color(0xFFD7E3F7),
    onSecondaryContainer = Color(0xFF101C2B),

    // 4. TERCIARIO (Acentos, gráficos)
    tertiary = Color(0xFF0276F5), // Tu azul brillante para destacar cosas pequeñas
    onTertiary = Color.White,

    // 5. FONDOS (La parte más importante del Light Mode)
    // background: El fondo general de la pantalla (gris azulado muy pálido, no gris sucio)
    background = Color(0xFFF8F9FF),
    onBackground = Color(0xFF1A1C1E), // Texto casi negro (mejor que gris puro)

    // surface: Las Tarjetas (Cards), BottomSheets, Dialogs
    // En light mode, las tarjetas deben ser BLANCAS o casi blancas para resaltar sobre el fondo.
    surface = Color(0xFFE0E2EC),
    onSurface = Color(0xFF1A1C1E), // El texto en las tarjetas debe ser oscuro

    // 6. VARIANTES (Campos de texto, bordes)
    surfaceVariant = Color(0xFFE0E2EC), // Gris suave para los inputs (OutlinedTextField)
    onSurfaceVariant = Color(0xFF44474F), // Texto de placeholders/iconos

    // 7. ERRORES
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

private val DarkColors = darkColorScheme(
    // 1. PRIMARY (MANTENIDO)
    primary = Color(0xFF1783F6),
    onPrimary = Color.White,

    // 2. PRIMARY CONTAINER (AJUSTADO)
    primaryContainer = Color(0xFF002F7F),
    onPrimaryContainer = Color(0xFFD1E4FF),

    // 3. SECONDARY (SINCRONIZADO)
    secondary = Color(0xFFB0C0D6),
    onSecondary = Color(0xFF1B2938),

    secondaryContainer = Color(0xFF324050),
    onSecondaryContainer = Color(0xFFD7E3F7),

    // 4. TERTIARY (MANTENIDO)
    tertiary = Color(0xFF3C95F4),
    onTertiary = Color.White,

    // 5. BACKGROUND (MANTENIDO - Es el alma de tu tema oscuro)
    background = Color(0xFF001021),
    onBackground = Color.White,

    // 6. SURFACE (SINCRONIZADO - El cambio más importante)
    surface = Color(0xFF121D2B),
    onSurface = Color.White,

    // 7. VARIANTES & ERRORES
    surfaceVariant = Color(0xFF252F3D),
    onSurfaceVariant = Color(0xFFC4C7D0),

    error = Color(0xFFCE1300),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun MyProjectFinanciaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}