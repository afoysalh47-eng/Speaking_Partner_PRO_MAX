# AI Speaking Partner PRO MAX

An immersive face-to-face English speaking coach and video call simulator designed to eliminate speaking anxiety through live animated 3D tutor calls, bilingual Bangla-to-English voice coaching, real-time 3-tone grammar rewrites, phoneme pronunciation analysis, IELTS mock exams, and familiar person photo/voice cloning.

### User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural and product choices have been confirmed from your answers and integrated with the full 12-module specification from your guide:

- **Confirmed Decision 1 — Hybrid AI & Voice Engine**: Out-of-the-box conversational intelligence powered by **Firebase AI Logic (`gemini-3.8-flash`)** combined with a **Circular Multi-API Failover Loop** supporting user-configured Gemini fallback keys and voice cloning providers (Fish Audio, ElevenLabs, Hugging Face Zero-Shot Clone) with automatic fallback to native Android Speech-to-Text (`SpeechRecognizer`) and Text-to-Speech (`TextToSpeech`).
- **Confirmed Decision 2 — Cloud Persistence & Authentication**: Full **Google Sign-In via Jetpack Credential Manager** paired with **Firebase Firestore** to sync user onboarding goals, streak/fluency stats, 3-Tone Vocabulary Bank flashcards, custom Familiar Person profiles, and live Admin/OTA topics and banners across devices.
- **Confirmed Decision 3 — Switchable Multilingual Help & Bangla Coaching**: Interface guidance switchable between **Bangla (বাংলা), English, and Hindi** during onboarding or in settings, while maintaining 1-line Bangla grammar explanations (`banglaTip`), Bengali phonetic tips in the Pronunciation Coach Lab, and a live **BN বাংলা Mic / EN Mic Toggle** during video calls.

---

### 1. Overview & Core Concept

- **What It Does**: Replaces intimidating text or audio-only language bots with a realistic **Face-to-Face Video Call** experience. Users receive a realistic incoming call screen, talk face-to-face with an animated 3D tutor (with head nodding, blinking, and syllable-synced A/O/E/M mouth shapes) alongside their own front-camera Eye-Contact PiP preview, and receive instant grammar corrections, 3-tone rewrites (Casual, Business Formal, Native Idiomatic), IPA phoneme coloring, and IELTS Band 4.5–8.5 scoring.
- **Target Audience / Persona**: Bengali, Hindi, and South Asian English learners, university students, job seekers, and IELTS candidates who understand English grammar rules passively but struggle with speaking anxiety or hesitation in real-world face-to-face conversations.
- **Key Value**: Combines the best workflows of **Speak** (pattern drills & 3-tone rewriter), **ELSA Speak** (word-by-word phoneme coloring & Bangla pronunciation tips), **Duolingo Max** (interactive roleplay missions & Explain My Answer), **Cambly** (unlimited bilingual free-talk & IELTS mock examiner), and **Praktika AI** (3D avatar, STAR method CV interview lab, and familiar person photo/voice clone) in one unified Android application.

---

### 2. User Experience & Visual Design

#### Key User Flows (All 12 Modules)

1. **Module 01 — Onboarding Step 1 (Choose Your Goals)**:
   - Displays a progress bar (`Step 1 of 3`), a `2×3` grid of 6 interactive goal cards (*Everyday English*, *Study & campus*, *Job interviews*, *Work & business*, *Travel & abroad*, *IELTS speaking*), a bottom **Help language** selector (`বাংলা / Bangla`, `English`, `Hindi`), and a Deep Purple **Continue →** CTA.
2. **Module 02 — Onboarding Step 2 (Pick Your Level)**:
   - Displays a circular AI tutor portrait surrounded by animated sound waves, 3 selectable proficiency cards (*Beginner • A1–A2*, *Intermediate • B1–B2*, *Advanced • C1–C2*), an interactive **Take a 2-minute level check** quick call trigger, and a **Build my plan →** button.
3. **Module 03 — Home Dashboard & Daily Challenge**:
   - Top header with user avatar, active level badge (`Beginner • A1`), and language/admin quick actions.
   - Hero card (*"Let's talk about your day"*) with a prominent **Start speaking** video call button.
   - Live telemetry row showing **7-day streak bar**, **Minutes spoken today** (`12 min today`), **Fluency score** (`78 Fluency`), and **Today's 5-minute challenge** progress bar (`3/5 min`).
   - Quick-launch cards for all Pro Labs (*Pronunciation Coach*, *IELTS Mock Suite*, *3-Tone Vocab Bank*, *Familiar Person Clone*, *Pattern Drills & STAR CV Lab*).
