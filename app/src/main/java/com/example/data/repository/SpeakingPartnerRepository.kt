package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CustomTopic
import com.example.data.model.FamiliarPerson
import com.example.data.model.UserProfile
import com.example.data.model.VocabEntry
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class SpeakingPartnerRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth get() = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw FirebaseFirestoreException(
                "User must be signed in with Google before accessing Firestore.",
                FirebaseFirestoreException.Code.PERMISSION_DENIED
            )
    }

    fun observeUserProfile(userId: String = requireUserId()): Flow<UserProfile?> = flow {
        val path = "users/$userId"
        emitAll(
            db.collection("users").document(userId)
                .snapshots()
                .map { snapshot ->
                    if (snapshot.exists()) {
                        snapshot.toObject(
                            UserProfile::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    } else {
                        null
                    }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    handleFirestoreError(firestoreError ?: error, OperationType.GET, path)
                    throw error
                }
        )
    }

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        val uid = requireUserId()
        val path = "users/$uid"
        val docRef = db.collection("users").document(uid)
        return try {
            val existingSnap = docRef.get().await()
            if (!existingSnap.exists()) {
                val createPayload = profile.copy(userId = uid).toFirestoreMap(isCreate = true)
                docRef.set(createPayload).await()
            } else {
                val updatePayload = mapOf(
                    "displayName" to profile.displayName.take(80).ifBlank { "Learner" },
                    "englishLevel" to profile.englishLevel.take(30).ifBlank { "Beginner" },
                    "helpLanguage" to profile.helpLanguage.take(30).ifBlank { "Bangla" },
                    "selectedGoals" to profile.selectedGoals.take(10).map { it.take(50) },
                    "streakDays" to profile.streakDays.coerceIn(0L, 10000L),
                    "minutesToday" to profile.minutesToday.coerceIn(0L, 1440L),
                    "fluencyScore" to profile.fluencyScore.coerceIn(0L, 100L),
                    "dailyChallengeMinutes" to profile.dailyChallengeMinutes.coerceIn(0L, 1440L),
                    "onboardingCompleted" to profile.onboardingCompleted,
                    "latestVersionCode" to profile.latestVersionCode.coerceIn(1L, 1000L),
                    "homeBannerText" to profile.homeBannerText.take(200),
                    "backupGeminiKey1" to profile.backupGeminiKey1.take(200),
                    "backupGeminiKey2" to profile.backupGeminiKey2.take(200),
                    "fishAudioKey1" to profile.fishAudioKey1.take(200),
                    "fishAudioKey2" to profile.fishAudioKey2.take(200),
                    "elevenLabsKey" to profile.elevenLabsKey.take(200),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.update(updatePayload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun getUserProfileOnce(targetUserId: String = requireUserId()): Result<UserProfile?> {
        val path = "users/$targetUserId"
        return try {
            val snap = db.collection("users").document(targetUserId).get().await()
            val profile = if (snap.exists()) {
                snap.toObject(UserProfile::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
            } else {
                null
            }
            Result.success(profile)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, path)
            Result.failure(e)
        }
    }

    fun observeVocabBank(userId: String = requireUserId()): Flow<List<VocabEntry>> = flow {
        val path = "users/$userId/vocab_bank"
        emitAll(
            db.collection("users").document(userId).collection("vocab_bank")
                .whereEqualTo("userId", userId)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        VocabEntry::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt ?: com.google.firebase.Timestamp(0, 0) }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    handleFirestoreError(firestoreError ?: error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addVocabEntry(
        originalSentence: String,
        casualTone: String,
        formalTone: String,
        idiomaticTone: String,
        banglaMeaning: String,
        vocabHighlight: String
    ): Result<String> {
        val uid = requireUserId()
        val path = "users/$uid/vocab_bank"
        return try {
            val payload = mapOf(
                "userId" to uid,
                "originalSentence" to originalSentence.take(500).ifBlank { "Hello" },
                "casualTone" to casualTone.take(500).ifBlank { originalSentence.take(500) },
                "formalTone" to formalTone.take(500).ifBlank { originalSentence.take(500) },
                "idiomaticTone" to idiomaticTone.take(500).ifBlank { originalSentence.take(500) },
                "banglaMeaning" to banglaMeaning.take(500).ifBlank { "সহজ ইংরেজি প্রকাশ" },
                "vocabHighlight" to vocabHighlight.take(200).ifBlank { "Fluency phrase" },
                "masteryLevel" to 1L,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            val docRef = db.collection("users").document(uid).collection("vocab_bank").add(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun updateVocabMastery(vocabId: String, masteryLevel: Long): Result<Unit> {
        val uid = requireUserId()
        val path = "users/$uid/vocab_bank/$vocabId"
        return try {
            db.collection("users").document(uid)
                .collection("vocab_bank").document(vocabId)
                .update(
                    mapOf(
                        "masteryLevel" to masteryLevel.coerceIn(0L, 5L),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    fun observeFamiliarPeople(userId: String = requireUserId()): Flow<List<FamiliarPerson>> = flow {
        val path = "users/$userId/familiar_people"
        emitAll(
            db.collection("users").document(userId).collection("familiar_people")
                .whereEqualTo("userId", userId)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        FamiliarPerson::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt ?: com.google.firebase.Timestamp(0, 0) }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    handleFirestoreError(firestoreError ?: error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addFamiliarPerson(
        name: String,
        relationship: String,
        accent: String,
        mouthYRatio: Long,
        mouthWidthRatio: Long,
        voiceEngine: String,
        photoUri: String?,
        voiceSampleNote: String?
    ): Result<String> {
        val uid = requireUserId()
        val path = "users/$uid/familiar_people"
        return try {
            val baseMap = mutableMapOf<String, Any?>(
                "userId" to uid,
                "name" to name.take(80).ifBlank { "Familiar Partner" },
                "relationship" to relationship.take(60).ifBlank { "Friend" },
                "accent" to accent.take(30).ifBlank { "US" },
                "mouthYRatio" to mouthYRatio.coerceIn(0L, 100L),
                "mouthWidthRatio" to mouthWidthRatio.coerceIn(10L, 90L),
                "voiceEngine" to voiceEngine.take(40).ifBlank { "HuggingFace" },
                "photoUri" to photoUri?.take(500),
                "voiceSampleNote" to voiceSampleNote?.take(200),
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            ).filterValues { it != null }.mapValues { it.value!! }

            val docRef = db.collection("users").document(uid).collection("familiar_people").add(baseMap).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteFamiliarPerson(personId: String): Result<Unit> {
        val uid = requireUserId()
        val path = "users/$uid/familiar_people/$personId"
        return try {
            db.collection("users").document(uid).collection("familiar_people").document(personId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    fun observeCustomTopics(userId: String = requireUserId()): Flow<List<CustomTopic>> = flow {
        val path = "users/$userId/custom_topics"
        emitAll(
            db.collection("users").document(userId).collection("custom_topics")
                .whereEqualTo("userId", userId)
                .snapshots()
                .map { snapshot ->
                    snapshot.toObjects(
                        CustomTopic::class.java,
                        DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                    ).sortedByDescending { it.createdAt ?: com.google.firebase.Timestamp(0, 0) }
                }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    handleFirestoreError(firestoreError ?: error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addCustomTopic(
        title: String,
        badgeLevel: String,
        category: String,
        roleplayPrompt: String
    ): Result<String> {
        val uid = requireUserId()
        val path = "users/$uid/custom_topics"
        return try {
            val payload = mapOf(
                "userId" to uid,
                "title" to title.take(120).ifBlank { "Custom Practice" },
                "badgeLevel" to badgeLevel.take(30).ifBlank { "Easy" },
                "category" to category.take(60).ifBlank { "Everyday English" },
                "roleplayPrompt" to roleplayPrompt.take(600).ifBlank { "Practice natural English conversation." },
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            val docRef = db.collection("users").document(uid).collection("custom_topics").add(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteCustomTopic(topicId: String): Result<Unit> {
        val uid = requireUserId()
        val path = "users/$uid/custom_topics/$topicId"
        return try {
            db.collection("users").document(uid).collection("custom_topics").document(topicId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }
}
