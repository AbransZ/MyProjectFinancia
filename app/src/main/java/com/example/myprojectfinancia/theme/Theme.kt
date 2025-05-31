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
    primary = Color(0xFF3C96F5),
    onPrimary = Color.White,
    secondary = Color(0xFF5588C7),
    onSecondary = Color.Black,
    background = Color(0xFFECECEC),
    onBackground = Color.DarkGray,
    surface = Color.White,
    onSurface = Color.Black,
    primaryContainer = Color(0xFF8894F5),
    onPrimaryContainer = Color(0xFF0276F5)
)

// Esquema de colores para el modo oscuro
private val DarkColors = darkColorScheme(
    primary = Color(0xFF3C96F5),
    onPrimary = Color.White,
    secondary = Color(0xFF488AC9),
    onSecondary = Color.White,
    background = Color(0xFF001021),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    primaryContainer = Color(0xFF21274B),
    onPrimaryContainer = Color(0xFF0276F5)

)

@Composable
fun MyProjectFinanciaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    /*val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }*/
    val colorScheme = if(darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}