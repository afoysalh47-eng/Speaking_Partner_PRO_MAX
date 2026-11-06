package com.example.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.R
import com.example.data.ai.SpeakingAiEngine
import com.example.data.model.ChatTurnMessage
import com.example.data.model.CustomTopic
import com.example.data.model.FamiliarPerson
import com.example.data.model.IeltsBandEvaluation
import com.example.data.model.IeltsCueCard
import com.example.data.model.PatternDrill
import com.example.data.model.PhonemeChallenge
import com.example.data.model.PracticeScenario
import com.example.data.model.PreBuiltCatalog
import com.example.data.model.StructuredAiTurnResponse
import com.example.data.model.TutorPersona
import com.example.data.model.UserProfile
import com.example.data.model.VideoLesson
import com.example.data.model.VideoLessonsCatalog
import com.example.data.model.VocabEntry
import com.example.data.model.WordPronunciationScore
import com.example.data.repository.SpeakingPartnerRepository
import com.example.data.voice.VisemeShape
import com.example.data.voice.VoiceAndLipSyncController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

enum class AppScreen {
    ONBOARDING_STEP_1,
    ONBOARDING_STEP_2,
    HOME_DASHBOARD,
    VIDEO_LESSONS_ACADEMY,
    SCENARIOS_HUB,
    INCOMING_CALL,
    LIVE_3D_CALL,
    FAMILIAR_CLONE_STUDIO,
    PRONUNCIATION_COACH,
    IELTS_MOCK_SUITE,
    VOCAB_BANK_QUIZ,
    PATTERN_AND_CV_LAB,
    CLOUD_ADMIN_OTA
}

