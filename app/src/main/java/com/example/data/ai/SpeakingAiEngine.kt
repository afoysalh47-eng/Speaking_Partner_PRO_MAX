package com.example.data.ai

import android.util.Log
import com.example.data.model.ChatTurnMessage
import com.example.data.model.IeltsBandEvaluation
import com.example.data.model.PracticeScenario
import com.example.data.model.StructuredAiTurnResponse
import com.example.data.model.TutorPersona
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val TAG = "SpeakingAiEngine"

class SpeakingAiEngine {

    private val turnResponseSchema = Schema.obj(
        properties = mapOf(
            "reply" to Schema.string(description = "The tutor's natural spoken response continuing the roleplay or coaching"),
            "emotion" to Schema.enumeration(
                values = listOf("happy", "encouraging", "curious", "proud"),
                description = "The tutor's facial expression during this reply"
            ),
            "betterWay" to Schema.string(description = "Corrected or more natural English version of what the user said"),
            "casualTone" to Schema.string(description = "How to express the user's message in a friendly Casual tone"),
            "formalTone" to Schema.string(description = "How to express the user's message in a Business Formal tone"),
            "idiomaticTone" to Schema.string(description = "How to express the user's message using Native Idiomatic English"),
            "banglaTip" to Schema.string(description = "1-line easy grammar or phrasing explanation in Bangla (বাংলা)"),
            "vocabWord" to Schema.string(description = "One useful English phrase or vocabulary highlight with short meaning"),
            "hintForUser" to Schema.string(description = "A ready-to-say English response hint for the user's next turn")
        )
    )

    private val ieltsEvaluationSchema = Schema.obj(
        properties = mapOf(
            "overallBand" to Schema.double(description = "Estimated overall IELTS Speaking Band Score between 4.5 and 8.5"),
            "fluencyBand" to Schema.double(description = "Fluency and Coherence band score (4.5 to 8.5)"),
            "lexicalBand" to Schema.double(description = "Lexical Resource band score (4.5 to 8.5)"),
            "grammarBand" to Schema.double(description = "Grammatical Range and Accuracy band score (4.5 to 8.5)"),
            "pronunciationBand" to Schema.double(description = "Pronunciation band score (4.5 to 8.5)"),
            "examinerFeedback" to Schema.string(description = "Concise official examiner feedback in English"),
            "banglaImprovementPlan" to Schema.string(description = "Actionable tips in Bangla to raise the band score by +1.0")
        )
    )