4. **Module 04 — Practice Your World (Scenarios Hub)**:
   - Search bar (*"Find a topic or situation"*), 3 filter chips (*For you*, *Beginner*, *All topics*), and scenario cards with *Easy (Mint)* and *Intermediate (Amber)* badges:
     - *Meet someone new*, *At a restaurant*, *Campus conversation*, *Job interview*, *Work meeting*, *At the airport*, plus Duolingo Max-inspired roleplay missions (*Return a faulty gadget*, *See a doctor & explain symptoms*, *Handle wrong bill at restaurant*) and any cloud-synced admin topics.
   - Bottom **Pronunciation Coach** promotional banner.
5. **Module 05 — Incoming Video Call Ringing Screen**:
   - Triggered when starting a scenario or tutor call. Displays concentric pulsing radar rings, the selected tutor's portrait and accent badge (*Sarah — US*, *Alex — UK*, *Maya — AU*, *Prof. James — IELTS UK*, or *Custom Familiar Person*), and a vibrant Mint/Green **Answer Video Call** button to mentally prime the user for a real conversation.
6. **Module 06 — Live 3D Video Call & Eye-Contact PiP**:
   - Full-screen interactive call stage featuring a 60 FPS 3D-transformed animated tutor canvas/avatar (`graphicsLayer` 3D tilt, natural head nod, eye blinking, emotion states: *happy, encouraging, curious, proud*, and real-time `A, O, E, M` viseme lip-sync).
   - Top-right draggable **Eye-Contact ON** front-camera preview (`CameraX` with graceful permission flow and privacy toggle).
   - Top bar **BN বাংলা Mic / EN Mic Toggle** for unlimited Bilingual Bangla-to-English speech input (speak in Bangla -> AI shows natural English phrasing, pronounces it, and prompts user to repeat) plus filler-word counter (`um, uh, like`).
   - Bottom live subtitle overlay, expandable **Better Way & Bangla Grammar Tip** banner, 3-Tone quick chips, and **Hint for Next Turn** pill.
7. **Module 07 — Custom Photo Upload & Lip-Sync Engine**:
   - Photo picker (`PickVisualMedia`) allowing users to upload unlimited photos of familiar people (friend, boss, teacher).
   - Interactive **Mouth Position & Width Calibration Sliders** overlaying a live lip-sync preview markers (`mouthY`, `mouthWidth`, `jawOpenScale`) so the uploaded photo's lips animate naturally in sync with speech syllables during video calls.
