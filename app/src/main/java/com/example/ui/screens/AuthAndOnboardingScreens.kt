package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.data.firebase.attemptAutoSignIn
import com.example.data.firebase.onGoogleSignInClicked
import com.example.data.model.LearningGoal
import com.example.data.model.PreBuiltCatalog
import com.example.data.model.UserProfile
import com.example.ui.theme.DeepPurplePrimary
import com.example.ui.theme.FreshMintPrimary
import com.example.ui.theme.LavenderContainer
import com.example.ui.theme.LavenderMistBackground
import com.example.ui.theme.MintDarkText
import com.example.ui.theme.MintLightContainer
import com.example.ui.theme.RoyalPurpleAccent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        attemptAutoSignIn(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = onAuthSuccess,
            onUnauthenticated = {},
            scope = scope
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEDE9FE),
                        LavenderMistBackground,
                        Color.White
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = DeepPurplePrimary,
                shadowElevation = 10.dp,
                modifier = Modifier.size(88.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "AI Speaking Partner Logo",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                color = MintLightContainer,
                shape = RoundedCornerShape(50),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "PRO MAX • 12 MODULES EDITION",
                    style = MaterialTheme.typography.labelMedium,
                    color = MintDarkText,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Text(
                text = "AI Speaking Partner",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "মানুষের সামনে ইংরেজিতে কথা বলার ভয় বা জড়তা দূর করতে সরাসরি Face-to-Face 3D Video Call, বাংলা-টু-ইংলিশ মাইক, 3-Tone Rewriter এবং IELTS Examiner।",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Included Pro Capabilities",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepPurplePrimary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "60FPS 3D Video Call",
                            "Eye-Contact Camera PiP",
                            "BN বাংলা ➔ EN Mic",
                            "Photo Lip-Sync Clone",
                            "Voice Clone Cascade",
                            "ELSA Phoneme Coach",
                            "IELTS Band 4.5–8.5",
                            "3-Tone Vocab Quiz"
                        ).forEach { badge ->
                            Surface(
                                color = LavenderContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = badge,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepPurplePrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMessage != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    onGoogleSignInClicked(
                        context = context,
                        credentialManager = credentialManager,
                        onAuthSuccess = {
                            isLoading = false
                            onAuthSuccess()
                        },
                        onAuthError = { err ->
                            isLoading = false
                            errorMessage = err
                        },
                        scope = scope,
                        onAuthCancelled = {
                            isLoading = false
                        }
                    )
                },
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("google_sign_in_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Connecting to Google...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Google Sign-In",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign in with Google",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingStep1GoalsScreen(
    userProfile: UserProfile,
    onToggleGoal: (String) -> Unit,
    onSelectHelpLanguage: (String) -> Unit,
    onContinueClicked: () -> Unit,
    onSkipToHome: () -> Unit
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }
    val helpLanguages = listOf("Bangla", "English", "Hindi")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LavenderMistBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Step 1 of 3 • Onboarding",
                    style = MaterialTheme.typography.labelLarge,
                    color = DeepPurplePrimary
                )
                Text(
                    text = "Dashboard →",
                    style = MaterialTheme.typography.labelLarge,
                    color = RoyalPurpleAccent,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSkipToHome() }
                        .padding(6.dp)
                        .testTag("skip_onboarding_button")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { 0.33f },
                color = DeepPurplePrimary,
                trackColor = LavenderContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Choose Your Goals",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (userProfile.helpLanguage) {
                    "Hindi" -> "अपनी प्रैक्टिस को पर्सनलाइज़ करने के लिए अपने मुख्य इंग्लिश गोल्स चुनें (2×3 গ্রিড)।"
                    "English" -> "Select your primary speaking goals to personalize your 3D AI tutor plan."
                    else -> "আপনার ইংরেজি চর্চার মূল লক্ষ্যগুলো বেছে নিন (২×৩ গ্রিডে ৬টি গোল কার্ড)।"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 2x3 Grid of 6 Goal Cards
            val goals = PreBuiltCatalog.goals
            for (rowIndex in 0 until 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (colIndex in 0 until 2) {
                        val goal = goals[rowIndex * 2 + colIndex]
                        val isSelected = goal.id in userProfile.selectedGoals
                        GoalGridCard(
                            goal = goal,
                            helpLanguage = userProfile.helpLanguage,
                            isSelected = isSelected,
                            onClick = { onToggleGoal(goal.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Help Language Dropdown Card (Bangla, English, Hindi)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { languageMenuExpanded = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("help_language_dropdown"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Help Language",
                            tint = DeepPurplePrimary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Help language (সহায়ক ভাষা)",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "Used for grammar tips & bilingual hints",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box {
                        Surface(
                            color = LavenderContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = when (userProfile.helpLanguage) {
                                    "Bangla" -> "বাংলা / Bangla ▾"
                                    "Hindi" -> "हिन्दी / Hindi ▾"
                                    else -> "English ▾"
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = DeepPurplePrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = languageMenuExpanded,
                            onDismissRequest = { languageMenuExpanded = false }
                        ) {
                            helpLanguages.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            when (lang) {
                                                "Bangla" -> "বাংলা / Bangla"
                                                "Hindi" -> "हिन्दी / Hindi"
                                                else -> "English"
                                            }
                                        )
                                    },
                                    onClick = {
                                        onSelectHelpLanguage(lang)
                                        languageMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onContinueClicked,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_step1_continue_button")
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Continue",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun GoalGridCard(
    goal: LearningGoal,
    helpLanguage: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconVec = when (goal.iconName) {
        "school" -> Icons.Default.School
        "work" -> Icons.Default.WorkOutline
        "business" -> Icons.Default.BusinessCenter
        "flight" -> Icons.Default.FlightTakeoff
        "verified" -> Icons.Default.Verified
        else -> Icons.Default.ChatBubbleOutline
    }

    val localizedSubtitle = when (helpLanguage) {
        "Hindi" -> goal.hindiDesc
        "English" -> goal.englishDesc
        else -> goal.banglaDesc
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LavenderContainer else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) DeepPurplePrimary else MaterialTheme.colorScheme.outline
        ),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("goal_card_${goal.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) DeepPurplePrimary else LavenderContainer,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = iconVec,
                            contentDescription = goal.title,
                            tint = if (isSelected) Color.White else DeepPurplePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = FreshMintPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = goal.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = localizedSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3
            )
        }
    }
}

@Composable
fun OnboardingStep2LevelScreen(
    userProfile: UserProfile,
    onSelectLevel: (String) -> Unit,
    onStartLevelCheckCall: () -> Unit,
    onBuildMyPlanClicked: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val infiniteTransition = rememberInfiniteTransition(label = "tutorWave")
    val waveRadius by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waveScale"
    )

    val levels = listOf(
        Triple(
            "Beginner",
            "Beginner • A1–A2",
            "আমি ছোট ছোট বাক্যে কথা বলতে পারি কিন্তু মাঝে মাঝে শব্দ খুঁজে পেতে আটকে যাই।"
        ),
        Triple(
            "Intermediate",
            "Intermediate • B1–B2",
            "আমি দৈনন্দিন কথা বুঝতে পারি এবং মিটিং বা ইন্টারভিউতে আরও ন্যাচারাল হতে চাই।"
        ),
        Triple(
            "Advanced",
            "Advanced • C1–C2",
            "আমি সাবলীলভাবে কথা বলি, এখন Native Idioms এবং IELTS Band 7.5+ প্র্যাকটিস করতে চাই।"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LavenderMistBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "← Step 2 of 3 • Pick Your Level",
                    style = MaterialTheme.typography.labelLarge,
                    color = DeepPurplePrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onBack() }
                        .padding(4.dp)
                )
                FilterChip(
                    selected = true,
                    onClick = {},
                    label = { Text(userProfile.helpLanguage) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { 0.67f },
                color = DeepPurplePrimary,
                trackColor = LavenderContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Circular AI Tutor Portrait with Sound Waves
            Box(
                modifier = Modifier.size(124.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val baseR = size.minDimension / 2.4f
                    drawCircle(
                        color = Color(0xFF8B5CF6).copy(alpha = 0.22f),
                        radius = baseR * waveRadius
                    )
                    drawCircle(
                        color = Color(0xFF10B981).copy(alpha = 0.35f),
                        radius = baseR * (waveRadius * 0.92f),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = DeepPurplePrimary,
                    border = BorderStroke(3.dp, Color.White),
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(86.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "AI Tutor Sarah",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                            Text(
                                text = "Sarah",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pick Your Current Level",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "সাউন্ড ওয়েভসহ AI টিউটর আপনার লেভেল অনুযায়ী কথা বলার গতি ও শব্দ বেছে নেবে।",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            levels.forEach { (levelKey, title, desc) ->
                val isSelected = userProfile.englishLevel == levelKey
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) LavenderContainer else Color.White
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) DeepPurplePrimary else MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSelectLevel(levelKey) }
                        .testTag("level_card_$levelKey")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected Level",
                                tint = FreshMintPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                onClick = onStartLevelCheckCall,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, DeepPurplePrimary),
                contentPadding = PaddingValues(vertical = 14.dp, horizontal = 20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("take_level_check_button")
            ) {
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "2-Minute Level Check",
                    tint = DeepPurplePrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Take a 2-minute level check (Live Call)",
                    style = MaterialTheme.typography.titleSmall,
                    color = DeepPurplePrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onBuildMyPlanClicked,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("build_my_plan_button")
            ) {
                Text(
                    text = "Build my plan →",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}
