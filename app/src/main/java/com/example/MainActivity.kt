package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.firebase.authStateFlow
import com.example.data.firebase.configureAppCheck
import com.example.data.firebase.signOutUser
import com.example.data.model.PreBuiltCatalog
import com.example.ui.screens.CloudAdminAndOtaScreen
import com.example.ui.screens.FamiliarCloneAndVoiceScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.IeltsMockExaminerScreen
import com.example.ui.screens.IncomingVideoCallScreen
import com.example.ui.screens.Live3DVideoCallScreen
import com.example.ui.screens.OnboardingStep1GoalsScreen
import com.example.ui.screens.OnboardingStep2LevelScreen
import com.example.ui.screens.OtaUpdateAvailableDialog
import com.example.ui.screens.PatternDrillsAndStarCvScreen
import com.example.ui.screens.PracticeScenariosHubScreen
import com.example.ui.screens.PronunciationCoachLabScreen
import com.example.ui.screens.SignInScreen
import com.example.ui.screens.VideoLessonsAcademyScreen
import com.example.ui.screens.VocabBankAndQuizScreen
import com.example.ui.theme.DeepPurplePrimary
import com.example.ui.theme.LavenderContainer
import com.example.ui.theme.SpeakingPartnerTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SpeakingPartnerViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure App Check with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            SpeakingPartnerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(auth: FirebaseAuth = Firebase.auth) {
    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)
    val user = currentUser

    if (user == null) {
        SignInScreen(
            onAuthSuccess = {}
        )
    } else {
        AuthenticatedSpeakingPartnerApp(
            currentUserId = user.uid,
            displayName = user.displayName ?: user.email?.substringBefore("@") ?: "Learner"
        )
    }
}