class SpeakingPartnerViewModel(
    private val repository: SpeakingPartnerRepository,
    private val aiEngine: SpeakingAiEngine,
    private val voiceController: VoiceAndLipSyncController,
    private val currentUserId: String,
    private val initialDisplayName: String
) : ViewModel() {

    val currentAppVersionCode: Long = 1L

    private val _currentScreen = MutableStateFlow(AppScreen.HOME_DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _hasInitializedOnboarding = MutableStateFlow(false)

    val userProfile: StateFlow<UserProfile> = repository.observeUserProfile(currentUserId)
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            Log.w(TAG, "Profile stream error, using default profile", firestoreError ?: error)
            emit(
                UserProfile(
                    userId = currentUserId,
                    displayName = initialDisplayName.ifBlank { "Learner" }
                )
            )
        }
        .combine(_hasInitializedOnboarding) { profile, initialized ->
            val resolved = profile ?: UserProfile(
                userId = currentUserId,
                displayName = initialDisplayName.ifBlank { "Learner" },
                onboardingCompleted = false
            )
            if (!initialized) {
                _hasInitializedOnboarding.value = true
                if (profile == null) {
                    _currentScreen.value = AppScreen.ONBOARDING_STEP_1
                } else if (!profile.onboardingCompleted) {
                    _currentScreen.value = AppScreen.ONBOARDING_STEP_1
                }
            }
            resolved
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserProfile(
                userId = currentUserId,
                displayName = initialDisplayName.ifBlank { "Learner" }
            )
        )

    val vocabBank: StateFlow<List<VocabEntry>> = repository.observeVocabBank(currentUserId)
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            Log.w(TAG, "Vocab stream error", firestoreError ?: error)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val familiarPeople: StateFlow<List<FamiliarPerson>> = repository.observeFamiliarPeople(currentUserId)
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            Log.w(TAG, "Familiar people stream error", firestoreError ?: error)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val customTopics: StateFlow<List<CustomTopic>> = repository.observeCustomTopics(currentUserId)
        .catch { error ->
            val firestoreError = generateSequence(error) { it.cause }
                .filterIsInstance<FirebaseFirestoreException>()
                .firstOrNull()
            if (firestoreError == null && error is CancellationException) throw error
            Log.w(TAG, "Custom topics stream error", firestoreError ?: error)
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    // Combined Scenarios (Built-in + Cloud Admin Custom Topics)
    val allScenarios: StateFlow<List<PracticeScenario>> = customTopics
        .combine(MutableStateFlow(PreBuiltCatalog.scenarios)) { cloudList, builtInList ->
            val mappedCloud = cloudList.map { topic ->
                PracticeScenario(
                    id = "cloud_${topic.id}",
                    title = topic.title,
                    badgeLevel = topic.badgeLevel,
                    category = topic.category,
                    banglaSummary = "ক্লাউড অ্যাডমিন প্যানেল থেকে যুক্ত লাইভ প্র্যাকটিস টপিক।",
                    roleplayPrompt = topic.roleplayPrompt,
                    defaultTutorId = "sarah_us",
                    starterLine = "Hello! Let's practice our topic '${topic.title}'. How would you like to begin?",
                    starterHint = "Hi! I'd love to practice '${topic.title}' with you today.",
                    isCloudCustom = true
                )
            }
            mappedCloud + builtInList
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = PreBuiltCatalog.scenarios
        )

    // Combined Tutors (Built-in 4 Tutors + Custom Familiar Person Profiles)
    val allTutors: StateFlow<List<TutorPersona>> = familiarPeople
        .combine(MutableStateFlow(PreBuiltCatalog.tutors)) { customPeople, builtInTutors ->
            val mappedCustom = customPeople.map { person ->
                TutorPersona(
                    id = "familiar_${person.id}",
                    name = person.name,
                    roleTitle = "${person.relationship} (Familiar Clone)",
                    accentLabel = "${person.accent} Accent • ${person.voiceEngine}",
                    languageTag = when (person.accent) {
                        "UK" -> "en-GB"
                        "AU" -> "en-AU"
                        else -> "en-US"
                    },
                    banglaBio = "আপনার আপলোড করা পরিচিত মানুষের ছবি ও ক্লোন করা কণ্ঠস্বর প্রোফাইল।",
                    primaryColorHex = 0xFF7C3AED,
                    secondaryColorHex = 0xFFEDE9FE,
                    isCustomFamiliar = true,
                    customPhotoUri = person.photoUri,
                    mouthYRatio = (person.mouthYRatio.toFloat() / 100f).coerceIn(0.25f, 0.85f),
                    mouthWidthRatio = (person.mouthWidthRatio.toFloat() / 100f).coerceIn(0.15f, 0.75f),
                    voiceEngine = person.voiceEngine
                )
            }
            builtInTutors + mappedCustom
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = PreBuiltCatalog.tutors
        )

    // Scenario Search & Filter State
    private val _scenarioSearchQuery = MutableStateFlow("")
    val scenarioSearchQuery: StateFlow<String> = _scenarioSearchQuery.asStateFlow()

    private val _scenarioFilterChip = MutableStateFlow("All topics") // "For you", "Beginner", "All topics"
    val scenarioFilterChip: StateFlow<String> = _scenarioFilterChip.asStateFlow()

    // Active Call & Roleplay State
    private val _selectedScenario = MutableStateFlow(PreBuiltCatalog.scenarios.first())
    val selectedScenario: StateFlow<PracticeScenario> = _selectedScenario.asStateFlow()

    private val _selectedTutor = MutableStateFlow(PreBuiltCatalog.tutors.first())
    val selectedTutor: StateFlow<TutorPersona> = _selectedTutor.asStateFlow()

    private val _callHistory = MutableStateFlow<List<ChatTurnMessage>>(emptyList())
    val callHistory: StateFlow<List<ChatTurnMessage>> = _callHistory.asStateFlow()

    private val _latestCoaching = MutableStateFlow<StructuredAiTurnResponse?>(null)
    val latestCoaching: StateFlow<StructuredAiTurnResponse?> = _latestCoaching.asStateFlow()

    private val _isBanglaMicActive = MutableStateFlow(false)
    val isBanglaMicActive: StateFlow<Boolean> = _isBanglaMicActive.asStateFlow()

    private val _isEyeContactPreviewOn = MutableStateFlow(true)
    val isEyeContactPreviewOn: StateFlow<Boolean> = _isEyeContactPreviewOn.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _callDurationSeconds = MutableStateFlow(0L)
    val callDurationSeconds: StateFlow<Long> = _callDurationSeconds.asStateFlow()

    private val _sessionFillerCount = MutableStateFlow(0)
    val sessionFillerCount: StateFlow<Int> = _sessionFillerCount.asStateFlow()

    private val _sessionWordCount = MutableStateFlow(0)
    val sessionWordCount: StateFlow<Int> = _sessionWordCount.asStateFlow()

    private val _activeKeySlotLabel = MutableStateFlow("Primary: Firebase AI Logic (gemini-3.8-flash)")
    val activeKeySlotLabel: StateFlow<String> = _activeKeySlotLabel.asStateFlow()

    private var callTimerJob: Job? = null

    // Voice & Viseme states exposed from controller
    val isListening: StateFlow<Boolean> = voiceController.isListening
    val partialTranscript: StateFlow<String> = voiceController.partialTranscript
    val isTutorSpeaking: StateFlow<Boolean> = voiceController.isSpeaking
    val currentViseme: StateFlow<VisemeShape> = voiceController.currentViseme
    val activeVoiceCascadeLabel: StateFlow<String> = voiceController.activeVoiceCascadeLabel

    // Pronunciation Coach Lab State (Module 09)
    private val _selectedPhonemeChallenge = MutableStateFlow(PreBuiltCatalog.phonemeChallenges.first())
    val selectedPhonemeChallenge: StateFlow<PhonemeChallenge> = _selectedPhonemeChallenge.asStateFlow()

    private val _wordPronunciationScores = MutableStateFlow<List<WordPronunciationScore>>(emptyList())
    val wordPronunciationScores: StateFlow<List<WordPronunciationScore>> = _wordPronunciationScores.asStateFlow()

    // IELTS Mock Suite State (Module 10)
    private val _selectedIeltsCard = MutableStateFlow(PreBuiltCatalog.ieltsCueCards.first())
    val selectedIeltsCard: StateFlow<IeltsCueCard> = _selectedIeltsCard.asStateFlow()

    private val _currentIeltsPart = MutableStateFlow(1) // 1, 2, or 3
    val currentIeltsPart: StateFlow<Int> = _currentIeltsPart.asStateFlow()

    private val _ieltsTranscripts = MutableStateFlow<List<String>>(emptyList())
    val ieltsTranscripts: StateFlow<List<String>> = _ieltsTranscripts.asStateFlow()

    private val _ieltsEvaluation = MutableStateFlow<IeltsBandEvaluation?>(null)
    val ieltsEvaluation: StateFlow<IeltsBandEvaluation?> = _ieltsEvaluation.asStateFlow()

    private val _isEvaluatingIelts = MutableStateFlow(false)
    val isEvaluatingIelts: StateFlow<Boolean> = _isEvaluatingIelts.asStateFlow()

    // Pattern Drills & STAR CV Lab State
    private val _selectedPatternDrill = MutableStateFlow(PreBuiltCatalog.patternDrills.first())
    val selectedPatternDrill: StateFlow<PatternDrill> = _selectedPatternDrill.asStateFlow()

    private val _starCvCoachingOutput = MutableStateFlow<String?>(null)
    val starCvCoachingOutput: StateFlow<String?> = _starCvCoachingOutput.asStateFlow()

    private val _isGeneratingStarCv = MutableStateFlow(false)
    val isGeneratingStarCv: StateFlow<Boolean> = _isGeneratingStarCv.asStateFlow()

    // 100+ Video Lessons Academy State (108 Video Lessons)
    val allVideoLessons: List<VideoLesson> = VideoLessonsCatalog.allLessons

    private val _selectedVideoLesson = MutableStateFlow(VideoLessonsCatalog.allLessons.first())
    val selectedVideoLesson: StateFlow<VideoLesson> = _selectedVideoLesson.asStateFlow()

    private val _videoLessonCategoryFilter = MutableStateFlow("All (108)")
    val videoLessonCategoryFilter: StateFlow<String> = _videoLessonCategoryFilter.asStateFlow()

    private val _videoLessonSearchQuery = MutableStateFlow("")
    val videoLessonSearchQuery: StateFlow<String> = _videoLessonSearchQuery.asStateFlow()

    private val _completedVideoLessonIds = MutableStateFlow(setOf("vid_lesson_1", "vid_lesson_2", "vid_lesson_3"))
    val completedVideoLessonIds: StateFlow<Set<String>> = _completedVideoLessonIds.asStateFlow()

    private val _videoPlaybackRate = MutableStateFlow(1.0f)
    val videoPlaybackRate: StateFlow<Float> = _videoPlaybackRate.asStateFlow()

    // Status Banner / Toast Message
    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    // Dismissed OTA dialog tracker
    private val _dismissedOtaVersion = MutableStateFlow(1L)
    val dismissedOtaVersion: StateFlow<Long> = _dismissedOtaVersion.asStateFlow()

    init {
        seedInitialDataIfNeeded()
    }

    private fun seedInitialDataIfNeeded() {
        viewModelScope.launch {
            val profileResult = repository.getUserProfileOnce(currentUserId)
            val existingProfile = profileResult.getOrNull()
            if (existingProfile == null) {
                repository.saveUserProfile(
                    UserProfile(
                        userId = currentUserId,
                        displayName = initialDisplayName.ifBlank { "Learner" },
                        englishLevel = "Beginner",
                        helpLanguage = "Bangla",
                        selectedGoals = listOf("Everyday English", "Job interviews", "IELTS speaking"),
                        streakDays = 7L,
                        minutesToday = 12L,
                        fluencyScore = 78L,
                        dailyChallengeMinutes = 3L,
                        onboardingCompleted = false
                    )
                )
                // Seed starter 3-Tone Vocabulary Bank items so Recall Quiz works immediately
                repository.addVocabEntry(
                    originalSentence = "I could not come to office yesterday because I had a fever.",
                    casualTone = "Hey, I couldn't make it to work yesterday—I was down with a fever.",
                    formalTone = "I was unable to attend the office yesterday due to a sudden fever.",
                    idiomaticTone = "I had to call in sick yesterday because I was running a temperature.",
                    banglaMeaning = "আমি কালকে অফিসে যেতে পারিনি কারণ আমার জ্বর ছিল।",
                    vocabHighlight = "call in sick / down with a fever"
                )
                repository.addVocabEntry(
                    originalSentence = "I want to improve my English speaking confidence.",
                    casualTone = "I really wanna get more confident speaking English.",
                    formalTone = "My primary objective is to enhance my spoken English proficiency.",
                    idiomaticTone = "I'm keen to find my voice and speak English like a natural.",
                    banglaMeaning = "আমি ইংরেজিতে কথা বলার আত্মবিশ্বাস বাড়াতে চাই।",
                    vocabHighlight = "find my voice"
                )
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (screen != AppScreen.LIVE_3D_CALL) {
            stopCallTimer()
            voiceController.stopSpeechRecognition()
        }
        _currentScreen.value = screen
    }

    fun clearStatusMessage() {
        _statusBannerMessage.value = null
    }

    fun postStatusMessage(msg: String) {
        _statusBannerMessage.value = msg
    }

    // --- ONBOARDING STEP 1 & 2 ---
    fun toggleGoalSelection(goalId: String) {
        val current = userProfile.value
        val updatedGoals = if (goalId in current.selectedGoals) {
            if (current.selectedGoals.size > 1) current.selectedGoals - goalId else current.selectedGoals
        } else {
            (current.selectedGoals + goalId).take(6)
        }
        viewModelScope.launch {
            repository.saveUserProfile(current.copy(selectedGoals = updatedGoals))
        }
    }

    fun updateHelpLanguage(language: String) {
        val current = userProfile.value
        viewModelScope.launch {
            repository.saveUserProfile(current.copy(helpLanguage = language))
        }
    }

    fun updateEnglishLevel(level: String) {
        val current = userProfile.value
        viewModelScope.launch {
            repository.saveUserProfile(current.copy(englishLevel = level))
        }
    }

    fun completeOnboardingAndOpenHome() {
        val current = userProfile.value
        viewModelScope.launch {
            repository.saveUserProfile(current.copy(onboardingCompleted = true))
            _currentScreen.value = AppScreen.HOME_DASHBOARD
        }
    }

    fun startTwoMinuteLevelCheckCall() {
        val levelCheckScenario = PracticeScenario(
            id = "level_check_call",
            title = "2-Minute Speaking Level Check",
            badgeLevel = "Easy",
            category = "Everyday English",
            banglaSummary = "২ মিনিটের সংক্ষিপ্ত ভিডিও কলে আপনার স্পিকিং লেভেল ও ফ্লুয়েন্সি যাচাই করুন।",
            roleplayPrompt = "Conduct a friendly 2-minute English level check with the user. Ask about their daily life, interests, and goals, and encourage them warmly.",
            defaultTutorId = "sarah_us",
            starterLine = "Hi! Welcome to your 2-minute level check. Tell me a little about yourself and why you'd like to practice English!",
            starterHint = "Hi Sarah! I want to speak fluent English at work and while traveling."
        )
        startIncomingCall(levelCheckScenario, PreBuiltCatalog.tutors.first())
    }

    // --- SCENARIOS HUB FILTERING ---
    fun updateScenarioSearch(query: String) {
        _scenarioSearchQuery.value = query
    }

    fun updateScenarioFilterChip(chip: String) {
        _scenarioFilterChip.value = chip
    }

    // --- INCOMING CALL & LIVE 3D VIDEO CALL ---
    fun selectTutor(tutor: TutorPersona) {
        _selectedTutor.value = tutor
        val p = userProfile.value
        voiceController.resolveCascadeLabel(
            preferredEngine = tutor.voiceEngine,
            fishKey1 = p.fishAudioKey1,
            fishKey2 = p.fishAudioKey2,
            elevenKey = p.elevenLabsKey
        )
    }

    fun startIncomingCall(
        scenario: PracticeScenario = _selectedScenario.value,
        tutor: TutorPersona? = null
    ) {
        _selectedScenario.value = scenario
        val resolvedTutor = tutor
            ?: allTutors.value.find { it.id == scenario.defaultTutorId }
            ?: allTutors.value.first()
        selectTutor(resolvedTutor)
        _currentScreen.value = AppScreen.INCOMING_CALL
    }

    fun answerVideoCall() {
        val scenario = _selectedScenario.value
        val tutor = _selectedTutor.value
        val initialCoaching = StructuredAiTurnResponse(
            reply = scenario.starterLine,
            emotion = "happy",
            betterWay = "",
            casualTone = scenario.starterHint,
            formalTone = "I would be delighted to discuss this topic with you today.",
            idiomaticTone = "Let's dive right in and chat about this!",
            banglaTip = "টিউটরের প্রশ্নের উত্তরে নিচের Hint বাটনে ট্যাপ করুন অথবা সরাসরি মাইক্রোফোনে (English বা বাংলা Mic) কথা বলুন।",
            vocabWord = "dive right in (সরাসরি শুরু করা)",
            hintForUser = scenario.starterHint
        )
        _callHistory.value = listOf(
            ChatTurnMessage(
                id = UUID.randomUUID().toString(),
                isUser = false,
                text = scenario.starterLine,
                structuredCoaching = initialCoaching
            )
        )
        _latestCoaching.value = initialCoaching
        _callDurationSeconds.value = 0L
        _sessionFillerCount.value = 0
        _sessionWordCount.value = 0
        _currentScreen.value = AppScreen.LIVE_3D_CALL

        startCallTimer()
        voiceController.speakWithLipSync(
            text = scenario.starterLine,
            languageTag = tutor.languageTag
        )
    }

    private fun startCallTimer() {
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _callDurationSeconds.value += 1L
                // Every 60 seconds of active video call, increment user's daily speaking minutes & challenge progress
                if (_callDurationSeconds.value > 0 && _callDurationSeconds.value % 60L == 0L) {
                    val p = userProfile.value
                    repository.saveUserProfile(
                        p.copy(
                            minutesToday = p.minutesToday + 1L,
                            dailyChallengeMinutes = (p.dailyChallengeMinutes + 1L).coerceAtMost(5L),
                            fluencyScore = (p.fluencyScore + 1L).coerceAtMost(99L)
                        )
                    )
                }
            }
        }
    }

    private fun stopCallTimer() {
        callTimerJob?.cancel()
        callTimerJob = null
    }

    fun endVideoCall() {
        stopCallTimer()
        voiceController.stopSpeaking()
        voiceController.stopSpeechRecognition()
        // Award at least 1 minute of progress if the call lasted >= 15 seconds
        if (_callDurationSeconds.value >= 15L) {
            val p = userProfile.value
            viewModelScope.launch {
                repository.saveUserProfile(
                    p.copy(
                        minutesToday = p.minutesToday + 1L,
                        dailyChallengeMinutes = (p.dailyChallengeMinutes + 1L).coerceAtMost(5L)
                    )
                )
            }
        }
        _currentScreen.value = AppScreen.HOME_DASHBOARD
    }

    fun toggleBanglaMicMode(enableBangla: Boolean) {
        _isBanglaMicActive.value = enableBangla
    }

    fun toggleEyeContactPreview() {
        _isEyeContactPreviewOn.value = !_isEyeContactPreviewOn.value
    }

    fun startVoiceCaptureInCall() {
        val banglaMode = _isBanglaMicActive.value
        voiceController.startSpeechRecognition(
            isBanglaMode = banglaMode,
            onFinalTranscript = { spokenText ->
                submitUserSpeechTurn(spokenText, wasBanglaInput = banglaMode)
            },
            onErrorMessage = { err ->
                _statusBannerMessage.value = err
            }
        )
    }

    fun stopVoiceCaptureInCall() {
        voiceController.stopSpeechRecognition()
    }

    fun submitUserSpeechTurn(
        spokenText: String,
        wasBanglaInput: Boolean = _isBanglaMicActive.value
    ) {
        val trimmed = spokenText.trim()
        if (trimmed.isEmpty() || _isAiThinking.value) return

        val (words, fillers) = VoiceAndLipSyncController.countWordsAndFillers(trimmed)
        _sessionWordCount.value += words
        _sessionFillerCount.value += fillers

        val userMsg = ChatTurnMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = trimmed,
            wasBanglaInput = wasBanglaInput,
            fillerCount = fillers,
            wordCount = words
        )
        val updatedHistory = (_callHistory.value + userMsg).takeLast(14)
        _callHistory.value = updatedHistory
        _isAiThinking.value = true

        val profile = userProfile.value
        val tutor = _selectedTutor.value
        val scenario = _selectedScenario.value

        viewModelScope.launch {
            val result = aiEngine.generateConversationTurn(
                userSpeech = trimmed,
                wasBanglaInput = wasBanglaInput,
                scenario = scenario,
                tutor = tutor,
                englishLevel = profile.englishLevel,
                helpLanguage = profile.helpLanguage,
                slidingHistory = updatedHistory,
                backupKeys = listOf(profile.backupGeminiKey1, profile.backupGeminiKey2)
            )
            _activeKeySlotLabel.value = aiEngine.getActiveKeySlotLabel()
            _isAiThinking.value = false

            result.onSuccess { coaching ->
                _latestCoaching.value = coaching
                val aiMsg = ChatTurnMessage(
                    id = UUID.randomUUID().toString(),
                    isUser = false,
                    text = coaching.reply,
                    structuredCoaching = coaching
                )
                _callHistory.value = (_callHistory.value + aiMsg).takeLast(14)

                // Speak the reply and animate 3D tutor lips
                val spokenOutput = if (wasBanglaInput && coaching.betterWay.isNotBlank()) {
                    "In English, you can say: ${coaching.betterWay}. ${coaching.reply}"
                } else {
                    coaching.reply
                }
                voiceController.speakWithLipSync(
                    text = spokenOutput,
                    languageTag = tutor.languageTag
                )

                // Auto-save 3-tone rewrite into the user's Cloud Vocabulary Bank
                if (coaching.casualTone.isNotBlank() && coaching.formalTone.isNotBlank()) {
                    repository.addVocabEntry(
                        originalSentence = coaching.betterWay.ifBlank { trimmed },
                        casualTone = coaching.casualTone,
                        formalTone = coaching.formalTone,
                        idiomaticTone = coaching.idiomaticTone.ifBlank { coaching.casualTone },
                        banglaMeaning = coaching.banglaTip,
                        vocabHighlight = coaching.vocabWord.ifBlank { "Speaking turn phrase" }
                    )
                }
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "AI response failed. Check connection or failover keys."
            }
        }
    }

    fun speakTextSample(
        text: String,
        languageTag: String = _selectedTutor.value.languageTag,
        speechRate: Float = 0.96f
    ) {
        voiceController.speakWithLipSync(
            text = text,
            languageTag = languageTag,
            speechRate = speechRate
        )
    }

    // --- 100+ VIDEO LESSONS ACADEMY (108 INTERACTIVE 3D LESSONS) ---
    fun selectVideoLesson(lesson: VideoLesson) {
        _selectedVideoLesson.value = lesson
    }

    fun playVideoLessonAudio(lesson: VideoLesson, speechRate: Float = _videoPlaybackRate.value) {
        _selectedVideoLesson.value = lesson
        val tutor = allTutors.value.find { it.id == lesson.tutorId } ?: _selectedTutor.value
        val fullLessonNarration = "${lesson.videoScriptEnglish} Practice saying: ${lesson.shadowPracticeSentence}"
        voiceController.speakWithLipSync(
            text = fullLessonNarration,
            languageTag = tutor.languageTag,
            speechRate = speechRate
        )
    }

    fun stopVideoLessonAudio() {
        voiceController.stopSpeaking()
    }

    fun toggleVideoLessonCompleted(lessonId: String) {
        val current = _completedVideoLessonIds.value
        if (lessonId in current) {
            _completedVideoLessonIds.value = current - lessonId
        } else {
            _completedVideoLessonIds.value = current + lessonId
            _statusBannerMessage.value = "Lesson marked complete! (${current.size + 1}/108 completed)"
        }
    }

    fun updateVideoCategoryFilter(category: String) {
        _videoLessonCategoryFilter.value = category
    }

    fun updateVideoSearchQuery(query: String) {
        _videoLessonSearchQuery.value = query
    }

    fun setVideoPlaybackRate(rate: Float) {
        _videoPlaybackRate.value = rate.coerceIn(0.75f, 1.35f)
    }

    fun startShadowMicForVideoLesson(lesson: VideoLesson) {
        voiceController.startSpeechRecognition(
            isBanglaMode = false,
            onFinalTranscript = { transcript ->
                _completedVideoLessonIds.value = _completedVideoLessonIds.value + lesson.id
                _statusBannerMessage.value = "Great shadow speaking: \"$transcript\"! Lesson #${lesson.lessonNumber} marked complete ✓"
            },
            onErrorMessage = { err ->
                _statusBannerMessage.value = err
            }
        )
    }

    fun startCallFromVideoLesson(lesson: VideoLesson) {
        voiceController.stopSpeaking()
        val matchingTutor = allTutors.value.find { it.id == lesson.tutorId } ?: allTutors.value.first()
        val scenarioFromLesson = PracticeScenario(
            id = "video_call_${lesson.id}",
            title = "Lesson #${lesson.lessonNumber}: ${lesson.title}",
            badgeLevel = if (lesson.level == "Beginner") "Easy" else "Intermediate",
            category = lesson.category,
            banglaSummary = lesson.banglaSummary,
            roleplayPrompt = lesson.roleplayPrompt,
            defaultTutorId = lesson.tutorId,
            starterLine = "${lesson.videoScriptEnglish} Now let's practice this together—go ahead and try saying the pattern!",
            starterHint = lesson.shadowPracticeSentence
        )
        startIncomingCall(scenario = scenarioFromLesson, tutor = matchingTutor)
    }

    // --- MODULE 07 & 08: CUSTOM FAMILIAR PERSON & VOICE CLONING ---
    fun saveFamiliarPersonProfile(
        name: String,
        relationship: String,
        accent: String,
        mouthYRatio: Long,
        mouthWidthRatio: Long,
        voiceEngine: String,
        photoUri: String?,
        voiceSampleNote: String?
    ) {
        viewModelScope.launch {
            val res = repository.addFamiliarPerson(
                name = name,
                relationship = relationship,
                accent = accent,
                mouthYRatio = mouthYRatio,
                mouthWidthRatio = mouthWidthRatio,
                voiceEngine = voiceEngine,
                photoUri = photoUri,
                voiceSampleNote = voiceSampleNote
            )
            if (res.isSuccess) {
                _statusBannerMessage.value = "Saved Familiar Person '$name' with calibrated Lip-Sync & $voiceEngine!"
            } else {
                _statusBannerMessage.value = res.exceptionOrNull()?.localizedMessage ?: "Failed to save familiar profile"
            }
        }
    }

    fun deleteFamiliarPersonProfile(id: String) {
        viewModelScope.launch {
            repository.deleteFamiliarPerson(id)
        }
    }

    // --- MODULE 09: PRONUNCIATION COACH LAB (PHONEME ENGINE) ---
    fun selectPhonemeChallenge(challenge: PhonemeChallenge) {
        _selectedPhonemeChallenge.value = challenge
        _wordPronunciationScores.value = emptyList()
    }

    fun evaluatePronunciationAttempt(spokenAttempt: String) {
        val targetSentence = _selectedPhonemeChallenge.value.sentence
        val targetWords = targetSentence.split(Regex("\\s+")).filter { it.isNotBlank() }
        val spokenWordsSet = spokenAttempt
            .lowercase(Locale.ROOT)
            .replace(Regex("[^a-z0-9\\s]"), "")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() }
            .toSet()

        val evaluated = targetWords.map { rawWord ->
            val clean = rawWord.lowercase(Locale.ROOT).replace(Regex("[^a-z0-9]"), "")
            val matched = clean in spokenWordsSet || spokenWordsSet.any {
                it.startsWith(clean.take(3)) && clean.length >= 3
            }
            WordPronunciationScore(
                word = rawWord,
                isAccurate = matched,
                ipa = "/$clean/",
                tip = if (matched) {
                    "সঠিক উচ্চারণ (Green - Accurate)"
                } else {
                    "আরও স্পষ্টভাবে উচ্চারণ করুন: ${_selectedPhonemeChallenge.value.banglaArticulationTip}"
                }
            )
        }
        _wordPronunciationScores.value = evaluated
    }

    fun startPronunciationVoiceCapture() {
        voiceController.startSpeechRecognition(
            isBanglaMode = false,
            onFinalTranscript = { transcript ->
                evaluatePronunciationAttempt(transcript)
            },
            onErrorMessage = { msg ->
                _statusBannerMessage.value = msg
            }
        )
    }

    // --- MODULE 10: IELTS SPEAKING MOCK EXAMINER SUITE ---
    fun selectIeltsCueCard(card: IeltsCueCard) {
        _selectedIeltsCard.value = card
        _currentIeltsPart.value = 1
        _ieltsTranscripts.value = emptyList()
        _ieltsEvaluation.value = null
    }

    fun setIeltsPart(part: Int) {
        _currentIeltsPart.value = part.coerceIn(1, 3)
    }

    fun addIeltsSpokenResponse(responseText: String) {
        if (responseText.isBlank()) return
        val updated = _ieltsTranscripts.value + "Part ${_currentIeltsPart.value}: $responseText"
        _ieltsTranscripts.value = updated
        requestIeltsBandEvaluation(updated)
    }

    fun captureIeltsVoiceResponse() {
        voiceController.startSpeechRecognition(
            isBanglaMode = false,
            onFinalTranscript = { transcript ->
                addIeltsSpokenResponse(transcript)
            },
            onErrorMessage = { msg ->
                _statusBannerMessage.value = msg
            }
        )
    }

    private fun requestIeltsBandEvaluation(transcripts: List<String>) {
        if (transcripts.isEmpty()) return
        _isEvaluatingIelts.value = true
        viewModelScope.launch {
            val result = aiEngine.evaluateIeltsSpeaking(
                partNumber = _currentIeltsPart.value,
                topicTitle = _selectedIeltsCard.value.cueCardTopic,
                userTranscripts = transcripts
            )
            _isEvaluatingIelts.value = false
            result.onSuccess { eval ->
                _ieltsEvaluation.value = eval
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "Could not compute IELTS Band Score."
            }
        }
    }

    // --- MODULE 11: 3-TONE VOCABULARY BANK & RECALL QUIZ ---
    fun recordVocabQuizAnswer(vocabId: String, currentMastery: Long, correct: Boolean) {
        val nextMastery = if (correct) (currentMastery + 1L).coerceAtMost(5L) else (currentMastery - 1L).coerceAtLeast(0L)
        viewModelScope.launch {
            repository.updateVocabMastery(vocabId, nextMastery)
        }
    }

    // --- PATTERN DRILLS & STAR CV LAB ---
    fun selectPatternDrill(drill: PatternDrill) {
        _selectedPatternDrill.value = drill
    }

    fun generateStarMethodAnswer(cvContext: String, candidateDraft: String) {
        if (candidateDraft.isBlank()) return
        _isGeneratingStarCv.value = true
        viewModelScope.launch {
            val res = aiEngine.generateStarCvCoaching(cvContext, candidateDraft)
            _isGeneratingStarCv.value = false
            res.onSuccess { text ->
                _starCvCoachingOutput.value = text
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "Failed to generate STAR response."
            }
        }
    }

    // --- MODULE 12: CLOUD WEB ADMIN & IN-APP AUTO-UPDATER (OTA) ---
    fun addCloudTopicFromAdmin(
        title: String,
        badgeLevel: String,
        category: String,
        roleplayPrompt: String
    ) {
        viewModelScope.launch {
            val res = repository.addCustomTopic(
                title = title,
                badgeLevel = badgeLevel,
                category = category,
                roleplayPrompt = roleplayPrompt
            )
            if (res.isSuccess) {
                _statusBannerMessage.value = "Live OTA Topic '$title' published to Scenarios Hub!"
            } else {
                _statusBannerMessage.value = res.exceptionOrNull()?.localizedMessage ?: "Failed to publish topic"
            }
        }
    }

    fun deleteCloudTopicFromAdmin(topicId: String) {
        viewModelScope.launch {
            repository.deleteCustomTopic(topicId)
        }
    }

    fun updateAdminOtaAndApiKeys(
        homeBannerText: String,
        latestVersionCode: Long,
        backupGeminiKey1: String,
        backupGeminiKey2: String,
        fishAudioKey1: String,
        fishAudioKey2: String,
        elevenLabsKey: String
    ) {
        val current = userProfile.value
        viewModelScope.launch {
            val res = repository.saveUserProfile(
                current.copy(
                    homeBannerText = homeBannerText,
                    latestVersionCode = latestVersionCode,
                    backupGeminiKey1 = backupGeminiKey1,
                    backupGeminiKey2 = backupGeminiKey2,
                    fishAudioKey1 = fishAudioKey1,
                    fishAudioKey2 = fishAudioKey2,
                    elevenLabsKey = elevenLabsKey
                )
            )
            if (res.isSuccess) {
                _statusBannerMessage.value = "Cloud Admin & Multi-API Failover settings updated live!"
            } else {
                _statusBannerMessage.value = res.exceptionOrNull()?.localizedMessage ?: "Failed to update Admin config"
            }
        }
    }

    fun dismissOtaPopup(versionCode: Long) {
        _dismissedOtaVersion.value = versionCode
    }

    override fun onCleared() {
        super.onCleared()
        stopCallTimer()
        voiceController.release()
    }

    companion object {
        private const val TAG = "SpeakingPartnerVM"

        fun provideFactory(
            context: Context,
            currentUserId: String,
            displayName: String
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appContext = context.applicationContext
                val databaseId = appContext.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                val repo = SpeakingPartnerRepository(db)
                val aiEngine = SpeakingAiEngine()
                val viewModelScopePlaceholder = kotlinx.coroutines.CoroutineScope(
                    kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.Dispatchers.Main.immediate
                )
                val voiceCtrl = VoiceAndLipSyncController(appContext, viewModelScopePlaceholder)
                SpeakingPartnerViewModel(
                    repository = repo,
                    aiEngine = aiEngine,
                    voiceController = voiceCtrl,
                    currentUserId = currentUserId,
                    initialDisplayName = displayName
                )
            }
        }
    }
}
