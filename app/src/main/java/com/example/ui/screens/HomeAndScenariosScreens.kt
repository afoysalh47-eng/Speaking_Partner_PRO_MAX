package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FaceRetouchingNatural
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileInputStream
import com.example.data.model.PracticeScenario
import com.example.data.model.TutorPersona
import com.example.data.model.UserProfile
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
import com.example.ui.viewmodel.AppScreen

@Composable
fun HomeDashboardScreen(
    userProfile: UserProfile,
    tutors: List<TutorPersona>,
    selectedTutor: TutorPersona,
    onSelectTutor: (TutorPersona) -> Unit,
    onStartQuickCall: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    var apkExportMessage by remember { mutableStateOf<String?>(null) }

    val saveApkLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.android.package-archive")
    ) { destUri ->
        if (destUri != null) {
            try {
                val sourceApk = File(context.applicationInfo.sourceDir)
                FileInputStream(sourceApk).use { input ->
                    context.contentResolver.openOutputStream(destUri)?.use { output ->
                        input.copyTo(output)
                    }
                }
                apkExportMessage = "✓ Speaking_Partner_PRO_MAX.apk সফলভাবে আপনার ফোল্ডারে সেভ হয়েছে!"
            } catch (e: Exception) {
                apkExportMessage = "APK সেভ করা যায়নি: ${e.localizedMessage}"
            }
        }
    }

    val cefrBadge = when (userProfile.englishLevel) {
        "Intermediate" -> "Intermediate • B1"
        "Advanced" -> "Advanced • C1"
        else -> "Beginner • A1"
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
            // Top User Header & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = DeepPurplePrimary,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onNavigate(AppScreen.ONBOARDING_STEP_1) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Profile",
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = userProfile.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Surface(
                            color = MintLightContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = cefrBadge,
                                style = MaterialTheme.typography.labelMedium,
                                color = MintDarkText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onNavigate(AppScreen.ONBOARDING_STEP_1) },
                        modifier = Modifier.testTag("edit_goals_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Goals & Language Settings",
                            tint = DeepPurplePrimary
                        )
                    }
                    IconButton(
                        onClick = { onNavigate(AppScreen.CLOUD_ADMIN_OTA) },
                        modifier = Modifier.testTag("open_admin_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Cloud Admin & OTA",
                            tint = DeepPurplePrimary
                        )
                    }
                    IconButton(
                        onClick = onSignOut,
                        modifier = Modifier.testTag("sign_out_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sign Out",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Direct 1-Tap APK Export & 108 Video Lessons Quick Access Card (No Searching Needed)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, FreshMintPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("direct_apk_export_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
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
                                text = "DIRECT APK & 108 VIDEOS",
                                style = MaterialTheme.typography.labelSmall,
                                color = MintDarkText,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "v1.0 • Ready",
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepPurplePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Speaking_Partner_PRO_MAX.apk (আলাদা ফাইল)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "খোঁজাখুঁড়ি ছাড়াই নিচের বাটনে ট্যাপ করে সরাসরি APK ফাইলটি সেভ করুন অথবা ১০৮টি ভিডিও লেসন দেখুন:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (apkExportMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = MintLightContainer,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = apkExportMessage.orEmpty(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MintDarkText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { saveApkLauncher.launch("Speaking_Partner_PRO_MAX.apk") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FreshMintPrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("save_apk_direct_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Save APK",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Save APK File",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { onNavigate(AppScreen.VIDEO_LESSONS_ACADEMY) },
                            border = BorderStroke(1.5.dp, DeepPurplePrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_open_108_videos_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.OndemandVideo,
                                contentDescription = "108 Video Lessons",
                                tint = DeepPurplePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "108 Videos (#1–108)",
                                style = MaterialTheme.typography.labelLarge,
                                color = DeepPurplePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Live Cloud OTA Announcement Banner
            if (userProfile.homeBannerText.isNotBlank()) {
                Surface(
                    color = LavenderContainer,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, DeepPurplePrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Live Banner",
                            tint = DeepPurplePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = userProfile.homeBannerText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Hero Card: "Let's talk about your day" + Start speaking video button
            Card(
                shape = RoundedCornerShape(26.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(DeepPurplePrimary, RoyalPurpleAccent, Color(0xFF4C1D95))
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "LIVE 3D FACE-TO-FACE CALL",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                            Text(
                                text = "Tutor: ${selectedTutor.name}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MintLightContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Let's talk about your day",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "মুখোমুখি 3D ভিডিও কলে কথা বলুন, Eye-Contact ক্যামেরা চালু রাখুন এবং বাংলা বা ইংরেজিতে নির্ভয়ে চর্চা করুন।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onStartQuickCall,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FreshMintPrimary,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("hero_start_speaking_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Start Speaking Video Call"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Start speaking (Video Call)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7-Day Streak Bar + 12 min today + 78 Fluency Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricStatCard(
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = WarmAmberWarning,
                    valueText = "${userProfile.streakDays}-day",
                    labelText = "Streak bar",
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    icon = Icons.Default.Timer,
                    iconTint = DeepPurplePrimary,
                    valueText = "${userProfile.minutesToday} min",
                    labelText = "Spoken today",
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    icon = Icons.Default.Bolt,
                    iconTint = FreshMintPrimary,
                    valueText = "${userProfile.fluencyScore}",
                    labelText = "Fluency score",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Today's 5-minute challenge (3/5 min)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val doneMin = userProfile.dailyChallengeMinutes.coerceIn(0L, 5L)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's 5-minute challenge",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Surface(
                            color = MintLightContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "$doneMin/5 min",
                                style = MaterialTheme.typography.labelMedium,
                                color = MintDarkText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { (doneMin.toFloat() / 5f).coerceIn(0f, 1f) },
                        color = FreshMintPrimary,
                        trackColor = LavenderContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // 7-Day visual dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri").forEachIndexed { idx, day ->
                            val active = idx < userProfile.streakDays.coerceAtMost(7L)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (active) FreshMintPrimary else LavenderContainer,
                                    modifier = Modifier.size(22.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (active) "✓" else "•",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (active) Color.White else DeepPurplePrimary
                                        )
                                    }
                                }
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // AI Tutor & Familiar Person Selector Carousel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Choose Your AI Speaking Partner",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "+ Clone Photo/Voice",
                    style = MaterialTheme.typography.labelLarge,
                    color = DeepPurplePrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigate(AppScreen.FAMILIAR_CLONE_STUDIO) }
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                tutors.forEach { tutor ->
                    val isSelected = tutor.id == selectedTutor.id
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
                            .width(175.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onSelectTutor(tutor) }
                            .testTag("tutor_card_${tutor.id}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(tutor.primaryColorHex),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = tutor.name.take(1),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = tutor.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = tutor.accentLabel,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DeepPurplePrimary,
                                        maxLines = 1
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = tutor.roleTitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pro Coaching Labs & All Modules Hub
            Text(
                text = "Pro Max Modules & Coaching Labs",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(10.dp))

            ProModuleCard(
                title = "100+ Video Lessons Academy (108 Videos)",
                subtitle = "১০৮টি 3D AI টিউটর ভিডিও লেসন, বাংলা সাবটাইটেল, ফর্মুলা ও শ্যাডো প্র্যাকটিস",
                badge = "108 Videos • NEW",
                icon = Icons.Default.OndemandVideo,
                accentColor = FreshMintPrimary,
                onClick = { onNavigate(AppScreen.VIDEO_LESSONS_ACADEMY) },
                testTag = "open_video_academy_card"
            )
            ProModuleCard(
                title = "Practice Your World (Scenarios Hub)",
                subtitle = "৬টি রিয়েল-লাইফ কার্ড + Duolingo Max মিশন ও ক্লাউড টপিক",
                badge = "Module 04",
                icon = Icons.Default.RecordVoiceOver,
                accentColor = DeepPurplePrimary,
                onClick = { onNavigate(AppScreen.SCENARIOS_HUB) },
                testTag = "open_scenarios_hub_card"
            )
            ProModuleCard(
                title = "Pronunciation Coach Lab (Phoneme Engine)",
                subtitle = "ELSA স্টাইল Word-by-Word সবুজ/কমলা কালারিং, IPA ও বাংলা টিপস",
                badge = "Module 09",
                icon = Icons.Default.GraphicEq,
                accentColor = FreshMintPrimary,
                onClick = { onNavigate(AppScreen.PRONUNCIATION_COACH) },
                testTag = "open_pronunciation_lab_card"
            )
            ProModuleCard(
                title = "IELTS Speaking Mock Examiner Suite",
                subtitle = "অফিশিয়াল Part 1, Part 2 (Cue Card), Part 3 + Live Band Score (4.5–8.5)",
                badge = "Module 10",
                icon = Icons.Default.School,
                accentColor = Color(0xFF1E40AF),
                onClick = { onNavigate(AppScreen.IELTS_MOCK_SUITE) },
                testTag = "open_ielts_mock_card"
            )
            ProModuleCard(
                title = "3-Tone Vocabulary Bank & Recall Quiz",
                subtitle = "Casual, Business Formal ও Native Idiomatic স্টাইল + ফ্ল্যাশকার্ড কুইজ",
                badge = "Module 11",
                icon = Icons.Default.MenuBook,
                accentColor = RoyalPurpleAccent,
                onClick = { onNavigate(AppScreen.VOCAB_BANK_QUIZ) },
                testTag = "open_vocab_bank_card"
            )
            ProModuleCard(
                title = "Custom Photo Lip-Sync & Free Voice Cloning",
                subtitle = "পরিচিত মানুষের ছবি আপলোড করে ঠোঁট মেলানো এবং Hugging Face/Fish/ElevenLabs ক্লোন",
                badge = "Modules 07–08",
                icon = Icons.Default.FaceRetouchingNatural,
                accentColor = WarmAmberWarning,
                onClick = { onNavigate(AppScreen.FAMILIAR_CLONE_STUDIO) },
                testTag = "open_familiar_clone_card"
            )
            ProModuleCard(
                title = "Speak Pattern Drills & Praktika STAR CV Lab",
                subtitle = "নির্দিষ্ট বাক্য কাঠামো ড্রিল এবং সিভি/জব ডেসক্রিপশন পেস্ট করে STAR ইন্টারভিউ",
                badge = "Pro Labs",
                icon = Icons.Default.Psychology,
                accentColor = Color(0xFF047857),
                onClick = { onNavigate(AppScreen.PATTERN_AND_CV_LAB) },
                testTag = "open_pattern_cv_lab_card"
            )
            ProModuleCard(
                title = "Cloud Web Admin & In-App Auto-Updater (OTA)",
                subtitle = "নতুন টপিক, ব্যানার, Multi-API Key Failover এবং ওয়ান-ট্যাপ APK Update পপ-আপ",
                badge = "Module 12",
                icon = Icons.Default.AdminPanelSettings,
                accentColor = DeepPurplePrimary,
                onClick = { onNavigate(AppScreen.CLOUD_ADMIN_OTA) },
                testTag = "open_cloud_admin_card"
            )
        }
    }
}

@Composable
private fun MetricStatCard(
    icon: ImageVector,
    iconTint: Color,
    valueText: String,
    labelText: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = labelText,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = valueText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = labelText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProModuleCard(
    title: String,
    subtitle: String,
    badge: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = accentColor.copy(alpha = 0.14f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = LavenderContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = DeepPurplePrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open",
                tint = DeepPurplePrimary
            )
        }
    }
}

@Composable
fun PracticeScenariosHubScreen(
    userProfile: UserProfile,
    scenarios: List<PracticeScenario>,
    searchQuery: String,
    selectedFilterChip: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterChipSelect: (String) -> Unit,
    onScenarioClick: (PracticeScenario) -> Unit,
    onOpenPronunciationCoach: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val filteredScenarios = scenarios.filter { scenario ->
        val matchesQuery = searchQuery.isBlank() ||
            scenario.title.contains(searchQuery, ignoreCase = true) ||
            scenario.category.contains(searchQuery, ignoreCase = true) ||
            scenario.banglaSummary.contains(searchQuery, ignoreCase = true)

        val matchesChip = when (selectedFilterChip) {
            "For you" -> scenario.category in userProfile.selectedGoals || scenario.isCloudCustom
            "Beginner" -> scenario.badgeLevel.equals("Easy", ignoreCase = true)
            else -> true
        }
        matchesQuery && matchesChip
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
            Text(
                text = "Practice Your World",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "যেকোনো রিয়েল-লাইফ সিনারিওতে ট্যাপ করলেই সরাসরি টিউটরের সাথে ইনকামিং ভিডিও কল শুরু হবে।",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar: "Find a topic or situation"
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Find a topic or situation...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search topics",
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
                    .testTag("scenarios_search_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Filter Chips: For you, Beginner, All topics
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("For you", "Beginner", "All topics").forEach { chip ->
                    val selected = selectedFilterChip == chip
                    FilterChip(
                        selected = selected,
                        onClick = { onFilterChipSelect(chip) },
                        label = { Text(chip) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepPurplePrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_chip_$chip")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            filteredScenarios.forEach { scenario ->
                val isEasy = scenario.badgeLevel.equals("Easy", ignoreCase = true)
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onScenarioClick(scenario) }
                        .testTag("scenario_card_${scenario.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    color = if (isEasy) MintLightContainer else AmberLightContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = if (isEasy) "Easy" else "Intermediate",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isEasy) MintDarkText else AmberDarkText,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Surface(
                                    color = LavenderContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = scenario.category,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DeepPurplePrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Start Scenario Call",
                                tint = DeepPurplePrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = scenario.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = scenario.banglaSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Pronunciation Coach Banner
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = DeepPurplePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .clickable { onOpenPronunciationCoach() }
                    .testTag("pronunciation_coach_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = FreshMintPrimary,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "ELSA PHONEME ENGINE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pronunciation coach",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = "প্রতিটি শব্দের সবুজ/কমলা উচ্চারণ স্কোর, IPA এবং বাংলা ফোনেটিক টিপস দেখুন →",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Open Pronunciation Coach",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
