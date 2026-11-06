package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.TutorPersona
import com.example.data.model.VideoLesson
import com.example.data.model.VideoLessonsCatalog
import com.example.data.voice.VisemeShape
import com.example.ui.theme.AmberDarkText
import com.example.ui.theme.AmberLightContainer
import com.example.ui.theme.CallStageCardBg
import com.example.ui.theme.CallStageDarkBg
import com.example.ui.theme.CoralEndCall
import com.example.ui.theme.DeepPurplePrimary
import com.example.ui.theme.FreshMintPrimary
import com.example.ui.theme.LavenderContainer
import com.example.ui.theme.LavenderMistBackground
import com.example.ui.theme.MintDarkText
import com.example.ui.theme.MintLightContainer
import com.example.ui.theme.RoyalPurpleAccent
import com.example.ui.theme.WarmAmberWarning

@Composable
fun VideoLessonsAcademyScreen(
    lessons: List<VideoLesson>,
    selectedLesson: VideoLesson,
    tutors: List<TutorPersona>,
    completedLessonIds: Set<String>,
    selectedCategory: String,
    searchQuery: String,
    playbackRate: Float,
    isTutorSpeaking: Boolean,
    currentViseme: VisemeShape,
    isListening: Boolean,
    onSelectLesson: (VideoLesson) -> Unit,
    onPlayLessonVideo: (VideoLesson, Float) -> Unit,
    onStopLessonVideo: () -> Unit,
    onSpeakPhrase: (String, String, Float) -> Unit,
    onStartShadowMic: (VideoLesson) -> Unit,
    onToggleLessonComplete: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPlaybackRateChange: (Float) -> Unit,
    onStartLiveCallForLesson: (VideoLesson) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var selectedLevelFilter by remember { mutableStateOf("All Levels") }
    var showBanglaSubtitles by remember { mutableStateOf(true) }

    val activeTutor = tutors.find { it.id == selectedLesson.tutorId } ?: tutors.first()

    val filteredLessons = lessons.filter { lesson ->
        val matchesCategory = selectedCategory == "All (108)" ||
            lesson.category.equals(selectedCategory, ignoreCase = true)
        val matchesLevel = selectedLevelFilter == "All Levels" ||
            lesson.level.equals(selectedLevelFilter, ignoreCase = true)
        val matchesQuery = searchQuery.isBlank() ||
            lesson.title.contains(searchQuery, ignoreCase = true) ||
            lesson.banglaTitle.contains(searchQuery, ignoreCase = true) ||
            lesson.category.contains(searchQuery, ignoreCase = true) ||
            lesson.keyPatternOrVocab.contains(searchQuery, ignoreCase = true) ||
            "#${lesson.lessonNumber}".contains(searchQuery.trim(), ignoreCase = true)
        matchesCategory && matchesLevel && matchesQuery
    }

    val completedCount = completedLessonIds.size
    val totalCount = lessons.size.coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LavenderMistBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 660.dp)
        ) {
            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("video_academy_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepPurplePrimary
                        )
                    }
                    Column {
                        Text(
                            text = "100+ Video Lessons Academy",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "১০৮টি পূর্ণাঙ্গ 3D AI টিউটর ভিডিও লেসন, বাংলা গাইড ও শ্যাডো প্র্যাকটিস",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    color = MintLightContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "$completedCount/${lessons.size} Done",
                        style = MaterialTheme.typography.labelMedium,
                        color = MintDarkText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Overall Academy Progress Bar
            LinearProgressIndicator(
                progress = { (completedCount.toFloat() / totalCount.toFloat()).coerceIn(0f, 1f) },
                color = FreshMintPrimary,
                trackColor = LavenderContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ================= 3D INTERACTIVE VIDEO LESSON PLAYER STAGE =================
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("video_lesson_player_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF1F1638), CallStageDarkBg, Color(0xFF130F24))
                            )
                        )
                        .padding(18.dp)
                ) {
                    // Top Video Metadata Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = FreshMintPrimary,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "LESSON #${selectedLesson.lessonNumber}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                color = Color.White.copy(alpha = 0.14f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "${selectedLesson.category} • ${selectedLesson.level}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = LavenderContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "⏱ ${selectedLesson.durationLabel}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title & Bangla Title
                    Text(
                        text = selectedLesson.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = selectedLesson.banglaTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MintLightContainer
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3D Avatar Video Frame + Live Subtitles Overlay
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(124.dp)
                        ) {
                            TutorAvatar3DCanvas(
                                tutor = activeTutor,
                                emotion = if (isTutorSpeaking) "happy" else "encouraging",
                                viseme = currentViseme,
                                isSpeaking = isTutorSpeaking,
                                modifier = Modifier.size(118.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = CallStageCardBg,
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isTutorSpeaking) FreshMintPrimary else Color.White.copy(alpha = 0.16f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${activeTutor.name} (${activeTutor.accentLabel.substringBefore(" ")})",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = FreshMintPrimary
                                        )
                                        Text(
                                            text = if (isTutorSpeaking) "● PLAYING 60FPS" else "READY TO PLAY",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isTutorSpeaking) FreshMintPrimary else LavenderContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "\"${selectedLesson.videoScriptEnglish}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White
                                    )
                                    if (showBanglaSubtitles) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "বাংলা সারসংক্ষেপ: ${selectedLesson.banglaSummary}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MintLightContainer
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Video Controls: Play / Stop / Speed (0.85x, 1.0x, 1.15x) / Subtitles toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    if (isTutorSpeaking) {
                                        onStopLessonVideo()
                                    } else {
                                        onPlayLessonVideo(selectedLesson, playbackRate)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isTutorSpeaking) CoralEndCall else FreshMintPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                modifier = Modifier.testTag("play_video_lesson_button")
                            ) {
                                Icon(
                                    imageVector = if (isTutorSpeaking) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = if (isTutorSpeaking) "Stop Video" else "Play Video Lesson"
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isTutorSpeaking) "Stop Video" else "Play 3D Video",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = { onPlayLessonVideo(selectedLesson, playbackRate) },
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = "Replay",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Replay",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White
                                )
                            }
                        }

                        // Speed Selector Pill (0.85x / 1.0x / 1.15x)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Speed",
                                tint = LavenderContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            listOf(0.85f to "0.85x", 1.0f to "1.0x", 1.15f to "1.15x").forEach { (rate, label) ->
                                val active = kotlin.math.abs(playbackRate - rate) < 0.03f
                                Surface(
                                    color = if (active) DeepPurplePrimary else Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .clickable { onPlaybackRateChange(rate) }
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Key Pattern / Vocabulary Box + Shadow Practice Box
                    Surface(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "KEY PATTERN & VOCABULARY FORMULA",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmAmberWarning,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedLesson.keyPatternOrVocab,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "SHADOW SPEAKING DRILL (টিউটরের সাথে বলুন):",
                                style = MaterialTheme.typography.labelSmall,
                                color = MintLightContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${selectedLesson.shadowPracticeSentence}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onSpeakPhrase(
                                            selectedLesson.shadowPracticeSentence,
                                            activeTutor.languageTag,
                                            playbackRate
                                        )
                                    },
                                    border = BorderStroke(1.dp, MintLightContainer),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Listen Drill",
                                        tint = MintLightContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Listen Drill",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White
                                    )
                                }

                                Button(
                                    onClick = { onStartShadowMic(selectedLesson) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isListening) CoralEndCall else DeepPurplePrimary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("shadow_speak_mic_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Speak Shadow Sentence",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isListening) "Listening..." else "Shadow Speak",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Row: Practice in Live 3D Call + Mark Complete
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onStartLiveCallForLesson(selectedLesson) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FreshMintPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1.35f)
                                .testTag("practice_lesson_in_3d_call_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Practice in 3D Call",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Live 3D Call on Lesson",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val isDone = selectedLesson.id in completedLessonIds
                        OutlinedButton(
                            onClick = { onToggleLessonComplete(selectedLesson.id) },
                            border = BorderStroke(
                                1.dp,
                                if (isDone) FreshMintPrimary else Color.White.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("mark_lesson_complete_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Complete",
                                tint = if (isDone) FreshMintPrimary else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDone) "Completed ✓" else "Mark Done",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ================= SEARCH & FILTER BAR FOR 108 VIDEO LESSONS =================
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text("Search 108 lessons (#1–#108, topic, English or বাংলা)...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Video Lessons",
                        tint = DeepPurplePrimary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = DeepPurplePrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("video_lessons_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Chips (All (108), Everyday English, Job interviews, etc.)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VideoLessonsCatalog.categories.forEach { category ->
                    val selected = selectedCategory == category
                    FilterChip(
                        selected = selected,
                        onClick = { onCategorySelect(category) },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepPurplePrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("video_cat_chip_$category")
                    )
                }
            }

            // Level Filter Chips + Subtitle Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("All Levels", "Beginner", "Intermediate", "Advanced").forEach { level ->
                    val selected = selectedLevelFilter == level
                    FilterChip(
                        selected = selected,
                        onClick = { selectedLevelFilter = level },
                        label = { Text(level) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RoyalPurpleAccent,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                FilterChip(
                    selected = showBanglaSubtitles,
                    onClick = { showBanglaSubtitles = !showBanglaSubtitles },
                    label = { Text(if (showBanglaSubtitles) "বাংলা Subtitles: ON" else "বাংলা Subtitles: OFF") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FreshMintPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${filteredLessons.size} Video Lessons",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap any card to play 3D video ↑",
                    style = MaterialTheme.typography.bodySmall,
                    color = DeepPurplePrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ================= 108 VIDEO LESSONS LIST =================
            filteredLessons.forEach { lesson ->
                val isSelected = lesson.id == selectedLesson.id
                val isCompleted = lesson.id in completedLessonIds
                val lessonTutor = tutors.find { it.id == lesson.tutorId } ?: tutors.first()

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) LavenderContainer else Color.White
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) DeepPurplePrimary else MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            onSelectLesson(lesson)
                            onPlayLessonVideo(lesson, playbackRate)
                        }
                        .testTag("video_lesson_item_${lesson.lessonNumber}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Video Thumbnail Badge with Lesson Number & Play Icon
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(lessonTutor.primaryColorHex),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "#${lesson.lessonNumber}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.OndemandVideo,
                                    contentDescription = "Play Video Lesson",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = when (lesson.level) {
                                        "Beginner" -> MintLightContainer
                                        "Advanced" -> LavenderContainer
                                        else -> AmberLightContainer
                                    },
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = lesson.level,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = when (lesson.level) {
                                            "Beginner" -> MintDarkText
                                            "Advanced" -> DeepPurplePrimary
                                            else -> AmberDarkText
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }

                                Text(
                                    text = "${lesson.category} • ${lesson.durationLabel}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = lesson.banglaTitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = DeepPurplePrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = lesson.keyPatternOrVocab,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Completed",
                                    tint = FreshMintPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) DeepPurplePrimary else LavenderContainer,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        onSelectLesson(lesson)
                                        onPlayLessonVideo(lesson, playbackRate)
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play Lesson",
                                        tint = if (isSelected) Color.White else DeepPurplePrimary,
                                        modifier = Modifier.size(20.dp)
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
