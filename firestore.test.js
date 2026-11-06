const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write user profiles", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user can create and read own profile, but fails shadow field injection", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice Rahman",
      englishLevel: "Beginner",
      helpLanguage: "Bangla",
      selectedGoals: ["Everyday English", "IELTS speaking"],
      streakDays: 7,
      minutesToday: 12,
      fluencyScore: 78,
      dailyChallengeMinutes: 3,
      onboardingCompleted: true,
      latestVersionCode: 1,
      homeBannerText: "Welcome to AI Speaking Partner PRO MAX",
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());

  // Shadow update with ghost field must fail
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).update({
      isAdmin: true,
    })
  );
});

test("Cross-user isolation: Bob cannot read or modify Alice's profile or vocab bank", async () => {
  const now = new Date();
  await testEnv.withSecurityRulesDisabled(async (context) => {
    const db = context.firestore();
    await db.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      englishLevel: "Beginner",
      helpLanguage: "Bangla",
      streakDays: 1,
      minutesToday: 5,
      fluencyScore: 70,
      dailyChallengeMinutes: 2,
      onboardingCompleted: true,
      createdAt: now,
    });
    await db.collection("users").doc(ALICE_UID).collection("vocab_bank").doc("v1").set({
      userId: ALICE_UID,
      originalSentence: "I want to talk",
      casualTone: "Let's chat",
      formalTone: "I would like to discuss",
      idiomaticTone: "Let's catch up",
      banglaMeaning: "আমি কথা বলতে চাই",
      vocabHighlight: "catch up",
      masteryLevel: 1,
      createdAt: now,
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("vocab_bank").where("userId", "==", ALICE_UID).get()
  );

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("vocab_bank").where("userId", "==", ALICE_UID).get()
  );
});

test("Authenticated user can create familiar person and custom topic with valid schema", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("familiar_people").doc("p1").set({
      userId: ALICE_UID,
      name: "Rahim Sir",
      relationship: "University Professor",
      accent: "UK",
      mouthYRatio: 64,
      mouthWidthRatio: 38,
      voiceEngine: "HuggingFace",
      createdAt: now,
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("custom_topics").doc("t1").set({
      userId: ALICE_UID,
      title: "Salary Negotiation",
      badgeLevel: "Intermediate",
      category: "Work & business",
      roleplayPrompt: "Act as HR director negotiating annual raise.",
      createdAt: now,
    })
  );
});
