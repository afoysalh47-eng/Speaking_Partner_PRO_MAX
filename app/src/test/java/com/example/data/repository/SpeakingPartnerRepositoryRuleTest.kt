package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeakingPartnerRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveAndObserveProfile_authenticatedOwner_succeeds() = runBlocking {
        val aliceUid = signInTestUser("alice@speakingpartner.test")
        val repository = SpeakingPartnerRepository(firestore)

        val saveResult = withTimeout(5000L) {
            repository.saveUserProfile(
                UserProfile(
                    userId = aliceUid,
                    displayName = "Alice Rahman",
                    englishLevel = "Intermediate",
                    helpLanguage = "Bangla",
                    onboardingCompleted = true
                )
            )
        }
        assertTrue("Expected saveUserProfile to succeed", saveResult.isSuccess)

        val emittedProfile = withTimeout(3000L) {
            repository.observeUserProfile(aliceUid).first { it != null && it.displayName == "Alice Rahman" }
        }
        assertNotNull(emittedProfile)
        assertEquals("Intermediate", emittedProfile?.englishLevel)
    }

    @Test
    fun addAndObserveVocabEntry_authenticatedOwner_succeeds() = runBlocking {
        val aliceUid = signInTestUser("alice@speakingpartner.test")
        val repository = SpeakingPartnerRepository(firestore)

        val createResult = withTimeout(5000L) {
            repository.addVocabEntry(
                originalSentence = "I could not come to office yesterday",
                casualTone = "Couldn't make it to work yesterday.",
                formalTone = "I was unable to attend the office yesterday.",
                idiomaticTone = "I had to call in sick yesterday.",
                banglaMeaning = "আমি কালকে অফিসে যেতে পারিনি",
                vocabHighlight = "call in sick"
            )
        }
        assertTrue(createResult.isSuccess)
        val vocabId = createResult.getOrThrow()

        val vocabList = withTimeout(3000L) {
            repository.observeVocabBank(aliceUid).first { list -> list.any { it.id == vocabId } }
        }
        assertTrue(vocabList.any { it.id == vocabId })
    }

    @Test
    fun crossUserProfileAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser("alice@speakingpartner.test")
        val aliceRepo = SpeakingPartnerRepository(firestore)
        withTimeout(5000L) {
            aliceRepo.saveUserProfile(UserProfile(userId = aliceUid, displayName = "Alice"))
        }

        signInTestUser("bob@speakingpartner.test")
        val bobRepo = SpeakingPartnerRepository(firestore)
        val crossReadResult = withTimeout(5000L) {
            bobRepo.getUserProfileOnce(aliceUid)
        }
        assertTrue(crossReadResult.isFailure)
        val firestoreEx = crossReadResult.exceptionOrNull()?.unwrapFirestoreException()
        assertNotNull(firestoreEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }

    @Test
    fun unauthenticatedObserveVocab_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repository = SpeakingPartnerRepository(firestore)

        var caught: Throwable? = null
        try {
            withTimeout(3000L) {
                repository.observeVocabBank("unauth_user").first()
            }
        } catch (e: Throwable) {
            caught = e
        }
        assertNotNull(caught)
        val firestoreEx = caught?.unwrapFirestoreException()
        assertNotNull(firestoreEx)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx?.code)
    }
}