    private val turnModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = turnResponseSchema
            }
        )
    }

    private val ieltsModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = ieltsEvaluationSchema
            }
        )
    }

    private val plainModel by lazy {
        Firebase.ai.generativeModel(modelName = "gemini-3.8-flash")
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(25, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .build()
    }

    private var activeSlotLabel: String = "Primary: Firebase AI Logic (gemini-3.8-flash)"

    fun getActiveKeySlotLabel(): String = activeSlotLabel

    suspend fun generateConversationTurn(
        userSpeech: String,
        wasBanglaInput: Boolean,
        scenario: PracticeScenario,
        tutor: TutorPersona,
        englishLevel: String,
        helpLanguage: String,
        slidingHistory: List<ChatTurnMessage>,
        backupKeys: List<String> = emptyList()
    ): Result<StructuredAiTurnResponse> {
        val recentHistoryText = slidingHistory.takeLast(10).joinToString("\n") { msg ->
            if (msg.isUser) "User: ${msg.text}" else "${tutor.name}: ${msg.text}"
        }

        val modeInstruction = if (wasBanglaInput) {
            """
            BILINGUAL BANGLA-TO-ENGLISH MODE ACTIVE:
            The user just spoke directly in Bangla (or mixed Bangla-English): "$userSpeech".
            1. In 'betterWay', provide the most natural English translation of their Bangla sentence.
            2. In 'reply', warmly tell the user how to say that in English and invite them to repeat it aloud once, then continue the roleplay naturally.
            3. Provide 'casualTone', 'formalTone', and 'idiomaticTone' variations of that English sentence.
            4. In 'banglaTip', explain the sentence structure in 1 simple Bangla line.
            """.trimIndent()
        } else {
            """
            ENGLISH SPEAKING PRACTICE MODE:
            The user said: "$userSpeech".
            1. Reply naturally in character as ${tutor.name} (${tutor.roleTitle}, ${tutor.accentLabel}) for scenario "${scenario.title}" (${scenario.roleplayPrompt}). Keep 'reply' conversational (1-3 sentences) and end with an engaging follow-up question.
            2. In 'betterWay', provide a polished, grammatically natural version of the user's sentence.
            3. Rewrite the user's thought into 3 distinct styles: 'casualTone', 'formalTone' (Business Formal), and 'idiomaticTone' (Native Idiomatic).
            4. In 'banglaTip', provide a 1-line helpful grammar/phrasing explanation in Bangla (বাংলা) (and if helpLanguage is $helpLanguage, keep it clear for a $englishLevel learner).
            5. In 'vocabWord', highlight 1 strong phrase with its Bangla meaning.
            6. In 'hintForUser', give a natural 1-sentence example the user can say next.
            """.trimIndent()
        }

        val fullPrompt = """
            Tutor Persona: ${tutor.name} (${tutor.roleTitle}, ${tutor.accentLabel})
            Scenario: ${scenario.title} — ${scenario.roleplayPrompt}
            User Level: $englishLevel | Preferred Help Language: $helpLanguage
            Recent Conversation Sliding Window:
            $recentHistoryText

            $modeInstruction
        """.trimIndent()

        // 1. Primary Slot: Firebase AI Logic (gemini-3.8-flash)
        try {
            activeSlotLabel = "Slot #1: Firebase AI Logic (gemini-3.8-flash)"
            val response = withTimeout(60_000L) {
                turnModel.generateContent(fullPrompt)
            }
            val rawJson = response.text ?: throw IllegalStateException("Model returned empty JSON")
            return Result.success(parseTurnJson(rawJson))
        } catch (e: CancellationException) {
            if (e !is TimeoutCancellationException) throw e
            Log.e(TAG, "Primary Firebase AI Logic timed out, checking failover slots", e)
        } catch (e: Exception) {
            Log.e(TAG, "Primary Firebase AI Logic error, checking failover slots", e)
            // Circular Failover Loop to user-configured backup keys if provided
            val validBackupKeys = backupKeys.map { it.trim() }.filter { it.isNotEmpty() }
            for ((index, key) in validBackupKeys.withIndex()) {
                try {
                    activeSlotLabel = "Failover Key #${index + 2} (Circular Auto-Switch)"
                    val fallbackResult = withTimeout(45_000L) {
                        callFallbackGeminiKey(key, fullPrompt)
                    }
                    return Result.success(fallbackResult)
                } catch (ce: CancellationException) {
                    if (ce !is TimeoutCancellationException) throw ce
                } catch (fe: Exception) {
                    Log.e(TAG, "Failover key #${index + 2} failed", fe)
                }
            }
            return Result.failure(e)
        }

        return Result.failure(IllegalStateException("Generation timed out after 60 seconds. Please try again."))
    }

    suspend fun evaluateIeltsSpeaking(
        partNumber: Int,
        topicTitle: String,
        userTranscripts: List<String>
    ): Result<IeltsBandEvaluation> {
        val combinedSpeech = userTranscripts.joinToString("\n- ")
        val prompt = """
            You are Prof. James, an official IELTS Speaking Examiner.
            Evaluate the candidate's spoken responses for IELTS Speaking Part $partNumber on topic: "$topicTitle".
            Candidate's spoken transcript:
            - $combinedSpeech

            Provide realistic IELTS Band Scores (between 4.5 and 8.5 in 0.5 increments) for overallBand, fluencyBand, lexicalBand, grammarBand, and pronunciationBand, plus concise examinerFeedback in English and a banglaImprovementPlan in Bangla (বাংলা).
        """.trimIndent()

        return try {
            val response = withTimeout(60_000L) {
                ieltsModel.generateContent(prompt)
            }
            val json = JSONObject(response.text ?: throw IllegalStateException("Empty IELTS evaluation"))
            Result.success(
                IeltsBandEvaluation(
                    overallBand = json.optDouble("overallBand", 6.5),
                    fluencyBand = json.optDouble("fluencyBand", 6.5),
                    lexicalBand = json.optDouble("lexicalBand", 6.5),
                    grammarBand = json.optDouble("grammarBand", 6.0),
                    pronunciationBand = json.optDouble("pronunciationBand", 6.5),
                    examinerFeedback = json.optString("examinerFeedback", "Good effort maintaining coherence."),
                    banglaImprovementPlan = json.optString(
                        "banglaImprovementPlan",
                        "বাক্যের মাঝে বিরতি কমিয়ে লিঙ্কিং ওয়ার্ড (However, Furthermore) ব্যবহার বাড়ালে স্কোর আরও বাড়বে।"
                    )
                )
            )
        } catch (e: CancellationException) {
            if (e !is TimeoutCancellationException) throw e
            Result.failure(IllegalStateException("IELTS evaluation timed out after 60 seconds."))
        } catch (e: Exception) {
            Log.e(TAG, "IELTS evaluation failed", e)
            Result.failure(e)
        }
    }

    suspend fun generateStarCvCoaching(
        cvOrJdText: String,
        userAnswer: String
    ): Result<String> {
        val prompt = """
            You are Alex, a UK Hiring Manager and Career Coach in the Praktika AI STAR CV Lab.
            Job Description / CV Context:
            $cvOrJdText

            Candidate's Response:
            "$userAnswer"

            Rewrite the candidate's response using the STAR Method (Situation, Task, Action, Result) in crisp professional English, and add a 2-line Bangla coaching tip at the end.
        """.trimIndent()

        return try {
            val response = withTimeout(60_000L) {
                plainModel.generateContent(prompt)
            }
            val text = response.text
            if (!text.isNullOrBlank()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("Model returned empty coaching text."))
            }
        } catch (e: CancellationException) {
            if (e !is TimeoutCancellationException) throw e
            Result.failure(IllegalStateException("STAR coaching timed out."))
        } catch (e: Exception) {
            Log.e(TAG, "STAR CV coaching failed", e)
            Result.failure(e)
        }
    }

    private fun parseTurnJson(rawJson: String): StructuredAiTurnResponse {
        val json = JSONObject(rawJson)
        return StructuredAiTurnResponse(
            reply = json.optString("reply", "That sounds great! Could you tell me a little more about that?"),
            emotion = json.optString("emotion", "encouraging"),
            betterWay = json.optString("betterWay", ""),
            casualTone = json.optString("casualTone", ""),
            formalTone = json.optString("formalTone", ""),
            idiomaticTone = json.optString("idiomaticTone", ""),
            banglaTip = json.optString("banglaTip", "স্বাভাবিকভাবে কথা চালিয়ে যান—আপনার বাক্য গঠন সুন্দর হচ্ছে!"),
            vocabWord = json.optString("vocabWord", ""),
            hintForUser = json.optString("hintForUser", "Sure, let me share an example from my daily life.")
        )
    }

    private suspend fun callFallbackGeminiKey(
        apiKey: String,
        prompt: String
    ): StructuredAiTurnResponse = withContext(Dispatchers.IO) {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent?key=$apiKey"
        val bodyJson = JSONObject().apply {
            put(
                "contents",
                JSONArray().put(
                    JSONObject().put(
                        "parts",
                        JSONArray().put(
                            JSONObject().put(
                                "text",
                                "$prompt\nRespond strictly in raw JSON with keys: reply, emotion, betterWay, casualTone, formalTone, idiomaticTone, banglaTip, vocabWord, hintForUser."
                            )
                        )
                    )
                )
            )
            put("generationConfig", JSONObject().put("responseMimeType", "application/json"))
        }
        val req = Request.Builder()
            .url(url)
            .post(bodyJson.toString().toRequestBody("application/json".toMediaType()))
            .build()
        httpClient.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) {
                throw IllegalStateException("Fallback key returned HTTP ${resp.code}")
            }
            val respStr = resp.body?.string().orEmpty()
            val root = JSONObject(respStr)
            val text = root.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
            parseTurnJson(text)
        }
    }
}
