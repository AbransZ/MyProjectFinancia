package com.example.myprojectfinancia.theme

import android.app.Activity
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
    primary = Color(0xFF1D416C),
    onPrimary = Color.Black,
    secondary = Color(0xFF32455A),
    onSecondary = Color(0xFFCDCCCC),
    background = Color(0xFFECECEC),
    onBackground = Color.DarkGray,
    surface = Color(0xC97C7CF1),
    onSurface = Color.LightGray,
    primaryContainer = Color(0xAD8894F5),
    onPrimaryContainer = Color(0xFF0276F5),
    onErrorContainer = Color(0xF8E70A0A),
    errorContainer = Color(0xFF30669E),
    surfaceVariant = Color(0xFFB9B9E8),
    secondaryContainer = Color(0xAD338AF5)
)

// Esquema de colores para el modo oscuro
private val DarkColors = darkColorScheme(
    primary = Color(0xFF3C96F5),
    onPrimary = Color.White,
    secondary = Color(0xFF224B57),
    onSecondary = Color(0xFFB8B7B7),
    tertiary = Color(0xFF27409A),
    background = Color(0xFF001021),
    onBackground = Color.White,
    surface = Color(0xFF2C4468),
    onSurface = Color.White,
    primaryContainer = Color(0xFF21274B),
    onPrimaryContainer = Color(0xFF0276F5),
    onErrorContainer = Color(0xB7E80505),
    errorContainer = Color(0xFF30669E),
    surfaceVariant = Color(0xFF001021),
    secondaryContainer = Color(0x9A275ED9)


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