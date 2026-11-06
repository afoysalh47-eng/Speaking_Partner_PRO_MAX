package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "Learner",
    val englishLevel: String = "Beginner",
    val helpLanguage: String = "Bangla",
    val selectedGoals: List<String> = listOf("Everyday English", "Job interviews"),
    val streakDays: Long = 7L,
    val minutesToday: Long = 12L,
    val fluencyScore: Long = 78L,
    val dailyChallengeMinutes: Long = 3L,
    val onboardingCompleted: Boolean = false,
    val latestVersionCode: Long = 1L,
    val homeBannerText: String = "Speak with confidence! Try Bilingual Bangla-to-English Mic in any live 3D video call.",
    val backupGeminiKey1: String = "",
    val backupGeminiKey2: String = "",
    val fishAudioKey1: String = "",
    val fishAudioKey2: String = "",
    val elevenLabsKey: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toFirestoreMap(isCreate: Boolean): Map<String, Any> {
        val base = mutableMapOf<String, Any?>(
            "userId" to userId,
            "displayName" to displayName.take(80).ifBlank { "Learner" },
            "englishLevel" to englishLevel.take(30).ifBlank { "Beginner" },
            "helpLanguage" to helpLanguage.take(30).ifBlank { "Bangla" },
            "selectedGoals" to selectedGoals.take(10).map { it.take(50) },
            "streakDays" to streakDays.coerceIn(0L, 10000L),
            "minutesToday" to minutesToday.coerceIn(0L, 1440L),
            "fluencyScore" to fluencyScore.coerceIn(0L, 100L),
            "dailyChallengeMinutes" to dailyChallengeMinutes.coerceIn(0L, 1440L),
            "onboardingCompleted" to onboardingCompleted,
            "latestVersionCode" to latestVersionCode.coerceIn(1L, 1000L),
            "homeBannerText" to homeBannerText.take(200),
            "backupGeminiKey1" to backupGeminiKey1.take(200),
            "backupGeminiKey2" to backupGeminiKey2.take(200),
            "fishAudioKey1" to fishAudioKey1.take(200),
            "fishAudioKey2" to fishAudioKey2.take(200),
            "elevenLabsKey" to elevenLabsKey.take(200),
            "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
        )
        if (isCreate || createdAt == null) {
            base["createdAt"] = com.google.firebase.firestore.FieldValue.serverTimestamp()
        } else {
            base["createdAt"] = createdAt
        }
        return base.filterValues { it != null }.mapValues { it.value!! }
    }
}

