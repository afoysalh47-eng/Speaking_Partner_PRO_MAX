package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.example.data.model.ChatTurnMessage
import com.example.data.model.PracticeScenario
import com.example.data.model.StructuredAiTurnResponse
import com.example.data.model.TutorPersona
import com.example.data.voice.VisemeShape
import com.example.ui.theme.AmberLightContainer
import com.example.ui.theme.CallStageCardBg
import com.example.ui.theme.CallStageDarkBg
import com.example.ui.theme.CoralEndCall
import com.example.ui.theme.DeepPurplePrimary
import com.example.ui.theme.FreshMintPrimary
import com.example.ui.theme.LavenderContainer
import com.example.ui.theme.MintLightContainer
import com.example.ui.theme.SoftVioletAccent
import com.example.ui.theme.WarmAmberWarning
import kotlinx.coroutines.delay

@Composable
fun IncomingVideoCallScreen(
    scenario: PracticeScenario,
    tutor: TutorPersona,
    allTutors: List<TutorPersona>,
    onSelectTutor: (TutorPersona) -> Unit,
    onAnswerCall: () -> Unit,
    onDeclineCall: () -> Unit
) {
    BackHandler { onDeclineCall() }

    val infiniteTransition = rememberInfiniteTransition(label = "radarPulse")
    val pulse1 by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse1"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1F1638), CallStageDarkBg, Color(0xFF0D0B14))
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "INCOMING FACE-TO-FACE 3D VIDEO CALL",
                    style = MaterialTheme.typography.labelMedium,
                    color = MintLightContainer,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = scenario.title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${tutor.name} • ${tutor.roleTitle} (${tutor.accentLabel})",
                style = MaterialTheme.typography.bodyMedium,
                color = LavenderContainer.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Radar Pulsing Ring + Tutor Portrait
            Box(
                modifier = Modifier.size(220.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val baseR = size.minDimension / 3.2f
                    drawCircle(
                        color = FreshMintPrimary.copy(alpha = pulseAlpha),
                        radius = baseR * pulse1,
                        style = Stroke(width = 4.dp.toPx())
                    )
                    drawCircle(
                        color = SoftVioletAccent.copy(alpha = pulseAlpha * 0.8f),
                        radius = baseR * (pulse1 * 0.82f)
                    )
                }

                TutorAvatar3DCanvas(
                    tutor = tutor,
                    emotion = "happy",
                    viseme = VisemeShape.REST,
                    isSpeaking = false,
                    modifier = Modifier.size(136.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Switch Partner Before Answering:",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                allTutors.forEach { item ->
                    val selected = item.id == tutor.id
                    Surface(
                        color = if (selected) DeepPurplePrimary else Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            1.dp,
                            if (selected) FreshMintPrimary else Color.White.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onSelectTutor(item) }
                    ) {
                        Text(
                            text = "${item.name} (${item.accentLabel.substringBefore(" ")})",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDeclineCall,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.5.dp, CoralEndCall),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("decline_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "Decline Call",
                        tint = CoralEndCall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Decline",
                        style = MaterialTheme.typography.titleMedium,
                        color = CoralEndCall
                    )
                }

                Button(
                    onClick = onAnswerCall,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FreshMintPrimary),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    modifier = Modifier
                        .weight(1.4f)
                        .testTag("answer_video_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Answer Video Call",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Answer Video Call",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun Live3DVideoCallScreen(
    scenario: PracticeScenario,
    tutor: TutorPersona,
    callHistory: List<ChatTurnMessage>,
    latestCoaching: StructuredAiTurnResponse?,
    isBanglaMicActive: Boolean,
    isEyeContactPreviewOn: Boolean,
    isListening: Boolean,
    partialTranscript: String,
    isTutorSpeaking: Boolean,
    isAiThinking: Boolean,
    currentViseme: VisemeShape,
    callDurationSeconds: Long,
    sessionWordCount: Int,
    sessionFillerCount: Int,
    activeKeySlotLabel: String,
    activeVoiceCascadeLabel: String,
    onToggleBanglaMic: (Boolean) -> Unit,
    onToggleEyeContact: () -> Unit,
    onStartVoiceCapture: () -> Unit,
    onStopVoiceCapture: () -> Unit,
    onSubmitSpeechText: (String, Boolean) -> Unit,
    onSpeakPhrase: (String) -> Unit,
    onEndCall: () -> Unit
) {
    BackHandler { onEndCall() }

    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (granted) {
            onStartVoiceCapture()
        }
    }

    var manualInputText by remember { mutableStateOf("") }
    var showThreeTonesSheet by remember { mutableStateOf(true) }

    val mins = callDurationSeconds / 60L
    val secs = callDurationSeconds % 60L
    val formattedTimer = String.format("%02d:%02d", mins, secs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CallStageDarkBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
        ) {
            // Top Bar: Timer, Bilingual BN/EN Mic Toggle, and Failover Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = FreshMintPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "● LIVE $formattedTimer",
                            style = MaterialTheme.typography.labelMedium,
                            color = FreshMintPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = CallStageCardBg,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "Words: $sessionWordCount • Fillers: $sessionFillerCount",
                            style = MaterialTheme.typography.labelSmall,
                            color = LavenderContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                // Direct Bilingual BN বাংলা Mic / EN Mic Toggle Button (Section 6)
                Surface(
                    color = if (isBanglaMicActive) WarmAmberWarning else DeepPurplePrimary,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable { onToggleBanglaMic(!isBanglaMicActive) }
                        .testTag("bilingual_mic_toggle_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Toggle Bangla or English Mic",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBanglaMicActive) "BN বাংলা Mic ON" else "EN English Mic",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Multi-API Auto-Switch & Voice Cascade Telemetry Strip
            Text(
                text = "$activeKeySlotLabel • $activeVoiceCascadeLabel",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.55f),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Center 3D Avatar Stage + Top-Right Front Camera Eye-Contact PiP
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF31255A), CallStageCardBg, Color(0xFF151221))
                        )
                    )
            ) {
                // 60 FPS Matrix4 / graphicsLayer 3D Animated Tutor
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TutorAvatar3DCanvas(
                        tutor = tutor,
                        emotion = latestCoaching?.emotion ?: "encouraging",
                        viseme = currentViseme,
                        isSpeaking = isTutorSpeaking,
                        modifier = Modifier.size(195.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = Color.Black.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "${tutor.name} (${tutor.accentLabel}) • Mood: ${(latestCoaching?.emotion ?: "encouraging").uppercase()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MintLightContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                // Top-Right Eye-Contact ON Front Camera PiP Preview
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFF0F0D17),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (isEyeContactPreviewOn) FreshMintPrimary else Color.Gray
                    ),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(width = 106.dp, height = 138.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            if (!hasCameraPermission) {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            } else {
                                onToggleEyeContact()
                            }
                        }
                        .testTag("eye_contact_pip_box")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isEyeContactPreviewOn && hasCameraPermission) {
                            FrontCameraPreviewPiP(modifier = Modifier.fillMaxSize())
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isEyeContactPreviewOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                                    contentDescription = "Front Camera Eye-Contact PiP",
                                    tint = FreshMintPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (!hasCameraPermission) "Tap for Camera" else "Camera Paused",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Surface(
                            color = Color.Black.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = if (isEyeContactPreviewOn) "Eye-Contact ON" else "Eye-Contact OFF",
                                style = MaterialTheme.typography.labelSmall,
                                color = FreshMintPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Subtitle + Better Way & Bangla Grammar Explanation Banner + 3-Tone Rewriter
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CallStageCardBg),
                border = BorderStroke(1.dp, SoftVioletAccent.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(215.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    val latestTurn = callHistory.lastOrNull()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAiThinking) "AI Tutor is formulating 3-tone feedback..." else "${tutor.name} (Live Subtitle):",
                            style = MaterialTheme.typography.labelMedium,
                            color = MintLightContainer
                        )
                        if (latestCoaching != null) {
                            IconButton(
                                onClick = { onSpeakPhrase(latestCoaching.reply) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Replay tutor voice",
                                    tint = FreshMintPrimary
                                )
                            }
                        }
                    }

                    if (isAiThinking) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = FreshMintPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Generating reply, 3-Tone rewrites & Bangla grammar tip...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    } else {
                        Text(
                            text = latestCoaching?.reply ?: latestTurn?.text.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (latestCoaching != null) {
                        Spacer(modifier = Modifier.height(8.dp))

                        if (latestCoaching.betterWay.isNotBlank()) {
                            Surface(
                                color = FreshMintPrimary.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSpeakPhrase(latestCoaching.betterWay) }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "✓ Better Way (Say it like this — tap to hear):",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = FreshMintPrimary
                                    )
                                    Text(
                                        text = latestCoaching.betterWay,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        // 1-Line Bangla Grammar Tip
                        Surface(
                            color = AmberLightContainer.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "বাংলা টিপস: ${latestCoaching.banglaTip}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AmberLightContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // 3-Tone Rewriter Pills (Casual, Formal, Idiomatic)
                        AnimatedVisibility(visible = showThreeTonesSheet) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                ToneRewriteRow("Casual", latestCoaching.casualTone, onSpeakPhrase)
                                ToneRewriteRow("Formal", latestCoaching.formalTone, onSpeakPhrase)
                                ToneRewriteRow("Idiomatic", latestCoaching.idiomaticTone, onSpeakPhrase)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Ready-made Hint for Next Turn
                        Surface(
                            color = DeepPurplePrimary.copy(alpha = 0.45f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSubmitSpeechText(latestCoaching.hintForUser, false)
                                }
                                .testTag("use_hint_pill")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = "Hint for next turn",
                                    tint = WarmAmberWarning,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tap to say Hint: \"${latestCoaching.hintForUser}\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Speech / Bilingual Input Bar (also works alongside voice mic)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = if (isListening && partialTranscript.isNotBlank()) partialTranscript else manualInputText,
                    onValueChange = { manualInputText = it },
                    placeholder = {
                        Text(
                            if (isBanglaMicActive) "বাংলায় বলুন বা লিখুন (যেমন: আমি কালকে অফিসে যেতে পারিনি)..."
                            else "Speak with Mic or type your response...",
                            color = Color.White.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = CallStageCardBg,
                        unfocusedContainerColor = CallStageCardBg,
                        focusedBorderColor = FreshMintPrimary,
                        unfocusedBorderColor = SoftVioletAccent.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("call_speech_input_field")
                )

                IconButton(
                    onClick = {
                        if (manualInputText.isNotBlank()) {
                            onSubmitSpeechText(manualInputText, isBanglaMicActive)
                            manualInputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(DeepPurplePrimary)
                        .testTag("send_speech_turn_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Submit speech turn",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Call Controls: Mic Capture (EN / BN) + End Call
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (isListening) {
                            onStopVoiceCapture()
                        } else if (hasAudioPermission) {
                            onStartVoiceCapture()
                        } else {
                            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            isListening -> CoralEndCall
                            isBanglaMicActive -> WarmAmberWarning
                            else -> FreshMintPrimary
                        }
                    ),
                    contentPadding = PaddingValues(vertical = 14.dp, horizontal = 16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("live_call_mic_button")
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Microphone Speak Button",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isListening -> "Stop Listening..."
                            isBanglaMicActive -> "Speak in বাংলা (BN Mic)"
                            else -> "Tap & Speak English"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                }

                Button(
                    onClick = onEndCall,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralEndCall),
                    contentPadding = PaddingValues(vertical = 14.dp, horizontal = 18.dp),
                    modifier = Modifier.testTag("end_video_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "End",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ToneRewriteRow(
    toneLabel: String,
    toneText: String,
    onSpeak: (String) -> Unit
) {
    if (toneText.isBlank()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .clickable { onSpeak(toneText) }
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = DeepPurplePrimary,
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = toneLabel,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = toneText,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.92f),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TutorAvatar3DCanvas(
    tutor: TutorPersona,
    emotion: String,
    viseme: VisemeShape,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar3d")
    val headNodPitch by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpeaking) 900 else 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "headNod"
    )
    val headTiltYaw by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "headYaw"
    )

    var isBlinking by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3200L)
            isBlinking = true
            delay(150L)
            isBlinking = false
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                rotationX = headNodPitch
                rotationY = headTiltYaw
                cameraDistance = 14f * density
            },
        contentAlignment = Alignment.Center
    ) {
        if (tutor.isCustomFamiliar && !tutor.customPhotoUri.isNullOrBlank()) {
            // Custom Familiar Person Uploaded Photo + Calibrated Animated Lip-Sync Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = tutor.customPhotoUri,
                    contentDescription = tutor.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val mouthCenterY = size.height * tutor.mouthYRatio
                    val mouthW = size.width * tutor.mouthWidthRatio
                    val mouthH = when (viseme) {
                        VisemeShape.A -> size.height * 0.11f
                        VisemeShape.O -> size.height * 0.13f
                        VisemeShape.E -> size.height * 0.06f
                        VisemeShape.M -> size.height * 0.02f
                        VisemeShape.REST -> size.height * 0.025f
                    }
                    drawRoundRect(
                        color = Color(0xFF881337).copy(alpha = 0.85f),
                        topLeft = Offset((size.width - mouthW) / 2f, mouthCenterY - mouthH / 2f),
                        size = Size(mouthW, mouthH),
                        cornerRadius = CornerRadius(mouthH / 2f, mouthH / 2f)
                    )
                }
            }
        } else {
            // Built-in 60 FPS 3D Vector Avatar with Eye Blinking, Glasses/Beard, and A/O/E/M Lip-Sync
            Canvas(modifier = Modifier.fillMaxSize()) {
                val r = size.minDimension / 2f
                val center = Offset(size.width / 2f, size.height / 2f)

                // Outer glowing aura ring
                drawCircle(
                    color = if (isSpeaking) FreshMintPrimary.copy(alpha = 0.35f)
                    else Color(tutor.primaryColorHex).copy(alpha = 0.25f),
                    radius = r
                )

                // Background circle
                drawCircle(
                    color = Color(tutor.primaryColorHex),
                    radius = r * 0.88f,
                    center = center
                )

                // Hair / Top silhouette
                drawCircle(
                    color = Color(0xFF1E1B4B),
                    radius = r * 0.56f,
                    center = Offset(center.x, center.y - r * 0.12f)
                )

                // Face skin oval
                drawOval(
                    color = Color(0xFFFDE68A),
                    topLeft = Offset(center.x - r * 0.44f, center.y - r * 0.44f),
                    size = Size(r * 0.88f, r * 0.96f)
                )

                // Eyebrows based on emotion (happy, encouraging, curious, proud)
                val browLift = if (emotion == "curious" || emotion == "proud") -r * 0.04f else 0f
                val eyeY = center.y - r * 0.08f
                val leftEyeX = center.x - r * 0.18f
                val rightEyeX = center.x + r * 0.18f

                drawLine(
                    color = Color(0xFF1E1B4B),
                    start = Offset(leftEyeX - r * 0.09f, eyeY - r * 0.12f + browLift),
                    end = Offset(leftEyeX + r * 0.09f, eyeY - r * 0.14f + browLift),
                    strokeWidth = 3.dp.toPx()
                )
                drawLine(
                    color = Color(0xFF1E1B4B),
                    start = Offset(rightEyeX - r * 0.09f, eyeY - r * 0.14f + browLift),
                    end = Offset(rightEyeX + r * 0.09f, eyeY - r * 0.12f + browLift),
                    strokeWidth = 3.dp.toPx()
                )

                // Eyes (Blinking vs Open)
                if (isBlinking) {
                    drawLine(
                        color = Color(0xFF1E1B4B),
                        start = Offset(leftEyeX - r * 0.07f, eyeY),
                        end = Offset(leftEyeX + r * 0.07f, eyeY),
                        strokeWidth = 3.dp.toPx()
                    )
                    drawLine(
                        color = Color(0xFF1E1B4B),
                        start = Offset(rightEyeX - r * 0.07f, eyeY),
                        end = Offset(rightEyeX + r * 0.07f, eyeY),
                        strokeWidth = 3.dp.toPx()
                    )
                } else {
                    drawCircle(
                        color = Color(0xFF1E1B4B),
                        radius = r * 0.055f,
                        center = Offset(leftEyeX, eyeY)
                    )
                    drawCircle(
                        color = Color(0xFF1E1B4B),
                        radius = r * 0.055f,
                        center = Offset(rightEyeX, eyeY)
                    )
                }

                // Glasses for Alex & Prof. James
                if (tutor.hasGlasses) {
                    drawCircle(
                        color = Color(0xFF1E293B),
                        radius = r * 0.12f,
                        center = Offset(leftEyeX, eyeY),
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                    drawCircle(
                        color = Color(0xFF1E293B),
                        radius = r * 0.12f,
                        center = Offset(rightEyeX, eyeY),
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                    drawLine(
                        color = Color(0xFF1E293B),
                        start = Offset(leftEyeX + r * 0.12f, eyeY),
                        end = Offset(rightEyeX - r * 0.12f, eyeY),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Optional Beard for Alex
                if (tutor.hasBeard) {
                    val beardPath = Path().apply {
                        moveTo(center.x - r * 0.36f, center.y + r * 0.12f)
                        quadraticTo(
                            center.x,
                            center.y + r * 0.62f,
                            center.x + r * 0.36f,
                            center.y + r * 0.12f
                        )
                    }
                    drawPath(
                        path = beardPath,
                        color = Color(0xFF334155).copy(alpha = 0.45f)
                    )
                }

                // Dynamic Viseme Mouth (A, O, E, M, REST)
                val mouthY = center.y + r * 0.22f
                when (viseme) {
                    VisemeShape.A -> {
                        drawRoundRect(
                            color = Color(0xFF9F1239),
                            topLeft = Offset(center.x - r * 0.16f, mouthY - r * 0.08f),
                            size = Size(r * 0.32f, r * 0.18f),
                            cornerRadius = CornerRadius(14f, 14f)
                        )
                    }
                    VisemeShape.O -> {
                        drawCircle(
                            color = Color(0xFF9F1239),
                            radius = r * 0.10f,
                            center = Offset(center.x, mouthY)
                        )
                    }
                    VisemeShape.E -> {
                        drawRoundRect(
                            color = Color(0xFF9F1239),
                            topLeft = Offset(center.x - r * 0.20f, mouthY - r * 0.04f),
                            size = Size(r * 0.40f, r * 0.09f),
                            cornerRadius = CornerRadius(10f, 10f)
                        )
                    }
                    VisemeShape.M, VisemeShape.REST -> {
                        val smilePath = Path().apply {
                            moveTo(center.x - r * 0.16f, mouthY)
                            quadraticTo(
                                center.x,
                                mouthY + r * 0.10f,
                                center.x + r * 0.16f,
                                mouthY
                            )
                        }
                        drawPath(
                            path = smilePath,
                            color = Color(0xFF9F1239),
                            style = Stroke(width = 3.5.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FrontCameraPreviewPiP(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        preview
                    )
                } catch (_: Exception) {
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = modifier
    )
}