@Composable
fun AuthenticatedSpeakingPartnerApp(
    currentUserId: String,
    displayName: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val viewModel: SpeakingPartnerViewModel = viewModel(
        key = currentUserId,
        factory = SpeakingPartnerViewModel.provideFactory(
            context = context,
            currentUserId = currentUserId,
            displayName = displayName
        )
    )

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val vocabBank by viewModel.vocabBank.collectAsStateWithLifecycle()
    val familiarPeople by viewModel.familiarPeople.collectAsStateWithLifecycle()
    val customTopics by viewModel.customTopics.collectAsStateWithLifecycle()
    val allScenarios by viewModel.allScenarios.collectAsStateWithLifecycle()
    val allTutors by viewModel.allTutors.collectAsStateWithLifecycle()

    val scenarioSearchQuery by viewModel.scenarioSearchQuery.collectAsStateWithLifecycle()
    val scenarioFilterChip by viewModel.scenarioFilterChip.collectAsStateWithLifecycle()
    val selectedScenario by viewModel.selectedScenario.collectAsStateWithLifecycle()
    val selectedTutor by viewModel.selectedTutor.collectAsStateWithLifecycle()
    val callHistory by viewModel.callHistory.collectAsStateWithLifecycle()
    val latestCoaching by viewModel.latestCoaching.collectAsStateWithLifecycle()
    val isBanglaMicActive by viewModel.isBanglaMicActive.collectAsStateWithLifecycle()
    val isEyeContactPreviewOn by viewModel.isEyeContactPreviewOn.collectAsStateWithLifecycle()
    val isListening by viewModel.isListening.collectAsStateWithLifecycle()
    val partialTranscript by viewModel.partialTranscript.collectAsStateWithLifecycle()
    val isTutorSpeaking by viewModel.isTutorSpeaking.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val currentViseme by viewModel.currentViseme.collectAsStateWithLifecycle()
    val callDurationSeconds by viewModel.callDurationSeconds.collectAsStateWithLifecycle()
    val sessionWordCount by viewModel.sessionWordCount.collectAsStateWithLifecycle()
    val sessionFillerCount by viewModel.sessionFillerCount.collectAsStateWithLifecycle()
    val activeKeySlotLabel by viewModel.activeKeySlotLabel.collectAsStateWithLifecycle()
    val activeVoiceCascadeLabel by viewModel.activeVoiceCascadeLabel.collectAsStateWithLifecycle()

    val selectedPhonemeChallenge by viewModel.selectedPhonemeChallenge.collectAsStateWithLifecycle()
    val wordPronunciationScores by viewModel.wordPronunciationScores.collectAsStateWithLifecycle()

    val selectedIeltsCard by viewModel.selectedIeltsCard.collectAsStateWithLifecycle()
    val currentIeltsPart by viewModel.currentIeltsPart.collectAsStateWithLifecycle()
    val ieltsTranscripts by viewModel.ieltsTranscripts.collectAsStateWithLifecycle()
    val ieltsEvaluation by viewModel.ieltsEvaluation.collectAsStateWithLifecycle()
    val isEvaluatingIelts by viewModel.isEvaluatingIelts.collectAsStateWithLifecycle()

    val selectedPatternDrill by viewModel.selectedPatternDrill.collectAsStateWithLifecycle()
    val starCvCoachingOutput by viewModel.starCvCoachingOutput.collectAsStateWithLifecycle()
    val isGeneratingStarCv by viewModel.isGeneratingStarCv.collectAsStateWithLifecycle()

    val selectedVideoLesson by viewModel.selectedVideoLesson.collectAsStateWithLifecycle()
    val videoLessonCategoryFilter by viewModel.videoLessonCategoryFilter.collectAsStateWithLifecycle()
    val videoLessonSearchQuery by viewModel.videoLessonSearchQuery.collectAsStateWithLifecycle()
    val completedVideoLessonIds by viewModel.completedVideoLessonIds.collectAsStateWithLifecycle()
    val videoPlaybackRate by viewModel.videoPlaybackRate.collectAsStateWithLifecycle()

    val statusBannerMessage by viewModel.statusBannerMessage.collectAsStateWithLifecycle()
    val dismissedOtaVersion by viewModel.dismissedOtaVersion.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusBannerMessage) {
        statusBannerMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    // Automatic In-App OTA APK Update Popup when latestVersionCode > currentAppVersionCode (Module 12)
    if (userProfile.latestVersionCode > viewModel.currentAppVersionCode &&
        dismissedOtaVersion < userProfile.latestVersionCode
    ) {
        OtaUpdateAvailableDialog(
            latestVersionCode = userProfile.latestVersionCode,
            onUpdateNow = {
                viewModel.dismissOtaPopup(userProfile.latestVersionCode)
                viewModel.postStatusMessage("OTA v${userProfile.latestVersionCode} package applied! All modules up to date.")
            },
            onDismiss = {
                viewModel.dismissOtaPopup(userProfile.latestVersionCode)
            }
        )
    }

    val showBottomBar = currentScreen !in setOf(
        AppScreen.ONBOARDING_STEP_1,
        AppScreen.ONBOARDING_STEP_2,
        AppScreen.INCOMING_CALL,
        AppScreen.LIVE_3D_CALL
    )

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.HOME_DASHBOARD,
                        onClick = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepPurplePrimary,
                            selectedTextColor = DeepPurplePrimary,
                            indicatorColor = LavenderContainer
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.VIDEO_LESSONS_ACADEMY,
                        onClick = { viewModel.navigateTo(AppScreen.VIDEO_LESSONS_ACADEMY) },
                        icon = { Icon(Icons.Default.OndemandVideo, contentDescription = "108 Videos") },
                        label = { Text("108 Videos") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepPurplePrimary,
                            selectedTextColor = DeepPurplePrimary,
                            indicatorColor = LavenderContainer
                        ),
                        modifier = Modifier.testTag("nav_video_lessons")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.SCENARIOS_HUB,
                        onClick = { viewModel.navigateTo(AppScreen.SCENARIOS_HUB) },
                        icon = { Icon(Icons.Default.RecordVoiceOver, contentDescription = "Scenarios") },
                        label = { Text("Scenarios") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepPurplePrimary,
                            selectedTextColor = DeepPurplePrimary,
                            indicatorColor = LavenderContainer
                        ),
                        modifier = Modifier.testTag("nav_scenarios")
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.startIncomingCall() },
                        icon = { Icon(Icons.Default.Videocam, contentDescription = "3D Call") },
                        label = { Text("3D Call") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepPurplePrimary,
                            selectedTextColor = DeepPurplePrimary,
                            indicatorColor = LavenderContainer
                        ),
                        modifier = Modifier.testTag("nav_3d_call")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PRONUNCIATION_COACH,
                        onClick = { viewModel.navigateTo(AppScreen.PRONUNCIATION_COACH) },
                        icon = { Icon(Icons.Default.GraphicEq, contentDescription = "Phonemes") },
                        label = { Text("Phonemes") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepPurplePrimary,
                            selectedTextColor = DeepPurplePrimary,
                            indicatorColor = LavenderContainer
                        ),
                        modifier = Modifier.testTag("nav_phonemes")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.VOCAB_BANK_QUIZ,
                        onClick = { viewModel.navigateTo(AppScreen.VOCAB_BANK_QUIZ) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = "3-Tones") },
                        label = { Text("3-Tones") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepPurplePrimary,
                            selectedTextColor = DeepPurplePrimary,
                            indicatorColor = LavenderContainer
                        ),
                        modifier = Modifier.testTag("nav_vocab")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.ONBOARDING_STEP_1 -> {
                    OnboardingStep1GoalsScreen(
                        userProfile = userProfile,
                        onToggleGoal = viewModel::toggleGoalSelection,
                        onSelectHelpLanguage = viewModel::updateHelpLanguage,
                        onContinueClicked = { viewModel.navigateTo(AppScreen.ONBOARDING_STEP_2) },
                        onSkipToHome = viewModel::completeOnboardingAndOpenHome
                    )
                }

                AppScreen.ONBOARDING_STEP_2 -> {
                    OnboardingStep2LevelScreen(
                        userProfile = userProfile,
                        onSelectLevel = viewModel::updateEnglishLevel,
                        onStartLevelCheckCall = viewModel::startTwoMinuteLevelCheckCall,
                        onBuildMyPlanClicked = viewModel::completeOnboardingAndOpenHome,
                        onBack = { viewModel.navigateTo(AppScreen.ONBOARDING_STEP_1) }
                    )
                }

                AppScreen.HOME_DASHBOARD -> {
                    HomeDashboardScreen(
                        userProfile = userProfile,
                        tutors = allTutors,
                        selectedTutor = selectedTutor,
                        onSelectTutor = viewModel::selectTutor,
                        onStartQuickCall = { viewModel.startIncomingCall() },
                        onNavigate = viewModel::navigateTo,
                        onSignOut = {
                            signOutUser(
                                credentialManager = credentialManager,
                                onSignOutComplete = {},
                                scope = scope
                            )
                        }
                    )
                }

                AppScreen.VIDEO_LESSONS_ACADEMY -> {
                    VideoLessonsAcademyScreen(
                        lessons = viewModel.allVideoLessons,
                        selectedLesson = selectedVideoLesson,
                        tutors = allTutors,
                        completedLessonIds = completedVideoLessonIds,
                        selectedCategory = videoLessonCategoryFilter,
                        searchQuery = videoLessonSearchQuery,
                        playbackRate = videoPlaybackRate,
                        isTutorSpeaking = isTutorSpeaking,
                        currentViseme = currentViseme,
                        isListening = isListening,
                        onSelectLesson = viewModel::selectVideoLesson,
                        onPlayLessonVideo = viewModel::playVideoLessonAudio,
                        onStopLessonVideo = viewModel::stopVideoLessonAudio,
                        onSpeakPhrase = { text, langTag, rate ->
                            viewModel.speakTextSample(text, langTag, rate)
                        },
                        onStartShadowMic = viewModel::startShadowMicForVideoLesson,
                        onToggleLessonComplete = viewModel::toggleVideoLessonCompleted,
                        onCategorySelect = viewModel::updateVideoCategoryFilter,
                        onSearchQueryChange = viewModel::updateVideoSearchQuery,
                        onPlaybackRateChange = viewModel::setVideoPlaybackRate,
                        onStartLiveCallForLesson = viewModel::startCallFromVideoLesson,
                        onBack = {
                            viewModel.stopVideoLessonAudio()
                            viewModel.navigateTo(AppScreen.HOME_DASHBOARD)
                        }
                    )
                }

                AppScreen.SCENARIOS_HUB -> {
                    PracticeScenariosHubScreen(
                        userProfile = userProfile,
                        scenarios = allScenarios,
                        searchQuery = scenarioSearchQuery,
                        selectedFilterChip = scenarioFilterChip,
                        onSearchQueryChange = viewModel::updateScenarioSearch,
                        onFilterChipSelect = viewModel::updateScenarioFilterChip,
                        onScenarioClick = { scenario -> viewModel.startIncomingCall(scenario) },
                        onOpenPronunciationCoach = { viewModel.navigateTo(AppScreen.PRONUNCIATION_COACH) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }

                AppScreen.INCOMING_CALL -> {
                    IncomingVideoCallScreen(
                        scenario = selectedScenario,
                        tutor = selectedTutor,
                        allTutors = allTutors,
                        onSelectTutor = viewModel::selectTutor,
                        onAnswerCall = viewModel::answerVideoCall,
                        onDeclineCall = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }

                AppScreen.LIVE_3D_CALL -> {
                    Live3DVideoCallScreen(
                        scenario = selectedScenario,
                        tutor = selectedTutor,
                        callHistory = callHistory,
                        latestCoaching = latestCoaching,
                        isBanglaMicActive = isBanglaMicActive,
                        isEyeContactPreviewOn = isEyeContactPreviewOn,
                        isListening = isListening,
                        partialTranscript = partialTranscript,
                        isTutorSpeaking = isTutorSpeaking,
                        isAiThinking = isAiThinking,
                        currentViseme = currentViseme,
                        callDurationSeconds = callDurationSeconds,
                        sessionWordCount = sessionWordCount,
                        sessionFillerCount = sessionFillerCount,
                        activeKeySlotLabel = activeKeySlotLabel,
                        activeVoiceCascadeLabel = activeVoiceCascadeLabel,
                        onToggleBanglaMic = viewModel::toggleBanglaMicMode,
                        onToggleEyeContact = viewModel::toggleEyeContactPreview,
                        onStartVoiceCapture = viewModel::startVoiceCaptureInCall,
                        onStopVoiceCapture = viewModel::stopVoiceCaptureInCall,
                        onSubmitSpeechText = viewModel::submitUserSpeechTurn,
                        onSpeakPhrase = { text -> viewModel.speakTextSample(text) },
                        onEndCall = viewModel::endVideoCall
                    )
                }

                AppScreen.FAMILIAR_CLONE_STUDIO -> {
                    FamiliarCloneAndVoiceScreen(
                        familiarPeople = familiarPeople,
                        currentViseme = currentViseme,
                        isTutorSpeaking = isTutorSpeaking,
                        onSaveFamiliarPerson = viewModel::saveFamiliarPersonProfile,
                        onDeleteFamiliarPerson = viewModel::deleteFamiliarPersonProfile,
                        onTestVoiceAndLips = { text, lang -> viewModel.speakTextSample(text, lang) },
                        onStartCallWithPerson = { person ->
                            val matchingTutor = allTutors.find { it.id == "familiar_${person.id}" }
                            viewModel.startIncomingCall(tutor = matchingTutor)
                        },
                        onBack = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }

                AppScreen.PRONUNCIATION_COACH -> {
                    PronunciationCoachLabScreen(
                        challenges = PreBuiltCatalog.phonemeChallenges,
                        selectedChallenge = selectedPhonemeChallenge,
                        wordScores = wordPronunciationScores,
                        isListening = isListening,
                        onSelectChallenge = viewModel::selectPhonemeChallenge,
                        onSpeakTargetSentence = { text -> viewModel.speakTextSample(text) },
                        onStartPronunciationCapture = viewModel::startPronunciationVoiceCapture,
                        onEvaluateManualAttempt = viewModel::evaluatePronunciationAttempt,
                        onBack = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }

                AppScreen.IELTS_MOCK_SUITE -> {
                    IeltsMockExaminerScreen(
                        cueCards = PreBuiltCatalog.ieltsCueCards,
                        selectedCard = selectedIeltsCard,
                        currentPart = currentIeltsPart,
                        transcripts = ieltsTranscripts,
                        evaluation = ieltsEvaluation,
                        isEvaluating = isEvaluatingIelts,
                        isListening = isListening,
                        onSelectCard = viewModel::selectIeltsCueCard,
                        onSelectPart = viewModel::setIeltsPart,
                        onCaptureVoiceResponse = viewModel::captureIeltsVoiceResponse,
                        onSubmitTextResponse = viewModel::addIeltsSpokenResponse,
                        onSpeakPrompt = { text -> viewModel.speakTextSample(text, "en-GB") },
                        onBack = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }

                AppScreen.VOCAB_BANK_QUIZ -> {
                    VocabBankAndQuizScreen(
                        vocabList = vocabBank,
                        onSpeakSentence = { text -> viewModel.speakTextSample(text) },
                        onAddCustomSentence = { orig, cas, form, idio, bn, voc ->
                            // Handled via repository
                        },
                        onQuizAnswer = viewModel::recordVocabQuizAnswer,
                        onBack = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }

                AppScreen.PATTERN_AND_CV_LAB -> {
                    PatternDrillsAndStarCvScreen(
                        drills = PreBuiltCatalog.patternDrills,
                        selectedDrill = selectedPatternDrill,
                        starCoachingOutput = starCvCoachingOutput,
                        isGeneratingStar = isGeneratingStarCv,
                        onSelectDrill = viewModel::selectPatternDrill,
                        onSpeakSample = { text -> viewModel.speakTextSample(text) },
                        onGenerateStarCoaching = viewModel::generateStarMethodAnswer,
                        onBack = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }

                AppScreen.CLOUD_ADMIN_OTA -> {
                    CloudAdminAndOtaScreen(
                        userProfile = userProfile,
                        customTopics = customTopics,
                        currentAppVersionCode = viewModel.currentAppVersionCode,
                        onPublishTopic = viewModel::addCloudTopicFromAdmin,
                        onDeleteTopic = viewModel::deleteCloudTopicFromAdmin,
                        onSaveOtaAndKeys = viewModel::updateAdminOtaAndApiKeys,
                        onBack = { viewModel.navigateTo(AppScreen.HOME_DASHBOARD) }
                    )
                }
            }
        }
    }
}