@IgnoreExtraProperties
data class VocabEntry(
    @DocumentId val id: String = "",
    val userId: String = "",
    val originalSentence: String = "",
    val casualTone: String = "",
    val formalTone: String = "",
    val idiomaticTone: String = "",
    val banglaMeaning: String = "",
    val vocabHighlight: String = "",
    val masteryLevel: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FamiliarPerson(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val relationship: String = "",
    val accent: String = "US",
    val mouthYRatio: Long = 64L,
    val mouthWidthRatio: Long = 38L,
    val voiceEngine: String = "HuggingFace",
    val photoUri: String? = null,
    val voiceSampleNote: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CustomTopic(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val badgeLevel: String = "Easy",
    val category: String = "Everyday English",
    val roleplayPrompt: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

data class LearningGoal(
    val id: String,
    val title: String,
    val banglaDesc: String,
    val hindiDesc: String,
    val englishDesc: String,
    val iconName: String
)

data class PracticeScenario(
    val id: String,
    val title: String,
    val badgeLevel: String, // "Easy" (Mint) or "Intermediate" (Amber)
    val category: String,
    val banglaSummary: String,
    val roleplayPrompt: String,
    val defaultTutorId: String,
    val starterLine: String,
    val starterHint: String,
    val isDuolingoMission: Boolean = false,
    val isCloudCustom: Boolean = false
)

data class TutorPersona(
    val id: String,
    val name: String,
    val roleTitle: String,
    val accentLabel: String,
    val languageTag: String,
    val banglaBio: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val hasGlasses: Boolean = false,
    val hasBeard: Boolean = false,
    val isCustomFamiliar: Boolean = false,
    val customPhotoUri: String? = null,
    val mouthYRatio: Float = 0.64f,
    val mouthWidthRatio: Float = 0.38f,
    val voiceEngine: String = "DeviceTTS"
)

data class StructuredAiTurnResponse(
    val reply: String,
    val emotion: String, // happy, encouraging, curious, proud
    val betterWay: String,
    val casualTone: String,
    val formalTone: String,
    val idiomaticTone: String,
    val banglaTip: String,
    val vocabWord: String,
    val hintForUser: String
)

data class ChatTurnMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val wasBanglaInput: Boolean = false,
    val fillerCount: Int = 0,
    val wordCount: Int = 0,
    val structuredCoaching: StructuredAiTurnResponse? = null
)

data class PhonemeChallenge(
    val id: String,
    val focusSound: String,
    val sentence: String,
    val ipaTranscription: String,
    val banglaArticulationTip: String,
    val difficulty: String
)

data class WordPronunciationScore(
    val word: String,
    val isAccurate: Boolean,
    val ipa: String,
    val tip: String
)

data class PatternDrill(
    val id: String,
    val patternHeader: String,
    val banglaMeaning: String,
    val exampleCasual: String,
    val exampleFormal: String,
    val exampleIdiomatic: String,
    val practicePrompt: String
)

data class IeltsCueCard(
    val id: String,
    val part1Questions: List<String>,
    val cueCardTopic: String,
    val bulletPoints: List<String>,
    val part3Questions: List<String>
)

data class IeltsBandEvaluation(
    val overallBand: Double,
    val fluencyBand: Double,
    val lexicalBand: Double,
    val grammarBand: Double,
    val pronunciationBand: Double,
    val examinerFeedback: String,
    val banglaImprovementPlan: String
)

object PreBuiltCatalog {
    val goals = listOf(
        LearningGoal(
            id = "Everyday English",
            title = "Everyday English",
            banglaDesc = "দৈনন্দিন জীবনের সাধারণ কথোপকথন ও জড়তা কাটানো।",
            hindiDesc = "रोज़मर्रा की बातचीत और झिझक दूर करना।",
            englishDesc = "Daily casual conversations and overcoming speaking hesitation.",
            iconName = "chat"
        ),
        LearningGoal(
            id = "Study & campus",
            title = "Study & campus",
            banglaDesc = "ইউনিভার্সিটি ক্লাস, প্রেজেন্টেশন এবং সহপাঠীদের সাথে কথোপকথন।",
            hindiDesc = "यूनिवर्सिटी क्लास, प्रेजेंटेशन और सहपाठियों के साथ बातचीत।",
            englishDesc = "University classes, presentations, and talking with classmates.",
            iconName = "school"
        ),
        LearningGoal(
            id = "Job interviews",
            title = "Job interviews",
            banglaDesc = "চাকরির ইন্টারভিউয়ের প্রশ্নোত্তর ও নিজের অভিজ্ঞতা তুলে ধরা।",
            hindiDesc = "नौकरी के इंटरव्यू के सवाल-जवाब और अपना अनुभव बताना।",
            englishDesc = "Job interview Q&A, STAR method answers, and career confidence.",
            iconName = "work"
        ),
        LearningGoal(
            id = "Work & business",
            title = "Work & business",
            banglaDesc = "অফিসের মিটিং, ক্লায়েন্ট কল এবং প্রফেশনাল ইংলিশ।",
            hindiDesc = "ऑफिस मीटिंग, क्लाइंट कॉल और प्रोफेशनल इंग्लिश।",
            englishDesc = "Office standups, client calls, and professional communication.",
            iconName = "business"
        ),
        LearningGoal(
            id = "Travel & abroad",
            title = "Travel & abroad",
            banglaDesc = "এয়ারপোর্ট, হোটেল, রেস্তোরাঁ ও বিদেশ ভ্রমণের কথোপকথন।",
            hindiDesc = "एयरपोर्ट, होटल, रेस्तरां और विदेश यात्रा की बातचीत।",
            englishDesc = "Airport check-in, immigration, hotels, and dining abroad.",
            iconName = "flight"
        ),
        LearningGoal(
            id = "IELTS speaking",
            title = "IELTS speaking",
            banglaDesc = "আইইএলটিএস স্পিকিং পার্ট ১, ২ (Cue Card) ও পার্ট ৩ প্রস্তুতি।",
            hindiDesc = "IELTS स्पीकिंग पार्ट 1, 2 (Cue Card) और पार्ट 3 की तैयारी।",
            englishDesc = "Full IELTS Speaking Part 1, 2 Cue Card, and Part 3 Band 7+ prep.",
            iconName = "verified"
        )
    )

    val tutors = listOf(
        TutorPersona(
            id = "sarah_us",
            name = "Sarah",
            roleTitle = "Friendly Tutor",
            accentLabel = "US Accent",
            languageTag = "en-US",
            banglaBio = "হাসিমুখে সহজ ভাষায় কথা বলে, জড়তা কাটাতে সাহায্য করে এবং ভুল হলে আলতো করে শুধরে দেয়।",
            primaryColorHex = 0xFF6D28D9,
            secondaryColorHex = 0xFFDDD6FE,
            hasGlasses = false,
            hasBeard = false
        ),
        TutorPersona(
            id = "alex_uk",
            name = "Alex",
            roleTitle = "Hiring Manager",
            accentLabel = "UK British Accent",
            languageTag = "en-GB",
            banglaBio = "চশমা ও দাড়িসহ ফর্মাল লুক; চাকরির ইন্টারভিউ ও অফিস মিটিং প্র্যাকটিসের জন্য।",
            primaryColorHex = 0xFF1E40AF,
            secondaryColorHex = 0xFFDBEAFE,
            hasGlasses = true,
            hasBeard = true
        ),
        TutorPersona(
            id = "maya_au",
            name = "Maya",
            roleTitle = "Casual Best Friend",
            accentLabel = "Australian Accent",
            languageTag = "en-AU",
            banglaBio = "একদম ঘরোয়া আড্ডার মতো সিনেমা, খাবার ও শখ নিয়ে কথা বলে।",
            primaryColorHex = 0xFF047857,
            secondaryColorHex = 0xFFD1FAE5,
            hasGlasses = false,
            hasBeard = false
        ),
        TutorPersona(
            id = "james_ielts",
            name = "Prof. James",
            roleTitle = "IELTS Examiner",
            accentLabel = "UK Accent",
            languageTag = "en-GB",
            banglaBio = "পাবলিক স্পিকিং ও IELTS Band 7+ ভোকাবুলারি শেখানোর জন্য।",
            primaryColorHex = 0xFF7C2D12,
            secondaryColorHex = 0xFFFEF3C7,
            hasGlasses = true,
            hasBeard = false
        )
    )

    val scenarios = listOf(
        PracticeScenario(
            id = "meet_someone_new",
            title = "Meet someone new",
            badgeLevel = "Easy",
            category = "Everyday English",
            banglaSummary = "নতুন কারো সাথে পরিচিত হওয়া, কুশল বিনিময় ও স্মল টক প্র্যাকটিস করায়।",
            roleplayPrompt = "You are Sarah at a friendly community coffee meetup. Greet the user warmly, introduce yourself, and practice natural small talk about their hobbies and day.",
            defaultTutorId = "sarah_us",
            starterLine = "Hi there! I don't think we've met yet. I'm Sarah—what brings you here today?",
            starterHint = "Hi Sarah! Nice to meet you. I'm here to practice my English and make new friends."
        ),
        PracticeScenario(
            id = "at_a_restaurant",
            title = "At a restaurant",
            badgeLevel = "Easy",
            category = "Travel & abroad",
            banglaSummary = "রেস্তোরাঁয় খাবার অর্ডার দেওয়া, মেনু সম্পর্কে জানতে চাওয়া ও বিল চাওয়ার প্র্যাকটিস।",
            roleplayPrompt = "You are Maya working as a friendly server at an international bistro. Help the user order food, ask about dietary preferences, and handle the bill.",
            defaultTutorId = "maya_au",
            starterLine = "Welcome to Lavender Bistro! Here is the menu—can I start you off with something to drink?",
            starterHint = "Could I have a glass of sparkling water, and what is your chef's special today?"
        ),
        PracticeScenario(
            id = "campus_conversation",
            title = "Campus conversation",
            badgeLevel = "Easy",
            category = "Study & campus",
            banglaDescOrSummary(),
            roleplayPrompt = "You are a university classmate discussing an upcoming group presentation and project deadline with the user.",
            defaultTutorId = "sarah_us",
            starterLine = "Hey! Are you ready for our seminar presentation tomorrow, or should we review our slides together?",
            starterHint = "Let's go over the slides once more so we feel confident about the Q&A part."
        ),
        PracticeScenario(
            id = "job_interview",
            title = "Job interview",
            badgeLevel = "Intermediate",
            category = "Job interviews",
            banglaSummary = "হায়ারিং ম্যানেজার হিসেবে আপনার শক্তি, দুর্বলতা ও আগের কাজের অভিজ্ঞতা নিয়ে প্রশ্ন করে।",
            roleplayPrompt = "You are Alex, a senior UK Hiring Manager conducting a behavioral job interview. Ask about strengths, weaknesses, and past achievements using the STAR method.",
            defaultTutorId = "alex_uk",
            starterLine = "Good morning, thanks for joining the video interview. To begin, could you walk me through your background and key strengths?",
            starterHint = "Thank you for having me. Over the past two years, I have focused on problem-solving and team collaboration..."
        ),
        PracticeScenario(
            id = "work_meeting",
            title = "Work meeting",
            badgeLevel = "Intermediate",
            category = "Work & business",
            banglaSummary = "অফিস টিম মিটিংয়ে কাজের আপডেট দেওয়া ও পরিকল্পনা বোঝানোর প্র্যাকটিস।",
            roleplayPrompt = "You are Alex leading a Monday morning project standup meeting. Ask the user for their weekly progress update, blockers, and next milestones.",
            defaultTutorId = "alex_uk",
            starterLine = "Everyone's on the call now. Could you share a quick update on what you completed last week and your priority for today?",
            starterHint = "Last week I finalized the core module, and today my main focus is testing the release build."
        ),
        PracticeScenario(
            id = "at_the_airport",
            title = "At the airport",
            badgeLevel = "Easy",
            category = "Travel & abroad",
            banglaSummary = "এয়ারপোর্ট চেক-ইন, বোর্ডিং পাস ও ইমিগ্রেশন কথোপকথন অনুশীলন।",
            roleplayPrompt = "You are an international airport check-in and immigration officer. Ask for the user's passport, destination, purpose of travel, and luggage details.",
            defaultTutorId = "james_ielts",
            starterLine = "Good afternoon. May I see your passport and boarding pass please? What is the purpose of your visit?",
            starterHint = "Here is my passport. I am traveling for a one-week conference and sightseeing."
        ),
        PracticeScenario(
            id = "return_faulty_gadget",
            title = "Return a faulty gadget at store",
            badgeLevel = "Intermediate",
            category = "Everyday English",
            banglaSummary = "Duolingo Max Mission: দোকানে নষ্ট গ্যাজেট ফেরত দেওয়া ও রিফান্ড চাওয়ার রোলপ্লে।",
            roleplayPrompt = "Duolingo Max Roleplay Mission: You are a store manager. The user bought wireless headphones yesterday that won't charge. Ask for receipt and negotiate replacement or refund.",
            defaultTutorId = "maya_au",
            starterLine = "Hi there! How can I help you at the customer service desk today?",
            starterHint = "I bought these headphones yesterday, but the left earbud isn't charging. Could I get a replacement?",
            isDuolingoMission = true
        ),
        PracticeScenario(
            id = "see_a_doctor",
            title = "See a doctor & explain symptoms",
            badgeLevel = "Easy",
            category = "Everyday English",
            banglaSummary = "Duolingo Max Mission: ডাক্তারের কাছে শারীরিক উপসর্গ ও সমস্যার কথা পরিষ্কারভাবে বলা।",
            roleplayPrompt = "Duolingo Max Roleplay Mission: You are a caring clinic doctor. Ask the user about their symptoms, how long they have had a fever or cough, and give clear advice.",
            defaultTutorId = "sarah_us",
            starterLine = "Hello, please have a seat. What symptoms have been bothering you over the last few days?",
            starterHint = "I've had a mild fever and a sore throat since Tuesday evening.",
            isDuolingoMission = true
        ),
        PracticeScenario(
            id = "wrong_bill_restaurant",
            title = "Order & handle wrong bill at restaurant",
            badgeLevel = "Intermediate",
            category = "Travel & abroad",
            banglaSummary = "Duolingo Max Mission: রেস্তোরাঁয় ভুল বিল আসলে ভদ্রভাবে সেটি ঠিক করে নেওয়া।",
            roleplayPrompt = "Duolingo Max Roleplay Mission: You are a restaurant waiter who accidentally charged the user for two extra desserts. Respond politely when the user points out the mistake.",
            defaultTutorId = "alex_uk",
            starterLine = "Here is your check for the table. Whenever you're ready, you can tap your card.",
            starterHint = "Excuse me, I think there might be a small mistake on the bill—we didn't order the two extra desserts.",
            isDuolingoMission = true
        )
    )

    private fun banglaDescOrSummary(): String =
        "ক্যাম্পাসে সহপাঠীর সাথে ক্লাস, অ্যাসাইনমেন্ট ও প্রজেক্ট নিয়ে আলোচনা।"

    val phonemeChallenges = listOf(
        PhonemeChallenge(
            id = "th_unvoiced",
            focusSound = "/θ/ vs /t/ (Think, Three, Thank)",
            sentence = "I think three thousand things through thoroughly.",
            ipaTranscription = "/aɪ θɪŋk θriː ˈθaʊzənd θɪŋz θruː ˈθʌrəli/",
            banglaArticulationTip = "বাঙালিদের জন্য টিপস: 'Th' (/θ/) উচ্চারণের সময় জিহ্বার ডগা ওপরের ও নিচের দাঁতের মাঝখানে হালকা রেখে বাতাস বের করুন (বাংলা 'থ' এর মতো ঠোঁট বা তালুতে চাপ দেবেন না)।",
            difficulty = "Easy"
        ),
        PhonemeChallenge(
            id = "v_vs_w",
            focusSound = "V vs W (Very, Well, Voice, Water)",
            sentence = "Victor works very well with vivid wireless video.",
            ipaTranscription = "/ˈvɪktər wɜːrks ˈveri wel wɪð ˈvɪvɪd ˈwaɪərləs ˈvɪdioʊ/",
            banglaArticulationTip = "বাঙালিদের জন্য টিপস: 'V' বলার সময় ওপরের দাঁত নিচের ঠোঁটকে স্পর্শ করবে (ভোঁতা কম্পন), আর 'W' বলার সময় ঠোঁট গোল হবে (চুমু খাওয়ার মতো)—দাঁত ঠোঁটে লাগবে না।",
            difficulty = "Easy"
        ),
        PhonemeChallenge(
            id = "th_voiced",
            focusSound = "/ð/ Voiced TH (This, That, Weather, Together)",
            sentence = "Whether the weather is warm, we gather together.",
            ipaTranscription = "/ˈweðər ðə ˈweðər ɪz wɔːrm wi ˈɡæðər təˈɡeðər/",
            banglaArticulationTip = "বাঙালিদের জন্য টিপস: 'This / Weather' (/ð/) উচ্চারণে জিহ্বার ডগা দাঁতের মাঝে রেখে গলায় হালকা ভাইব্রেশন (গুঞ্জন) তৈরি করুন।",
            difficulty = "Intermediate"
        ),
        PhonemeChallenge(
            id = "silent_letters",
            focusSound = "Silent Letters (Wednesday, Subtle, Debt, Receipt)",
            sentence = "On Wednesday I had no doubt about the subtle receipt.",
            ipaTranscription = "/ɒn ˈwenzdeɪ aɪ hæd noʊ daʊt əˈbaʊt ðə ˈsʌtəl rɪˈsiːt/",
            banglaArticulationTip = "বাঙালিদের জন্য টিপস: Wednesday-তে 'd' উচ্চারণ হয় না ('ওয়েঞ্জ-ডে'), Subtle-এ 'b' উচ্চারণ হয় না ('সাটল'), এবং Receipt-এ 'p' উচ্চারণ হয় না ('রিসিট')।",
            difficulty = "Intermediate"
        ),
        PhonemeChallenge(
            id = "ed_endings",
            focusSound = "-ed Past Endings (/t/, /d/, /ɪd/)",
            sentence = "She walked home, called her friend, and waited outside.",
            ipaTranscription = "/ʃi wɔːkt hoʊm kɔːld hɜːr frend ænd ˈweɪtɪd ˌaʊtˈsaɪd/",
            banglaArticulationTip = "বাঙালিদের জন্য টিপস: Walked উচ্চারণ হবে 'ওয়াক্ট' ('ওয়াক-এড' নয়), Called হবে 'কল্ড', শুধুমাত্র t/d দিয়ে শেষ হওয়া শব্দে (Waited) '/ɪd/' যোগ হয়।",
            difficulty = "Intermediate"
        )
    )

    val patternDrills = listOf(
        PatternDrill(
            id = "used_to",
            patternHeader = "I am used to + [Verb-ing]",
            banglaMeaning = "কোনো কিছুতে অভ্যস্ত থাকা বোঝাতে (যেমন: আমি সকালে তাড়াতাড়ি উঠতে অভ্যস্ত)।",
            exampleCasual = "I'm totally used to waking up early now.",
            exampleFormal = "I am accustomed to managing tight project deadlines.",
            exampleIdiomatic = "Early mornings are second nature to me now.",
            practicePrompt = "Make a sentence with 'I am used to...' about your daily routine or work."
        ),
        PatternDrill(
            id = "meaning_to",
            patternHeader = "I have been meaning to + [Verb]",
            banglaMeaning = "অনেকদিন ধরে কিছু করার কথা ভাবছি কিন্তু করা হয়ে ওঠেনি বোঝাতে।",
            exampleCasual = "I've been meaning to call you all week!",
            exampleFormal = "I have been intending to follow up on our proposal.",
            exampleIdiomatic = "It's been on my radar to reach out to you.",
            practicePrompt = "Say something you have been meaning to do recently."
        ),
        PatternDrill(
            id = "would_rather",
            patternHeader = "I would rather + [Verb] than...",
            banglaMeaning = "একটির চেয়ে অন্যটি বেশি পছন্দ করা বোঝাতে।",
            exampleCasual = "I'd rather cook at home than eat out tonight.",
            exampleFormal = "I would prefer to finalize the report before the meeting.",
            exampleIdiomatic = "Given the choice, I'd jump at cooking at home tonight.",
            practicePrompt = "Share a preference using 'I would rather...'"
        )
    )

    val ieltsCueCards = listOf(
        IeltsCueCard(
            id = "skill_learned",
            part1Questions = listOf(
                "Do you work or are you a student?",
                "How do you usually spend your weekends?",
                "Why are you learning to speak fluent English?"
            ),
            cueCardTopic = "Describe a useful skill you learned recently.",
            bulletPoints = listOf(
                "What the skill is and when you learned it",
                "How you practiced it step by step",
                "What difficulties you faced at first",
                "And explain why this skill is important for your future"
            ),
            part3Questions = listOf(
                "Which practical skills should schools teach more than theoretical subjects?",
                "How has artificial intelligence changed the way adults learn communication skills?"
            )
        ),
        IeltsCueCard(
            id = "memorable_journey",
            part1Questions = listOf(
                "Do you enjoy traveling to new places?",
                "What kind of transport do you prefer on long trips?",
                "Do you prefer traveling alone or with friends?"
            ),
            cueCardTopic = "Describe a memorable journey you took that taught you something new.",
            bulletPoints = listOf(
                "Where you went and who accompanied you",
                "What happened during the journey",
                "What unexpected challenge or surprise occurred",
                "And explain why you still remember this trip vividly"
            ),
            part3Questions = listOf(
                "Why do some people prefer exploring local places rather than traveling abroad?",
                "In what ways does international travel broaden a person's mindset?"
            )
        )
    )
}
