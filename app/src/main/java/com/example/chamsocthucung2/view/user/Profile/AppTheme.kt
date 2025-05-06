package com.example.chamsocthucung2.view.user.Profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFA500),
    background = Color(0xFFFFE4B5),
    onPrimary = Color.White,
    onBackground = Color.Black,
    // Thêm các màu khác nếu cần
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFC107),
    background = Color(0xFF121212),
    onPrimary = Color.Black,
    onBackground = Color.White,
    // Thêm các màu khác nếu cần
)

private val PetCareShapes = Shapes(
    medium = RoundedCornerShape(16.dp)
)

@Composable
fun PetCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PetCareTypography,
        shapes = PetCareShapes,
        content = content
    )
}
