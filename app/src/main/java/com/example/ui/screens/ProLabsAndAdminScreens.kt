package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.data.model.CustomTopic
import com.example.data.model.FamiliarPerson
import com.example.data.model.IeltsBandEvaluation
import com.example.data.model.IeltsCueCard
import com.example.data.model.PatternDrill
import com.example.data.model.PhonemeChallenge
import com.example.data.model.PreBuiltCatalog
import com.example.data.model.TutorPersona
import com.example.data.model.UserProfile
import com.example.data.model.VocabEntry
import com.example.data.model.WordPronunciationScore
import com.example.data.voice.VisemeShape
import com.example.ui.theme.AmberDarkText
import com.example.ui.theme.AmberLightContainer
import com.example.ui.theme.DeepPurplePrimary
import com.example.ui.theme.FreshMintPrimary
import com.example.ui.theme.LavenderContainer
import com.example.ui.theme.LavenderMistBackground
import com.example.ui.theme.MintDarkText
import com.example.ui.theme.MintLightContainer
import com.example.ui.theme.RoyalPurpleAccent
import com.example.ui.theme.WarmAmberWarning

// --- MODULES 07 & 08: CUSTOM PHOTO UPLOAD & LIP-SYNC + FREE VOICE CLONING ---
@Composable
fun FamiliarCloneAndVoiceScreen(
    familiarPeople: List<FamiliarPerson>,
    currentViseme: VisemeShape,
    isTutorSpeaking: Boolean,
    onSaveFamiliarPerson: (String, String, String, Long, Long, String, String?, String?) -> Unit,
    onDeleteFamiliarPerson: (String) -> Unit,
    onTestVoiceAndLips: (String, String) -> Unit,
    onStartCallWithPerson: (FamiliarPerson) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var name by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("Boss / Manager") }
    var accent by remember { mutableStateOf("US") }
    var voiceEngine by remember { mutableStateOf("HuggingFace") }
    var mouthYSlider by remember { mutableFloatStateOf(64f) }
    var mouthWidthSlider by remember { mutableFloatStateOf(38f) }
    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }
    var voiceClipStatus by remember { mutableStateOf("Ready: 15s Zero-Shot Audio Reference") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
        }
    }

    val previewTutor = TutorPersona(
        id = "preview_clone",
        name = name.ifBlank { "Familiar Person" },
        roleTitle = relationship,
        accentLabel = "$accent Accent",
        languageTag = if (accent == "UK") "en-GB" else "en-US",
        banglaBio = "",
        primaryColorHex = 0xFF6D28D9,
        secondaryColorHex = 0xFFEDE9FE,
        isCustomFamiliar = selectedPhotoUri != null,
        customPhotoUri = selectedPhotoUri,
        mouthYRatio = mouthYSlider / 100f,
        mouthWidthRatio = mouthWidthSlider / 100f,
        voiceEngine = voiceEngine
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
                .widthIn(max = 640.dp)
        ) {
            LabTopHeader(
                title = "Photo Lip-Sync & Voice Cloning",
                subtitle = "Modules 07 & 08 • পরিচিত মানুষের ছবি ও কণ্ঠস্বর ক্লোন",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TutorAvatar3DCanvas(
                        tutor = previewTutor,
                        emotion = "happy",
                        viseme = if (isTutorSpeaking) currentViseme else VisemeShape.A,
                        isSpeaking = isTutorSpeaking,
                        modifier = Modifier.size(140.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("pick_familiar_photo_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Pick Gallery Photo"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload Gallery Photo")
                        }

                        Button(
                            onClick = {
                                onTestVoiceAndLips(
                                    "Hello! My lip-sync calibration and cloned voice are ready for our video call.",
                                    previewTutor.languageTag
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FreshMintPrimary),
                            modifier = Modifier.testTag("test_lip_sync_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Test Lip-Sync"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test Lip-Sync")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Lip-Sync Calibration Sliders
                    Text(
                        text = "Mouth Vertical Position (ঠোঁটের অবস্থান): ${mouthYSlider.toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Slider(
                        value = mouthYSlider,
                        onValueChange = { mouthYSlider = it },
                        valueRange = 35f..82f
                    )

                    Text(
                        text = "Mouth Width Scale (ঠোঁটের প্রস্থ): ${mouthWidthSlider.toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Slider(
                        value = mouthWidthSlider,
                        onValueChange = { mouthWidthSlider = it },
                        valueRange = 18f..65f
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Familiar Person Name (e.g., Rahim Sir, My Boss)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("familiar_name_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = relationship,
                        onValueChange = { relationship = it },
                        label = { Text("Relationship / Role (Friend, Boss, Teacher)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Multi-Engine Voice Cloning Cascade (Module 08):",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("HuggingFace", "FishAudio", "ElevenLabs", "DeviceTTS").forEach { eng ->
                            FilterChip(
                                selected = voiceEngine == eng,
                                onClick = { voiceEngine = eng },
                                label = { Text(eng) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("US", "UK", "AU").forEach { acc ->
                            FilterChip(
                                selected = accent == acc,
                                onClick = { accent = acc },
                                label = { Text("$acc Accent") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onSaveFamiliarPerson(
                                name.ifBlank { "Familiar Partner" },
                                relationship,
                                accent,
                                mouthYSlider.toLong(),
                                mouthWidthSlider.toLong(),
                                voiceEngine,
                                selectedPhotoUri,
                                voiceClipStatus
                            )
                            name = ""
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_familiar_person_button")
                    ) {
                        Text("Save Familiar Person Profile to Cloud")
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Saved Familiar Person Profiles (${familiarPeople.size})",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            familiarPeople.forEach { person ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${person.name} (${person.relationship})",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${person.accent} Accent • Engine: ${person.voiceEngine} • Mouth Y: ${person.mouthYRatio}%",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row {
                            IconButton(onClick = { onStartCallWithPerson(person) }) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = "Video Call with Familiar Person",
                                    tint = FreshMintPrimary
                                )
                            }
                            IconButton(onClick = { onDeleteFamiliarPerson(person.id) }) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- MODULE 09: PRONUNCIATION COACH LAB (PHONEME ENGINE) ---
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PronunciationCoachLabScreen(
    challenges: List<PhonemeChallenge>,
    selectedChallenge: PhonemeChallenge,
    wordScores: List<WordPronunciationScore>,
    isListening: Boolean,
    onSelectChallenge: (PhonemeChallenge) -> Unit,
    onSpeakTargetSentence: (String) -> Unit,
    onStartPronunciationCapture: () -> Unit,
    onEvaluateManualAttempt: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val audioLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (granted) onStartPronunciationCapture()
    }

    var testAttemptInput by remember(selectedChallenge.id) {
        mutableStateOf(selectedChallenge.sentence)
    }

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
                .widthIn(max = 640.dp)
        ) {
            LabTopHeader(
                title = "Pronunciation Coach Lab",
                subtitle = "Module 09 • Word-by-Word সবুজ/কমলা কালারিং, IPA ও বাংলা টিপস",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                challenges.forEach { ch ->
                    FilterChip(
                        selected = ch.id == selectedChallenge.id,
                        onClick = { onSelectChallenge(ch) },
                        label = { Text(ch.focusSound) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MintLightContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = selectedChallenge.focusSound,
                                style = MaterialTheme.typography.labelMedium,
                                color = MintDarkText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        IconButton(
                            onClick = { onSpeakTargetSentence(selectedChallenge.sentence) },
                            modifier = Modifier.testTag("listen_phoneme_sentence_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Listen to native pronunciation",
                                tint = DeepPurplePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = selectedChallenge.sentence,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "IPA: ${selectedChallenge.ipaTranscription}",
                        style = MaterialTheme.typography.labelMedium,
                        color = DeepPurplePrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = LavenderContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectedChallenge.banglaArticulationTip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (hasAudioPermission) {
                                    onStartPronunciationCapture()
                                } else {
                                    audioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("pronunciation_mic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Speak Sentence"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isListening) "Listening..." else "Speak Aloud")
                        }

                        OutlinedButton(
                            onClick = { onEvaluateManualAttempt(testAttemptInput) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("check_pronunciation_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Check Phonemes"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Check Word Coloring")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = testAttemptInput,
                        onValueChange = { testAttemptInput = it },
                        label = { Text("Spoken Transcript to Analyze (Word-by-Word)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (wordScores.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Word-by-Word Phoneme Accuracy (সবুজ = সঠিক, কমলা = অনুশীলন দরকার):",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            wordScores.forEach { ws ->
                                Surface(
                                    color = if (ws.isAccurate) MintLightContainer else AmberLightContainer,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.clickable { onSpeakTargetSentence(ws.word) }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = ws.word,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (ws.isAccurate) MintDarkText else AmberDarkText
                                        )
                                        Text(
                                            text = ws.ipa,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (ws.isAccurate) MintDarkText else AmberDarkText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- MODULE 10: IELTS SPEAKING MOCK EXAMINER SUITE ---
@Composable
fun IeltsMockExaminerScreen(
    cueCards: List<IeltsCueCard>,
    selectedCard: IeltsCueCard,
    currentPart: Int,
    transcripts: List<String>,
    evaluation: IeltsBandEvaluation?,
    isEvaluating: Boolean,
    isListening: Boolean,
    onSelectCard: (IeltsCueCard) -> Unit,
    onSelectPart: (Int) -> Unit,
    onCaptureVoiceResponse: () -> Unit,
    onSubmitTextResponse: (String) -> Unit,
    onSpeakPrompt: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var draftResponse by remember { mutableStateOf("") }

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
                .widthIn(max = 640.dp)
        ) {
            LabTopHeader(
                title = "IELTS Speaking Mock Examiner",
                subtitle = "Module 10 • Prof. James (UK) • Part 1, 2, 3 & Band Score (4.5–8.5)",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Part 1, Part 2 (2-Min Cue Card), Part 3 selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    1 to "Part 1: Intro",
                    2 to "Part 2: 2-Min Cue Card",
                    3 to "Part 3: Discussion"
                ).forEach { (partNum, label) ->
                    FilterChip(
                        selected = currentPart == partNum,
                        onClick = { onSelectPart(partNum) },
                        label = { Text(label) },
                        modifier = Modifier.testTag("ielts_part_chip_$partNum")
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    when (currentPart) {
                        1 -> {
                            Text(
                                text = "Part 1 — Examiner Interview Questions",
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepPurplePrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            selectedCard.part1Questions.forEach { q ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "• $q",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = { onSpeakPrompt(q) }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = "Speak question",
                                            tint = DeepPurplePrimary
                                        )
                                    }
                                }
                            }
                        }
                        2 -> {
                            Text(
                                text = "Part 2 — Official 2-Minute Cue Card",
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepPurplePrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = selectedCard.cueCardTopic,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            selectedCard.bulletPoints.forEach { bp ->
                                Text(
                                    text = "  • $bp",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        else -> {
                            Text(
                                text = "Part 3 — Two-Way Abstract Discussion",
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepPurplePrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            selectedCard.part3Questions.forEach { q ->
                                Text(
                                    text = "• $q",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = draftResponse,
                        onValueChange = { draftResponse = it },
                        label = { Text("Speak or type your IELTS response for Band Evaluation") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ielts_response_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = onCaptureVoiceResponse,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FreshMintPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = "Record")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isListening) "Listening..." else "Speak Answer")
                        }

                        Button(
                            onClick = {
                                val textToEval = draftResponse.ifBlank {
                                    "Recently I learned how to communicate fluently in professional meetings through daily practice."
                                }
                                onSubmitTextResponse(textToEval)
                                draftResponse = ""
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("evaluate_ielts_band_button")
                        ) {
                            Text("Get Band Score")
                        }
                    }
                }
            }

            if (isEvaluating) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Prof. James is calculating your Estimated Band Score (4.5–8.5)...")
                }
            }

            if (evaluation != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, DeepPurplePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estimated IELTS Band Score",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Surface(
                                color = DeepPurplePrimary,
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = "Band ${evaluation.overallBand}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Fluency: ${evaluation.fluencyBand} • Lexical: ${evaluation.lexicalBand} • Grammar: ${evaluation.grammarBand} • Pronunciation: ${evaluation.pronunciationBand}",
                            style = MaterialTheme.typography.labelLarge,
                            color = RoyalPurpleAccent
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = evaluation.examinerFeedback,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = MintLightContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "বাংলা টিপস: ${evaluation.banglaImprovementPlan}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MintDarkText,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- MODULE 11: 3-TONE VOCABULARY BANK & RECALL QUIZ ---
@Composable
fun VocabBankAndQuizScreen(
    vocabList: List<VocabEntry>,
    onSpeakSentence: (String) -> Unit,
    onAddCustomSentence: (String, String, String, String, String, String) -> Unit,
    onQuizAnswer: (String, Long, Boolean) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var isQuizMode by remember { mutableStateOf(false) }
    var quizIndex by remember { mutableIntStateOf(0) }
    var revealAnswer by remember { mutableStateOf(false) }

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
                .widthIn(max = 640.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LabTopHeader(
                    title = "3-Tone Vocab Bank & Quiz",
                    subtitle = "Module 11 • Casual, Business Formal & Native Idiomatic",
                    onBack = onBack,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        isQuizMode = !isQuizMode
                        revealAnswer = false
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isQuizMode) FreshMintPrimary else DeepPurplePrimary
                    ),
                    modifier = Modifier.testTag("quiz_me_toggle_button")
                ) {
                    Icon(imageVector = Icons.Default.Quiz, contentDescription = "Quiz Me")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isQuizMode) "View Bank" else "Quiz Me")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isQuizMode && vocabList.isNotEmpty()) {
                val activeCard = vocabList[quizIndex % vocabList.size]
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, DeepPurplePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Flashcard Recall Quiz (${(quizIndex % vocabList.size) + 1}/${vocabList.size})",
                            style = MaterialTheme.typography.labelLarge,
                            color = DeepPurplePrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "বাংলা অর্থ: ${activeCard.banglaMeaning}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Key Phrase: ${activeCard.vocabHighlight}",
                            style = MaterialTheme.typography.titleMedium,
                            color = FreshMintPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (revealAnswer) {
                            Surface(
                                color = LavenderContainer,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("• Casual: ${activeCard.casualTone}", style = MaterialTheme.typography.bodyMedium)
                                    Text("• Business Formal: ${activeCard.formalTone}", style = MaterialTheme.typography.bodyMedium)
                                    Text("• Native Idiomatic: ${activeCard.idiomaticTone}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        onQuizAnswer(activeCard.id, activeCard.masteryLevel, false)
                                        revealAnswer = false
                                        quizIndex++
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Need Practice")
                                }
                                Button(
                                    onClick = {
                                        onQuizAnswer(activeCard.id, activeCard.masteryLevel, true)
                                        revealAnswer = false
                                        quizIndex++
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = FreshMintPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("I Got It! (+1)")
                                }
                            }
                        } else {
                            Button(
                                onClick = { revealAnswer = true },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reveal 3-Tone English Answers")
                            }
                        }
                    }
                }
            } else {
                vocabList.forEach { item ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = MintLightContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = item.vocabHighlight,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MintDarkText,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                IconButton(onClick = { onSpeakSentence(item.idiomaticTone) }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Speak",
                                        tint = DeepPurplePrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "বাংলা: ${item.banglaMeaning}",
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepPurplePrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("• Casual: ${item.casualTone}", style = MaterialTheme.typography.bodySmall)
                            Text("• Business Formal: ${item.formalTone}", style = MaterialTheme.typography.bodySmall)
                            Text("• Native Idiomatic: ${item.idiomaticTone}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

// --- SPEAK PATTERN DRILLS & PRAKTIKA STAR CV LAB ---
@Composable
fun PatternDrillsAndStarCvScreen(
    drills: List<PatternDrill>,
    selectedDrill: PatternDrill,
    starCoachingOutput: String?,
    isGeneratingStar: Boolean,
    onSelectDrill: (PatternDrill) -> Unit,
    onSpeakSample: (String) -> Unit,
    onGenerateStarCoaching: (String, String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var cvOrJdText by remember {
        mutableStateOf("Role: Product Manager / Software Engineer. Key skills: Leadership, Agile delivery, cross-functional communication.")
    }
    var candidateDraft by remember {
        mutableStateOf("In my last project our server went down before launch, so I coordinated with 3 engineers and fixed the database bottleneck in 4 hours.")
    }

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
                .widthIn(max = 640.dp)
        ) {
            LabTopHeader(
                title = "Speak Pattern Drills & STAR CV Lab",
                subtitle = "Speak AI Pattern Drills + Praktika AI Career Interview Lab",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                drills.forEach { d ->
                    FilterChip(
                        selected = d.id == selectedDrill.id,
                        onClick = { onSelectDrill(d) },
                        label = { Text(d.patternHeader) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = selectedDrill.patternHeader,
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepPurplePrimary
                    )
                    Text(
                        text = selectedDrill.banglaMeaning,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• Casual: ${selectedDrill.exampleCasual}", style = MaterialTheme.typography.bodySmall)
                    Text("• Formal: ${selectedDrill.exampleFormal}", style = MaterialTheme.typography.bodySmall)
                    Text("• Idiomatic: ${selectedDrill.exampleIdiomatic}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { onSpeakSample(selectedDrill.exampleCasual) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Listen to Pattern Drill")
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Praktika AI STAR Method CV & Job Interview Rewriter
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Praktika AI — STAR Method CV & Salary Lab",
                        style = MaterialTheme.typography.titleLarge,
                        color = DeepPurplePrimary
                    )
                    Text(
                        text = "আপনার সিভি বা জব ডেসক্রিপশন পেস্ট করুন এবং আপনার উত্তরকে STAR মেথডে (Situation, Task, Action, Result) রূপান্তর করুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = cvOrJdText,
                        onValueChange = { cvOrJdText = it },
                        label = { Text("Paste CV Summary or Job Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = candidateDraft,
                        onValueChange = { candidateDraft = it },
                        label = { Text("Your Raw Experience / Interview Answer") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onGenerateStarCoaching(cvOrJdText, candidateDraft) },
                        enabled = !isGeneratingStar,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_star_cv_button")
                    ) {
                        if (isGeneratingStar) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generating STAR Interview Script...")
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "STAR")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Transform into STAR Method Answer")
                        }
                    }

                    if (!starCoachingOutput.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = LavenderContainer,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = starCoachingOutput,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- MODULE 12: CLOUD WEB ADMIN & IN-APP AUTO-UPDATER (OTA) ---
@Composable
fun CloudAdminAndOtaScreen(
    userProfile: UserProfile,
    customTopics: List<CustomTopic>,
    currentAppVersionCode: Long,
    onPublishTopic: (String, String, String, String) -> Unit,
    onDeleteTopic: (String) -> Unit,
    onSaveOtaAndKeys: (String, Long, String, String, String, String, String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var topicTitle by remember { mutableStateOf("") }
    var topicBadge by remember { mutableStateOf("Easy") }
    var topicCategory by remember { mutableStateOf("Everyday English") }
    var topicPrompt by remember { mutableStateOf("") }

    var bannerText by remember(userProfile.homeBannerText) { mutableStateOf(userProfile.homeBannerText) }
    var latestVersionStr by remember(userProfile.latestVersionCode) {
        mutableStateOf(userProfile.latestVersionCode.toString())
    }
    var geminiKey1 by remember(userProfile.backupGeminiKey1) { mutableStateOf(userProfile.backupGeminiKey1) }
    var geminiKey2 by remember(userProfile.backupGeminiKey2) { mutableStateOf(userProfile.backupGeminiKey2) }
    var fishKey1 by remember(userProfile.fishAudioKey1) { mutableStateOf(userProfile.fishAudioKey1) }
    var fishKey2 by remember(userProfile.fishAudioKey2) { mutableStateOf(userProfile.fishAudioKey2) }
    var elevenKey by remember(userProfile.elevenLabsKey) { mutableStateOf(userProfile.elevenLabsKey) }

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
                .widthIn(max = 640.dp)
        ) {
            LabTopHeader(
                title = "Cloud Web Admin & OTA System",
                subtitle = "Module 12 • Live Topic Publisher, Multi-API Keys & APK Auto-Updater",
                onBack = onBack
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Level 1 OTA: Publish New Practice Topic Without APK Rebuild
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Level 1 OTA — Publish Live Practice Topic",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepPurplePrimary
                    )
                    Text(
                        text = "APK ডাউনলোড ছাড়াই সাথে সাথে নতুন Practice Topic যোগ করুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = topicTitle,
                        onValueChange = { topicTitle = it },
                        label = { Text("Topic Title (e.g., Hotel Room Upgrade)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_topic_title_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Easy", "Intermediate").forEach { b ->
                            FilterChip(
                                selected = topicBadge == b,
                                onClick = { topicBadge = b },
                                label = { Text("$b Badge") }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = topicPrompt,
                        onValueChange = { topicPrompt = it },
                        label = { Text("AI Tutor Roleplay Instructions") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_topic_prompt_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (topicTitle.isNotBlank()) {
                                onPublishTopic(
                                    topicTitle,
                                    topicBadge,
                                    topicCategory,
                                    topicPrompt.ifBlank { "Roleplay '${topicTitle}' naturally with the user." }
                                )
                                topicTitle = ""
                                topicPrompt = ""
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FreshMintPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("publish_cloud_topic_button")
                    ) {
                        Icon(imageVector = Icons.Default.CloudUpload, contentDescription = "Publish")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publish Live Topic to Cloud")
                    }

                    if (customTopics.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        customTopics.forEach { t ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("• ${t.title} (${t.badgeLevel})", style = MaterialTheme.typography.bodySmall)
                                IconButton(onClick = { onDeleteTopic(t.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete topic",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Level 2 OTA Version Trigger & Multi-API Failover Keys
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Level 2 OTA APK Version & Circular Failover Keys",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepPurplePrimary
                    )
                    Text(
                        text = "Installed App Version: v$currentAppVersionCode • Set latestVersionCode to 2 to trigger the automatic 'New Update Available! [Update Now]' popup.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = bannerText,
                        onValueChange = { bannerText = it },
                        label = { Text("Live Home Dashboard Banner Text") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = latestVersionStr,
                            onValueChange = { latestVersionStr = it },
                            label = { Text("latestVersionCode") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(
                            onClick = {
                                latestVersionStr = "2"
                                onSaveOtaAndKeys(
                                    bannerText,
                                    2L,
                                    geminiKey1,
                                    geminiKey2,
                                    fishKey1,
                                    fishKey2,
                                    elevenKey
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("trigger_ota_version_2_button")
                        ) {
                            Text("Simulate OTA v2 Popup")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = geminiKey1,
                        onValueChange = { geminiKey1 = it },
                        label = { Text("Failover Gemini API Key #1 (Optional Backup)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = geminiKey2,
                        onValueChange = { geminiKey2 = it },
                        label = { Text("Failover Gemini API Key #2 (Circular Backup)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fishKey1,
                        onValueChange = { fishKey1 = it },
                        label = { Text("Fish Audio Voice Clone API Key #1") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = elevenKey,
                        onValueChange = { elevenKey = it },
                        label = { Text("ElevenLabs Voice Clone API Key") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            val parsedVer = latestVersionStr.toLongOrNull()?.coerceAtLeast(1L) ?: 1L
                            onSaveOtaAndKeys(
                                bannerText,
                                parsedVer,
                                geminiKey1,
                                geminiKey2,
                                fishKey1,
                                fishKey2,
                                elevenKey
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepPurplePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_admin_config_button")
                    ) {
                        Text("Save Live Admin & Multi-API Config")
                    }
                }
            }
        }
    }
}

@Composable
fun OtaUpdateAvailableDialog(
    latestVersionCode: Long,
    onUpdateNow: () -> Unit,
    onDismiss: () -> Unit
) {
    var isDownloading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.SystemUpdate,
                contentDescription = "New Update Available",
                tint = DeepPurplePrimary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "New Update Available! (v$latestVersionCode)",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = "AI Speaking Partner PRO MAX-এর নতুন ভার্সন (latestVersionCode: $latestVersionCode) ক্লাউড অ্যাডমিন প্যানেল থেকে রিলিজ করা হয়েছে।",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (isDownloading) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Downloading & verifying OTA package...")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isDownloading = true
                    onUpdateNow()
                },
                colors = ButtonDefaults.buttonColors(containerColor = FreshMintPrimary),
                modifier = Modifier.testTag("ota_update_now_button")
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Update Now")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Update Now")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Later")
            }
        }
    )
}

@Composable
private fun LabTopHeader(
    title: String,
    subtitle: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White)
                .testTag("back_to_home_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = DeepPurplePrimary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
