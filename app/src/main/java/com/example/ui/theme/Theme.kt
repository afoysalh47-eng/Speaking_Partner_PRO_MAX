package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SpeakingPartnerColorScheme = lightColorScheme(
    primary = DeepPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = LavenderContainer,
    onPrimaryContainer = InkTextPrimary,
    secondary = FreshMintPrimary,
    onSecondary = Color.White,
    secondaryContainer = MintLightContainer,
    onSecondaryContainer = MintDarkText,
    tertiary = WarmAmberWarning,
    onTertiary = Color.White,
    tertiaryContainer = AmberLightContainer,
    onTertiaryContainer = AmberDarkText,
    background = LavenderMistBackground,
    onBackground = InkTextPrimary,
    surface = PureWhiteSurface,
    onSurface = InkTextPrimary,
    surfaceVariant = LavenderContainer,
    onSurfaceVariant = SlateTextSecondary,
    outline = MutedBorderViolet,
    error = CoralEndCall,
    onError = Color.White
)

@Composable
fun SpeakingPartnerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SpeakingPartnerColorScheme,
        typography = Typography,
        content = content
    )
}