8. **Module 08 — Free Voice Cloning & Multi-API Failover**:
   - Record or pick a 10–30 second audio sample for custom voice profiles.
   - Multi-engine configuration and health status panel showing the **Circular Failover Cascade**: *Fish Audio API (Key 1 → Key 2)* → *ElevenLabs API* → *Hugging Face Zero-Shot Clone* → *Android Device Neural TTS*, plus *Gemini Multi-Key Rotation (Firebase AI Logic → Backup Key #1 → Key #2)* while preserving the sliding conversation context window.
9. **Module 09 — Pronunciation Coach Lab (Phoneme Engine)**:
   - Inspired by ELSA Speak: users read target sentences or custom phrases aloud.
   - Evaluates speech word-by-word, coloring accurate words in **Mint Green** and words needing practice in **Amber**, accompanied by **IPA phonetic transcription** and **Bangla articulation tips** for challenging sounds (`/θ/`, `/ð/`, `V vs W`, silent letters, `-ed` endings).
10. **Module 10 — IELTS Speaking Mock Examiner Suite**:
    - Official 3-part simulator with *Prof. James (UK Examiner)*:
      - **Part 1**: Introduction & interview (4–5 min).
      - **Part 2**: Interactive 2-Minute Cue Card with 1-minute preparation timer & notes pad.
      - **Part 3**: Two-way abstract discussion.
    - Real-time and post-session **Estimated Band Score (Band 4.5 – 8.5)** breakdown across *Fluency & Coherence*, *Lexical Resource*, *Grammatical Range*, and *Pronunciation*.
11. **Module 11 — 3-Tone Vocabulary Bank & Recall Quiz**:
    - Automatically saves user sentences transformed into **Casual**, **Business Formal**, and **Native Idiomatic** tones alongside Bangla meanings and vocabulary highlights.
    - Includes an interactive **Quiz Me** flashcard recall mode with spaced repetition and mastery tracking.
12. **Module 12 — Cloud Admin Panel, Career/CV Lab & In-App Auto-Updater (OTA)**:
    - **In-App Cloud Admin & OTA Manager**: Allows adding/editing live practice topics, updating home banners, rotating backup API keys, and bumping `latestVersionCode` in Firestore. When `latestVersionCode > currentAppVersionCode`, an automatic **"New Update Available! [Update Now]"** modal dialog triggers with one-tap APK update flow.
    - **Praktika & Speak Pro Additions**: Includes **Speaking Pattern Drills** (*"I am used to..."*, *"I have been meaning to..."*) and **STAR Method CV/Job Interview Lab** where users paste a CV or Job Description for tailored mock interviews and salary negotiation roleplay.

#### Visual Identity & Theme
- **Aesthetic Direction**: **Soft Lavender Editorial & Tactile Studio** — calm, confidence-building, and modern, specifically engineered to lower cortisol and speaking anxiety while maintaining crisp data legibility.
- **Color Palette & Mood**:
  - **Canvas & Surfaces**: Soft Lavender Mist background (`#F6F4FF`), Pure Elevated Card Surface (`#FFFFFF`), Tinted Lavender Container (`#EDE9FE`), and Deep Night Call Stage (`#13111C`) for immersive video calls.
  - **Primary & Secondary Accents**: Deep Royal Purple (`#5B21B6` / `#6D28D9`) for primary actions, active states, and AI aura; Fresh Mint (`#10B981` / `#D1FAE5`) for "Easy" badges, accurate pronunciation, and "Answer Call" CTAs.
  - **Semantic Status**: Warm Amber (`#F59E0B` / `#FEF3C7`) for "Intermediate" badges and phoneme practice highlights; Coral Rose (`#EF4444`) for end-call and mic recording pulse.
- **Typography & Hierarchy**:
  - Bundled locally via `font-util`: **Plus Jakarta Sans** for bold, friendly display headers and card titles; **Inter** for clean UI body copy and subtitles; **JetBrains Mono** for IPA phonetic transcriptions, timers, and Band Score metrics.
- **Interactive Feedback & Motion**:
  - Concentric radar pulse rings on the Incoming Call screen; real-time 60 FPS `Matrix4` / `graphicsLayer` 3D head tilt, eye blink, and syllable viseme (`A, O, E, M`) mouth animation during calls; animated soundwave rings around active microphones.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Hybrid Firebase AI Logic + Multi-Key Circular Failover**
  - *Chosen Approach*: Use provisioned **Firebase AI Logic (`gemini-3.8-flash`)** with structured `Schema.obj` JSON output as the primary zero-configuration engine, backed by a client-side Circular Failover manager that rotates to user-configured backup Gemini keys if rate limits occur.
  - *Why*: Guarantees the app works immediately out of the box without requiring the user to hunt for API keys, while honoring the specification's multi-key failover architecture for uninterrupted marathon speaking sessions.
- **Decision 2: Real-Time 2.5D/3D Avatar & Custom Photo Lip-Sync Engine**
  - *Chosen Approach*: Build a dedicated Compose `Canvas` + `graphicsLayer` 3D perspective engine that renders both built-in tutor personas and user-uploaded gallery photos with calibrated mouth region warping (`A, O, E, M` viseme shapes), eye blinks, and emotional head movements synchronized to speech synthesis callbacks.
  - *Why*: Delivers smooth 60 FPS performance on all Android devices and works seamlessly with any user-uploaded portrait photo without requiring external 3D mesh rigging servers.
- **Decision 3: Cloud Sync via Google Sign-In & Firestore with Offline-Ready Defaults**
  - *Chosen Approach*: Authenticate users via Jetpack Credential Manager (`GetSignInWithGoogleOption`) and store user profiles, 3-Tone Vocabulary Bank items, Familiar Person configurations, and Cloud Admin/OTA config in Firestore (`R.string.firestore_database_id`), pre-seeded with the 6 built-in goals, 6+ scenarios, and 4 built-in tutors.
  - *Why*: Ensures zero data loss across reinstalls and enables live OTA topic/version updates from the Admin module while keeping all built-in scenarios immediately accessible.

---

### 4. Technical Architecture & Data Strategy *(Technical Reference)*

#### Architecture & Component Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        MainActivity (App Check + EdgeToEdge)                │
│                     ┌─────────────────────────────────────────┐             │
│                     │  AuthGate (Google Sign-In / Credential) │             │
│                     └───────────────────┬─────────────────────┘             │
└─────────────────────────────────────────┼───────────────────────────────────┘
                                          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                   Navigation Shell & OTA Update Watcher                     │
├──────────────────┬──────────────────┬──────────────────┬────────────────────┤
│  Onboarding &    │  Scenarios Hub   │  Incoming Call & │  Pro Coaching Labs │
│  Home Dashboard  │  & Roleplay      │  Live 3D Video   │  • Phoneme Coach   │
│  • Step 1 Goals  │  • Search/Filter │  Call Stage      │  • IELTS Mock 1-3  │
│  • Step 2 Level  │  • 6+ Scenarios  │  • 3D Avatar/Lip │  • 3-Tone Vocab    │
│  • Streak/Stats  │  • Pattern Drill │  • CameraX PiP   │  • Custom Clone    │
│  • Daily 5m Goal │  • STAR CV Lab   │  • BN/EN Mic     │  • Cloud Admin/OTA │
└────────┬─────────┴────────┬─────────┴────────┬─────────┴─────────┬──────────┘
         │                  │                  │                   │
         ▼                  ▼                  ▼                   ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                     SpeakingPartnerViewModel (StateFlow)                    │
├─────────────────────────────────────┬───────────────────────────────────────┤
│   AiConversation & Voice Engine     │      Firestore & Cloud Sync Layer     │
│ • Firebase.ai (gemini-3.8-flash)    │ • UserProfile & Streak Progress       │
│ • Structured 7-Field JSON Schema    │ • 3-Tone Vocab Bank & Quiz Flashcards │
│ • Multi-Key Failover Rotator        │ • Familiar Person Photo & Lip Coords  │
│ • SpeechRecognizer (EN / BN-BD)     │ • Cloud Admin Topics, Keys & OTA Code │
│ • Voice Clone Cascade & Android TTS │                                       │
└─────────────────────────────────────┴───────────────────────────────────────┘
```

#### Data Model & State
- **UserProfile**: `userId`, `displayName`, `selectedGoals` (list of 6 goal IDs), `englishLevel` (`Beginner`, `Intermediate`, `Advanced`), `helpLanguage` (`Bangla`, `English`, `Hindi`), `streakDays`, `minutesToday`, `fluencyScore`, `dailyChallengeMinutes`.
- **ScenarioTopic**: `id`, `title`, `difficultyBadge` (`Easy`, `Intermediate`), `category`, `roleplayPrompt`, `tutorId`, `starterHint`, `isCustomCloudTopic`.
- **TutorPersona & FamiliarProfile**: `id`, `name`, `roleTitle`, `accent` (`US`, `UK`, `AU`), `avatarType` (`BuiltIn`, `CustomPhoto`), `photoUri`, `mouthYRatio`, `mouthWidthRatio`, `voiceEngine` (`DeviceTTS`, `FishAudio`, `ElevenLabs`, `HuggingFaceClone`), `voiceSamplePath`.
- **StructuredAiTurnResponse (7-Field Gemini JSON)**: `reply`, `emotion` (`happy`, `encouraging`, `curious`, `proud`), `betterWay`, `casualTone`, `formalTone`, `idiomaticTone`, `banglaTip`, `vocabWord`, `hintForUser`.
- **VocabEntry**: `id`, `userId`, `originalSentence`, `casualTone`, `formalTone`, `idiomaticTone`, `banglaMeaning`, `vocabHighlight`, `masteryLevel`, `createdAt`.
- **CloudAdminConfig**: `latestVersionCode`, `apkDownloadUrl`, `releaseNotes`, `homeBannerText`, `backupGeminiKeys`, `fishAudioKeys`, `elevenLabsKey`.

#### Interactive Component & State Mapping
- **Onboarding & Language Switcher**: Selecting goals and help language (`Bangla`, `English`, `Hindi`) updates `UserProfile` in Firestore and dynamically localizes helper prompts and coaching tips across all screens.
- **Incoming Call -> Live 3D Call Stage**: Tapping any scenario or tutor opens the pulsing Radar Ringing screen; answering launches the 3D Call Stage, initializes `CameraX` front preview (if permitted), starts the session timer, and greets the user in the tutor's accent.
- **Bilingual Speech & 3-Tone Pipeline**: Tapping the **EN Mic** or **BN বাংলা Mic** captures continuous speech via `SpeechRecognizer`, counts words and filler words (`um, uh, like`), sends the transcript and sliding context history to `gemini-3.8-flash` (with automatic failover if needed), renders the 7-field JSON response in real time, animates the tutor's 3D head/eyes and `A, O, E, M` lip visemes while speaking the reply, and auto-saves the 3-tone rewrite to the user's Firestore Vocabulary Bank.
- **Pronunciation Coach & IELTS Evaluator**: Compares spoken transcript tokens against target phonemes or IELTS rubrics, generating word-level Green/Amber accuracy spans, IPA breakdowns with Bangla articulation tips, and Part 1/2/3 Band Scores (4.5–8.5).
- **Cloud Admin & OTA Updater**: Saving a new topic or incrementing `latestVersionCode` in the Admin screen writes directly to Firestore; active clients observing the config immediately display the new topic or trigger the **"New Update Available! [Update Now]"** dialog.
