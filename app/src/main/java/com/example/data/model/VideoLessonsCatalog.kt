package com.example.data.model

data class VideoLesson(
    val id: String,
    val lessonNumber: Int,
    val title: String,
    val category: String,
    val levelBadge: String, // "Beginner", "Intermediate", "Advanced"
    val durationLabel: String,
    val tutorId: String,
    val banglaTitle: String,
    val videoScriptEnglish: String,
    val banglaSummary: String,
    val keyPatternOrVocab: String,
    val shadowPracticeSentence: String,
    val roleplayPrompt: String
) {
    val level: String
        get() = levelBadge
}

object VideoLessonsCatalog {

    val categories = listOf(
        "All (108)",
        "Everyday English",
        "Job interviews",
        "Work & business",
        "Travel & abroad",
        "IELTS speaking",
        "Study & Grammar"
    )

    val allLessons: List<VideoLesson> by lazy {
        val rawSpecs = listOf(
            // --- 1. Everyday English (1..18) ---
            LessonSeed(
                "Everyday English", "Beginner", "04:15", "sarah_us",
                "Natural Greetings & First Impressions",
                "স্বাভাবিকভাবে কুশল বিনিময় ও প্রথম পরিচয়",
                "Instead of repeating 'I am fine, thank you' every time, native speakers use warm, natural openers like 'I'm doing great, thanks for asking! How's your day going so far?'",
                "How's your day going so far? / Pretty good, can't complain!",
                "I'm doing great, thanks for asking! How's your day going so far?"
            ),
            LessonSeed(
                "Everyday English", "Beginner", "05:10", "maya_au",
                "Effortless Small Talk with Strangers",
                "অপরিচিত কারো সাথে জড়তা ছাড়া স্মল টক",
                "Small talk is all about shared context. Comment on the venue, the weather, or the event, and follow up with an open question.",
                "What brings you here today? / Have you been here before?",
                "It's such a lively event today—what brings you here?"
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:45", "sarah_us",
                "Talking About Your Daily Routine",
                "নিজের দৈনন্দিন রুটিন গুছিয়ে বলা",
                "Use time connectors like 'First thing in the morning', 'Around noon', and 'In the evening' to make your daily story flow smoothly.",
                "First thing in the morning, I usually... / To wind down at night...",
                "First thing in the morning, I like to plan my top three priorities."
            ),
            LessonSeed(
                "Everyday English", "Beginner", "05:30", "maya_au",
                "Ordering Coffee & Cafe Customizations",
                "ক্যাফেতে কফি ও খাবার অর্ডার করার সহজ ইংরেজি",
                "When ordering at a café, use 'Could I get...' or 'I'll have...' instead of 'Give me'. It sounds polite and effortless.",
                "Could I get an iced latte with oat milk, please?",
                "Could I get a medium cappuccino for here, please?"
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:50", "sarah_us",
                "Shopping for Clothes & Asking for Sizes",
                "শপিং মলে পোশাকের সাইজ ও ট্রায়াল রুম খোঁজা",
                "In a store, ask 'Do you have this in a medium?' or 'Where are the fitting rooms?' to shop with total confidence.",
                "Do you have this in a different size? / Can I try this on?",
                "Excuse me, do you have this jacket in a medium, and can I try it on?"
            ),
            LessonSeed(
                "Everyday English", "Beginner", "05:20", "sarah_us",
                "Explaining Symptoms at a Doctor's Clinic",
                "ডাক্তারের কাছে শারীরিক সমস্যা পরিষ্কারভাবে বলা",
                "Use 'I've been having...' or 'I've had a...' to describe symptoms that started in the past and are still bothering you now.",
                "I've been having a dull headache since yesterday morning.",
                "I've had a sore throat and mild fever for the past two days."
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:40", "maya_au",
                "Making Polite Phone Calls & Leaving Messages",
                "ফোনে ভদ্রভাবে কথা বলা এবং মেসেজ রাখা",
                "On phone calls, introduce yourself clearly: 'Hi, this is Rahim calling from...' and ask 'Is now a good time to talk?'",
                "Is now a good time to chat for two minutes?",
                "Hi, this is Rahim calling—is now a good time for a quick chat?"
            ),
            LessonSeed(
                "Everyday English", "Beginner", "05:05", "sarah_us",
                "Asking for & Giving Street Directions",
                "রাস্তার ঠিকানা বা দিকনির্দেশনা চাওয়া ও দেওয়া",
                "Use landmarks and simple action verbs: 'Go straight for two blocks', 'Take a left at the traffic light', 'It's right across from the bank.'",
                "It's just around the corner / Right across from...",
                "Go straight for two blocks and you'll see the station on your right."
            ),
            LessonSeed(
                "Everyday English", "Intermediate", "06:00", "maya_au",
                "Expressing Your Opinion Without Sounding Rude",
                "ভদ্রভাবে নিজের মতামত প্রকাশ করা",
                "Soften strong opinions using phrases like 'From my perspective...', 'The way I see it...', or 'I tend to think that...'",
                "The way I see it... / I see your point, however...",
                "From my perspective, focusing on practical speaking makes the biggest difference."
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:35", "sarah_us",
                "Talking About Hobbies, Movies & Music",
                "নিজের শখ, প্রিয় সিনেমা ও গান নিয়ে আড্ডা",
                "Instead of just saying 'I like movies', say 'I'm really into sci-fi thrillers' or 'I'm a huge fan of acoustic music.'",
                "I'm really into... / In my free time, I love...",
                "In my free time, I'm really into watching documentaries and playing badminton."
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:55", "sarah_us",
                "Making Polite Requests & Asking for Favors",
                "ভদ্রভাবে কারো কাছে সাহায্য বা অনুরোধ করা",
                "Use 'Would you mind + Verb-ing' or 'Could you possibly...' when asking someone for a favor.",
                "Would you mind sending me the notes? / Could you do me a quick favor?",
                "Would you mind looking over this document when you have a free moment?"
            ),
            LessonSeed(
                "Everyday English", "Intermediate", "05:15", "maya_au",
                "Apologizing Naturally & Making Things Right",
                "স্বাভাবিকভাবে দুঃখ প্রকাশ করা ও ভুল শুধরে নেওয়া",
                "Match your apology to the situation: 'My bad!' for casual slips, or 'I sincerely apologize for the mix-up' in formal settings.",
                "Sorry to keep you waiting / Let me make it up to you.",
                "I'm really sorry to keep you waiting—the traffic was completely backed up."
            ),
            LessonSeed(
                "Everyday English", "Intermediate", "06:10", "maya_au",
                "Telling Engaging Personal Stories",
                "আড্ডায় আকর্ষণীয়ভাবে যেকোনো ঘটনা বা গল্প বলা",
                "Great storytellers set the scene quickly ('You won't believe what happened yesterday...'), build tension, and end with a takeaway.",
                "You won't believe what happened... / Long story short...",
                "Long story short, we caught the last train with just thirty seconds to spare!"
            ),
            LessonSeed(
                "Everyday English", "Intermediate", "05:40", "sarah_us",
                "Explaining Technical or Household Problems",
                "বাসা বা যেকোনো যান্ত্রিক সমস্যার কথা গুছিয়ে বলা",
                "Use 'keeps + Verb-ing' or 'won't + Verb' to describe things that aren't working: 'The Wi-Fi keeps dropping' or 'The door won't lock.'",
                "My phone battery keeps draining / The AC isn't cooling properly.",
                "The internet connection keeps dropping every ten minutes since this morning."
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:30", "maya_au",
                "Discussing Weekend & Holiday Plans",
                "সাপ্তাহিক ছুটি ও ভ্রমণের পরিকল্পনা নিয়ে কথা বলা",
                "Use 'I'm thinking of...' for flexible plans and 'I'm planning to...' when you've already decided.",
                "I'm thinking of catching up on sleep / We're planning a quick getaway.",
                "This weekend I'm thinking of visiting my family and relaxing at home."
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:50", "maya_au",
                "Inviting Friends & Accepting or Declining Politely",
                "বন্ধুদের আমন্ত্রণ জানানো এবং ভদ্রভাবে হ্যাঁ বা না বলা",
                "If you need to decline an invitation, use 'I'd love to, but I have a prior commitment—can we take a rain check?'",
                "Are you free this Friday? / Can I take a rain check?",
                "I'd love to join you for dinner, but I have a deadline tonight—can we do tomorrow?"
            ),
            LessonSeed(
                "Everyday English", "Beginner", "04:25", "sarah_us",
                "Giving & Receiving Genuine Compliments",
                "সুন্দরভাবে প্রশংসা করা এবং প্রশংসার উত্তর দেওয়া",
                "Go beyond 'Nice shirt'—compliment effort or impact: 'You did an amazing job presenting that idea!'",
                "That means a lot, thank you! / I really appreciate you noticing.",
                "Thank you so much, that really means a lot coming from you!"
            ),
            LessonSeed(
                "Everyday English", "Intermediate", "05:45", "sarah_us",
                "Clearing Up Misunderstandings Calmly",
                "ভুল বোঝাবুঝি হলে শান্ত ও সুন্দরভাবে সমাধান করা",
                "Never blame the listener. Say 'Perhaps I didn't explain that clearly—what I meant was...' to keep the conversation positive.",
                "What I meant to say was... / Let's make sure we're on the same page.",
                "I think we might have had a small mix-up—let me clarify what I meant."
            ),

            // --- 2. Job Interviews & Career (19..36) ---
            LessonSeed(
                "Job interviews", "Beginner", "06:20", "alex_uk",
                "Winning 'Tell Me About Yourself' Formula",
                "ইন্টারভিউয়ের শুরুতে নিজের পরিচয় দেওয়ার সেরা ফর্মুলা",
                "Use the Present–Past–Future formula: 1) Your current role/studies, 2) Key past achievements, 3) Why you are excited about this position.",
                "Currently I work as... / Previously I built... / Now I'm eager to...",
                "Currently I specialize in mobile development, and I'm excited to bring my problem-solving skills to your team."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "06:15", "alex_uk",
                "Answering 'What Are Your Greatest Strengths?'",
                "নিজের শক্তি বা দক্ষতার কথা প্রমাণসহ তুলে ধরা",
                "Don't just list adjectives. Pick one core strength and back it up immediately with a real metric or workplace story.",
                "One of my core strengths is... / For instance, in my last project...",
                "One of my strongest skills is adaptability—in my last project I learned a new framework in just one week."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "06:30", "alex_uk",
                "Turning Weaknesses into Growth Stories",
                "নিজের দুর্বলতা সম্পর্কিত প্রশ্নের স্মার্ট উত্তর",
                "Share a genuine, non-fatal skill you used to struggle with, and focus 70% of your answer on the system you built to overcome it.",
                "I used to find it challenging to..., so I started...",
                "I used to say yes to too many tasks at once, so now I use a priority matrix to manage expectations clearly."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "07:00", "alex_uk",
                "Mastering the STAR Method: Situation & Task",
                "STAR মেথডে যেকোনো বিহেভিওরাল প্রশ্নের সূচনা",
                "When an interviewer asks 'Tell me about a time...', spend 20% of your time setting a crisp Situation and Task so they understand the stakes.",
                "In my previous role at..., our team was tasked with...",
                "During our Q3 product launch, our team was tasked with reducing app crash rates by fifty percent."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "07:10", "alex_uk",
                "Mastering the STAR Method: Action & Measurable Result",
                "STAR মেথডে নিজের কাজ (Action) ও ফলাফল (Result) তুলে ধরা",
                "Use 'I' instead of 'We' when describing your specific actions, and always close with a quantifiable result.",
                "I took the initiative to... / As a result, we achieved...",
                "I refactored the networking layer, which reduced loading times by forty percent within two weeks."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "05:50", "alex_uk",
                "Why Should We Hire You Over Other Candidates?",
                "কেন আপনাকে নিয়োগ দেওয়া উচিত—তার আত্মবিশ্বাসী উত্তর",
                "Connect the company's biggest pain point directly to your unique combination of technical execution and communication skills.",
                "What sets me apart is my combination of...",
                "What sets me apart is that I combine strong technical execution with clear client communication."
            ),
            LessonSeed(
                "Job interviews", "Advanced", "07:25", "alex_uk",
                "Confident Salary & Compensation Negotiation",
                "আত্মবিশ্বাসের সাথে বেতন ও সুযোগ-সুবিধা নিয়ে আলোচনা",
                "Never apologize when discussing salary. Express enthusiasm for the role first, then anchor your range based on market value and impact.",
                "Based on my experience and the scope of this role, I'm looking at a range of...",
                "I'm thrilled about the offer—based on the responsibilities of the role, would there be flexibility toward the upper end of the range?"
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "05:40", "alex_uk",
                "Acing Remote & International Video Interviews",
                "বিদেশি কোম্পানির রিমোট ভিডিও ইন্টারভিউতে সফল হওয়ার কৌশল",
                "In remote interviews, emphasize self-management, asynchronous documentation, and proactive communication across time zones.",
                "I thrive in distributed teams by documenting progress proactively.",
                "Working across time zones has taught me to communicate clearly and keep all stakeholders updated."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "05:35", "alex_uk",
                "Explaining a Career Gap or Job Change Positively",
                "ক্যারিয়ার গ্যাপ বা চাকরি পরিবর্তনের কারণ ইতিবাচকভাবে বলা",
                "Frame any career pause around upskilling, personal responsibility, and renewed focus for the next chapter.",
                "During that period, I focused on upskilling in...",
                "During those six months, I completed two industry certifications and built three full-scale portfolio apps."
            ),
            LessonSeed(
                "Job interviews", "Advanced", "06:40", "alex_uk",
                "Demonstrating Leadership Without a Manager Title",
                "ম্যানেজার পদবি ছাড়াই নিজের লিডারশিপ গুণাবলি ফুটিয়ে তোলা",
                "Leadership is about ownership and mentoring peers. Share how you unblocked teammates or improved a workflow.",
                "I stepped up to coordinate... / I mentored two junior colleagues...",
                "When our team lead was away, I stepped up to coordinate daily sprints and kept our release on schedule."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "06:05", "alex_uk",
                "Talking About a Past Mistake or Failure",
                "আগের কাজের কোনো ভুল থেকে কী শিখেছেন তা বলা",
                "Own the mistake honestly, explain how you fixed it immediately, and highlight the safeguard you put in place so it never happened again.",
                "That experience taught me the importance of...",
                "That incident taught me to always run automated regression checks before deploying to production."
            ),
            LessonSeed(
                "Job interviews", "Beginner", "05:15", "alex_uk",
                "Smart Questions to Ask the Interviewer at the End",
                "ইন্টারভিউয়ের শেষে হায়ারিং ম্যানেজারকে করার মতো স্মার্ট প্রশ্ন",
                "Never say 'No, I have no questions.' Ask about what success looks like in the first 90 days or the team's biggest upcoming challenge.",
                "What does success look like in the first 90 days for this role?",
                "If I join the team, what would be the most important priority for me in the first ninety days?"
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "06:45", "alex_uk",
                "Explaining Complex Technical Projects in Simple English",
                "কঠিন টেকনিক্যাল প্রজেক্ট সহজ ইংরেজিতে বুঝিয়ে বলা",
                "Start with the user problem, explain your architecture in plain English, and avoid drowning the interviewer in jargon.",
                "At a high level, the system solves... by...",
                "At a high level, our app reduces speaking anxiety by combining real-time voice recognition with 3D avatar feedback."
            ),
            LessonSeed(
                "Job interviews", "Beginner", "05:30", "sarah_us",
                "Fresher & Campus Placement Interview Confidence",
                "ফ্রেশার বা নতুন গ্র্যাজুয়েটদের জন্য প্রথম ইন্টারভিউ গাইড",
                "When you don't have years of experience, highlight your academic projects, internships, fast learning speed, and work ethic.",
                "While I'm early in my career, my capstone project gave me hands-on experience in...",
                "My university capstone project gave me hands-on experience building real-world Android applications."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "06:10", "alex_uk",
                "Pitching Transferable Skills When Switching Careers",
                "এক পেশা থেকে অন্য পেশায় যাওয়ার সময় নিজের দক্ষতা তুলে ধরা",
                "Bridge your past industry to the new one by focusing on universal skills: problem-solving, stakeholder management, and analytical thinking.",
                "My background in... gives me a unique perspective on...",
                "My background in customer support gives me deep empathy for user pain points as a product designer."
            ),
            LessonSeed(
                "Job interviews", "Beginner", "04:50", "alex_uk",
                "Closing the Interview Strong & Thank-You Pitch",
                "ইন্টারভিউয়ের শেষ ৩০ সেকেন্ডে দারুণ ইমপ্রেশন রেখে আসা",
                "Before hanging up, thank the interviewer by name, reaffirm your interest, and ask about the next steps in the hiring timeline.",
                "Thank you for your time today—I'm even more excited about the role after our conversation.",
                "Thank you so much for your time today—our conversation made me even more excited about contributing to your team."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "06:20", "alex_uk",
                "Handling 'How Do You Deal With Tight Deadlines?'",
                "কাজের চাপ ও টাইট ডেডলাইন সামলানোর অভিজ্ঞতা বলা",
                "Show that you stay calm under pressure by breaking big deliverables into milestones and communicating early if trade-offs are needed.",
                "Under tight deadlines, I prioritize ruthlessly and communicate early.",
                "When facing a tight deadline, I break the project into daily milestones and keep stakeholders informed."
            ),
            LessonSeed(
                "Job interviews", "Intermediate", "05:55", "alex_uk",
                "Answering 'Where Do You See Yourself in 5 Years?'",
                "আগামী ৫ বছরে নিজেকে কোথায় দেখতে চান—তার বাস্তবসম্মত উত্তর",
                "Align your personal growth with the company's trajectory: mastering the role first, then growing into technical or team leadership.",
                "Over the next few years, I want to deepen my expertise in... and eventually mentor others.",
                "In five years, I see myself mastering this domain and helping lead major product initiatives here."
            ),

            // --- 3. Work, Office & Business English (37..54) ---
            LessonSeed(
                "Work & business", "Beginner", "05:10", "alex_uk",
                "Crisp 60-Second Daily Standup Updates",
                "অফিসের ডেইলি স্ট্যান্ডআপ মিটিংয়ে ১ মিনিটে কাজের আপডেট দেওয়া",
                "Keep standups concise: 1) What I finished yesterday, 2) What I'm focusing on today, 3) Any blockers I need help with.",
                "Yesterday I wrapped up... / Today my main focus is... / No blockers on my end.",
                "Yesterday I wrapped up the authentication flow, and today my main focus is testing the cloud sync module."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "05:45", "alex_uk",
                "Opening & Leading Online Zoom/Teams Meetings",
                "অনলাইন জুম বা টিমস মিটিং শুরু ও পরিচালনা করার ইংরেজি",
                "Start meetings with warmth and structure: 'Thanks everyone for hopping on—let's quickly review today's agenda.'",
                "Let's kick things off / Here's what we want to cover in the next 20 minutes.",
                "Thanks everyone for joining on time—let's kick things off by reviewing our three agenda items."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "06:15", "alex_uk",
                "Pitching a New Idea or Proposal to Your Manager",
                "বস বা ম্যানেজারের কাছে নতুন কোনো আইডিয়া উপস্থাপন করা",
                "Frame your proposal around business value: 'I noticed an opportunity to save 3 hours a week by automating...'",
                "I'd love to get your thoughts on a quick proposal that could...",
                "I'd love to share a quick proposal that could cut our customer response time in half."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "06:00", "alex_uk",
                "Polite Disagreement in Professional Meetings",
                "অফিস মিটিংয়ে ভদ্রতা বজায় রেখে ভিন্নমত প্রকাশ করা",
                "Validate your colleague's point first before offering an alternative: 'That's a valid point, Alex. Have we also considered...?'",
                "I see where you're coming from, though another angle to consider is...",
                "I see where you're coming from—have we also considered how this might affect mobile users?"
            ),
            LessonSeed(
                "Work & business", "Intermediate", "06:30", "sarah_us",
                "Welcoming & Onboarding International Clients",
                "বিদেশি ক্লায়েন্টদের সাথে প্রথম মিটিং ও অনবোর্ডিং কল",
                "Build instant trust with clients by setting clear expectations, timelines, and communication channels on day one.",
                "We're thrilled to partner with you / Here is our roadmap for Week 1.",
                "We're thrilled to partner with you on this project—let me walk you through our onboarding roadmap."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "05:50", "alex_uk",
                "Communicating a Project Delay Professionally",
                "প্রজেক্টের দেরি বা সমস্যা হলে আগেভাগেই পেশাদারভাবে জানানো",
                "Never hide a delay. Inform stakeholders early, explain the root cause briefly, and present your revised recovery plan.",
                "I wanted to give you an early heads-up regarding our timeline...",
                "I wanted to give you an early heads-up that we'll need two extra days to ensure quality testing."
            ),
            LessonSeed(
                "Work & business", "Advanced", "06:20", "alex_uk",
                "Giving Constructive Feedback to Teammates",
                "সহকর্মীকে উৎসাহ বজায় রেখে গঠনমূলক ফিডব্যাক দেওয়া",
                "Focus on the work, not the person. Acknowledge what went well first, then offer specific, actionable suggestions.",
                "You did a great job on..., and one area we could sharpen further is...",
                "The UI design looks super clean, and one small tweak that could make it even better is increasing button contrast."
            ),
            LessonSeed(
                "Work & business", "Beginner", "04:55", "sarah_us",
                "Following Up Verbally on Unanswered Emails",
                "ইমেইলের উত্তর না পেলে ভদ্রভাবে ফলো-আপ করার কৌশল",
                "Avoid sounding annoyed. Say 'Hi Sarah, just floating this back to the top of your inbox whenever you have a moment.'",
                "I'm just following up on the note I sent Tuesday...",
                "Hi Alex, I wanted to quickly follow up on the proposal I sent over on Tuesday."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "05:40", "alex_uk",
                "Cross-Functional Collaboration with Design & QA",
                "অন্য ডিপার্টমেন্ট বা টিমের সাথে সমন্বয় করে কাজ করা",
                "Use collaborative language like 'How can we align on...' and 'Let's sync up for ten minutes to unblock this.'",
                "Let's sync up briefly so we stay aligned on the release.",
                "Let's hop on a quick five-minute call to align on the final design specs."
            ),
            LessonSeed(
                "Work & business", "Advanced", "06:50", "alex_uk",
                "Presenting Quarterly Results & KPIs with Impact",
                "কাজের ফলাফল ও ডেটা প্রেজেন্টেশনে আকর্ষণীয়ভাবে তুলে ধরা",
                "Don't just read numbers off a slide—tell the story behind the numbers: 'As you can see here, user retention jumped by 24% because...'",
                "The key takeaway from this chart is...",
                "The key takeaway from Q3 is that our new onboarding flow boosted daily active users by thirty percent."
            ),
            LessonSeed(
                "Work & business", "Advanced", "07:05", "alex_uk",
                "Asking for a Promotion or Performance Review",
                "প্রমোশন বা পদোন্নতির জন্য বসের সাথে সফল আলোচনা",
                "Schedule a dedicated growth conversation, document your expanded scope over the past 6 months, and ask how to reach the next level.",
                "I'd love to discuss my recent contributions and how I can grow into...",
                "Over the past year I've taken ownership of three core modules, and I'd love to discuss stepping into a senior role."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "05:30", "alex_uk",
                "Delegating Tasks Clearly Without Micromanaging",
                "টিমের সদস্যদের পরিষ্কারভাবে কাজের দায়িত্ব বুঝিয়ে দেওয়া",
                "Define the desired outcome, deadline, and available support, then give your teammate autonomy over how they execute.",
                "Could you take the lead on...? Let me know if you need any support.",
                "Could you take the lead on preparing the client deck by Thursday afternoon?"
            ),
            LessonSeed(
                "Work & business", "Intermediate", "05:25", "maya_au",
                "Running Creative Brainstorming Sessions",
                "টিমের সাথে ব্রেইনস্টর্মিং মিটিংয়ে নতুন আইডিয়া বের করা",
                "Encourage wild ideas first without judgment: 'Building on what Maya just said, what if we also...?'",
                "Building on that idea... / Let's think outside the box.",
                "Building on Sarah's idea, what if we let users practice with their own uploaded photos?"
            ),
            LessonSeed(
                "Work & business", "Advanced", "06:45", "alex_uk",
                "De-escalating Upset or Frustrated Clients",
                "রেগে যাওয়া বা অসন্তুষ্ট ক্লায়েন্টকে শান্ত ও সন্তুষ্ট করার কৌশল",
                "Listen without interrupting, validate their frustration ('I completely understand why that's frustrating'), and take immediate ownership.",
                "I completely understand your concern, and here is what we're doing right now to fix it.",
                "I completely understand how important this launch is for you, and I am personally overseeing the fix right now."
            ),
            LessonSeed(
                "Work & business", "Advanced", "06:55", "alex_uk",
                "Consultative Sales Pitch & Closing Deals",
                "ক্লায়েন্টের প্রয়োজন বুঝে প্রোডাক্ট পিচ ও ডিল ক্লোজ করা",
                "Ask discovery questions about their biggest bottleneck before pitching features, then show how your solution pays for itself.",
                "Based on what you shared, here is how we can solve that...",
                "Based on your team's goals, this workflow will save you roughly ten hours every single week."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "05:35", "maya_au",
                "Networking at Tech & Business Conferences",
                "সেমিনার বা কনফারেন্সে প্রফেশনাল নেটওয়ার্কিং করা",
                "Ask people what excited them most about the keynote speaker, exchange LinkedIn profiles smoothly, and follow up within 24 hours.",
                "What did you think of the keynote session? / Let's stay in touch on LinkedIn!",
                "I really enjoyed your question during the panel—let's definitely connect on LinkedIn!"
            ),
            LessonSeed(
                "Work & business", "Advanced", "06:15", "alex_uk",
                "Delivering a 60-Second Executive Elevator Pitch",
                "মাত্র ৬০ সেকেন্ডে নিজের স্টার্টআপ বা প্রজেক্টের এলিভেটর পিচ",
                "Hook the listener in 10 seconds with the problem, explain your unique solution in 30 seconds, and end with a clear call to action.",
                "We help [target audience] achieve [outcome] without [pain point].",
                "We help Bengali learners speak fluent English through live 3D video calls without fear of judgment."
            ),
            LessonSeed(
                "Work & business", "Intermediate", "05:15", "sarah_us",
                "Setting Healthy Boundaries & Work-Life Balance",
                "অফিসে ভদ্রভাবে নিজের কাজের সীমা ও সময়ের ব্যবস্থাপনা করা",
                "Instead of a flat 'No', offer a realistic timeline: 'I'm fully booked with the release today, but I can tackle this first thing tomorrow.'",
                "To give this the attention it deserves, could we aim for tomorrow morning?",
                "My plate is full with the client release today, but I can jump on this first thing tomorrow morning."
            ),

            // --- 4. Travel, Airport & Abroad Survival (55..72) ---
            LessonSeed(
                "Travel & abroad", "Beginner", "05:00", "sarah_us",
                "Airport Check-In, Seat Selection & Baggage Drop",
                "এয়ারপোর্ট চেক-ইন কাউন্টারে কথা বলা ও সিট নেওয়া",
                "Ask politely for your preferred seat: 'Could I have an aisle/window seat if one is available?' and check luggage weight limits.",
                "Could I get a window seat, please? / Is my carry-on within the limit?",
                "Good morning! Here is my passport—could I get a window seat if one is still available?"
            ),
            LessonSeed(
                "Travel & abroad", "Intermediate", "06:10", "james_ielts",
                "Passing International Immigration Interviews Smoothly",
                "বিমানবন্দরে ইমিগ্রেশন অফিসারের প্রশ্নের সংক্ষিপ্ত ও সঠিক উত্তর",
                "Keep answers to immigration officers short, honest, and calm. State your purpose (tourism/business/study), length of stay, and hotel name.",
                "I'm here for a 7-day vacation / I'm staying at the Grand Hotel.",
                "I'm visiting for a one-week technology conference and staying at the downtown Marriott."
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "04:45", "maya_au",
                "Customs & Security Screening Conversations",
                "সিকিউরিটি চেক ও কাস্টমস ডেস্কে প্রয়োজনীয় কথোপকথন",
                "Know key security phrases: 'Do I need to take my laptop out of the bag?' and 'I have nothing to declare.'",
                "Do I need to remove my shoes and laptop? / I have nothing to declare.",
                "Excuse me, do I need to take my tablet and liquids out of my backpack?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "04:30", "sarah_us",
                "In-Flight Requests with Cabin Crew",
                "বিমানের ভেতরে কেবিন ক্রুদের সাথে কথা বলা",
                "Use polite requests in the cabin: 'Could I have a glass of water when you have a moment?' or 'Excuse me, could I get a blanket?'",
                "Could I get a cup of warm water, please?",
                "Excuse me, could I have a glass of water and an arrival card when you have a moment?"
            ),
            LessonSeed(
                "Travel & abroad", "Intermediate", "05:40", "maya_au",
                "Hotel Check-In, Amenities & Polite Room Upgrade",
                "হোটেলে চেক-ইন করা এবং ভদ্রভাবে ভালো রুম চাওয়া",
                "At the front desk, ask 'I have a reservation under...' and 'Is there any chance of a quiet room on a higher floor?'",
                "I have a reservation under the name... / What time is breakfast served?",
                "Hi! I have a reservation under Rahman—would it be possible to get a quiet room on a higher floor?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "05:05", "maya_au",
                "Asking Locals for Authentic Food & Hidden Gems",
                "স্থানীয় মানুষের কাছে সেরা রেস্তোরাঁ ও দর্শনীয় স্থানের খোঁজ নেওয়া",
                "Skip tourist traps by asking locals: 'Where do locals usually go for authentic coffee or dinner around here?'",
                "What's your favorite local spot for dinner around here?",
                "We'd love to try authentic local food—where do people from the neighborhood usually eat?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "05:15", "alex_uk",
                "Buying Train/Metro Tickets & Platform Navigation",
                "বিদেশে ট্রেন বা মেট্রো স্টেশনে টিকেট কাটা ও প্ল্যাটফর্ম খোঁজা",
                "Ask 'Which platform does the express train to central station leave from?' and 'Do I need to transfer lines?'",
                "Does this train stop at Central Station? / Which exit should I take?",
                "Excuse me, does this metro line go directly to the museum, or do I need to transfer?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "04:50", "maya_au",
                "Ordering Street Food & Checking Ingredients",
                "স্ট্রিট ফুড বা নতুন খাবার অর্ডার করার সময় উপাদান জেনে নেওয়া",
                "Ask 'What comes inside this dish?' or 'Is it very spicy?' so you can enjoy new cuisines safely.",
                "What's in this dish? / Could you make it mild, please?",
                "This looks delicious—what's inside the filling, and could I get a mild version?"
            ),
            LessonSeed(
                "Travel & abroad", "Intermediate", "05:55", "sarah_us",
                "Visiting a Pharmacy Abroad for Medicine",
                "বিদেশে ফার্মেসিতে গিয়ে ওষুধ বা প্রাথমিক চিকিৎসা চাওয়া",
                "Describe your symptom clearly to the pharmacist: 'Do you have something over-the-counter for motion sickness or allergies?'",
                "Do you have anything over-the-counter for a upset stomach?",
                "Hi, do you have an over-the-counter medicine for travel motion sickness?"
            ),
            LessonSeed(
                "Travel & abroad", "Intermediate", "06:20", "alex_uk",
                "Handling Lost Luggage or Missing Items at the Airport",
                "ব্যাগ বা লাগেজ হারিয়ে গেলে ব্যাগেজ ক্লেইম অফিসে রিপোর্ট করা",
                "Stay calm at the baggage service desk: provide your baggage tag receipt, describe the suitcase color/brand, and give your hotel address.",
                "My suitcase didn't come out on the carousel—here is my baggage claim tag.",
                "My black hard-shell suitcase didn't arrive on flight BA-142—here is my baggage tag number."
            ),
            LessonSeed(
                "Travel & abroad", "Intermediate", "05:30", "sarah_us",
                "Renting a Car & Understanding Insurance Options",
                "বিদেশে গাড়ি ভাড়া করা ও ইন্স্যুরেন্স বুঝে নেওয়া",
                "Clarify fuel policy ('Full-to-full') and insurance coverage before signing a rental agreement.",
                "Does this rate include full collision coverage? / Is it full-to-full on fuel?",
                "Could you confirm whether full insurance coverage is included in this rental rate?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "04:40", "james_ielts",
                "Chatting with Museum & Historical Tour Guides",
                "মিউজিয়াম বা দর্শনীয় স্থানে ট্যুর গাইডের সাথে কথা বলা",
                "Engage tour guides with curious questions: 'How long did it take to build this?' or 'Are photos allowed inside?'",
                "Is photography without flash allowed inside this gallery?",
                "That history is fascinating—how many years did it take to restore this building?"
            ),
            LessonSeed(
                "Travel & abroad", "Intermediate", "05:10", "maya_au",
                "Polite Bargaining at Local Souvenir Markets",
                "বিদেশের লোকাল মার্কেটে ভদ্রভাবে দামাদামি করা",
                "Bargain with a warm smile: 'If I buy two of these, could you give me a slightly better price?'",
                "Could you do a slightly better price if I take two?",
                "These handmade scarves are beautiful—if I buy three for my family, what's the best price you could do?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "05:00", "maya_au",
                "Making Friends with Fellow Travelers in Hostels/Tours",
                "ভ্রমণে অন্য দেশের পর্যটকদের সাথে বন্ধুত্ব করা",
                "Travelers love sharing itineraries: 'Where are you traveling from?' and 'What's been the highlight of your trip so far?'",
                "What's been the highlight of your trip so far?",
                "Hi! Where are you traveling from, and what has been the best part of your trip so far?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "04:55", "sarah_us",
                "Communicating Halal, Vegetarian or Allergy Needs",
                "রেস্তোরাঁয় হালাল, ভেজিটেরিয়ান বা অ্যালার্জির কথা পরিষ্কারভাবে বলা",
                "Be specific with servers: 'Does this dish contain any nuts or alcohol?' or 'Do you have halal/vegetarian options?'",
                "Do you have halal or vegetarian options on the menu?",
                "Excuse me, could you let me know which dishes on the menu are halal or vegetarian?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "04:35", "alex_uk",
                "Requesting Late Checkout & Luggage Storage at Hotels",
                "হোটেলে লেট চেক-আউট ও লাগেজ রাখার অনুরোধ করা",
                "Even if late checkout isn't free, hotels will almost always store your bags: 'Could we leave our bags with the concierge until 5 PM?'",
                "Would a 1 PM late checkout be possible? / Can you hold our luggage?",
                "Our flight is in the evening—could we leave our suitcases at the front desk for a few hours?"
            ),
            LessonSeed(
                "Travel & abroad", "Beginner", "04:45", "sarah_us",
                "Uber & Taxi Conversations: Route & Drop-Off",
                "ট্যাক্সি বা উবারে গন্তব্য ও রাস্তা বুঝিয়ে বলা",
                "Confirm your name and destination when entering a ride-share, and use 'Could you drop me off right by the main entrance?'",
                "Could you drop me off right across from the main gate, please?",
                "Hi! Going to Terminal 2—could you drop me off right by the departures entrance?"
            ),
            LessonSeed(
                "Travel & abroad", "Intermediate", "05:25", "alex_uk",
                "Handling Flight Delays & Rebooking at the Gate",
                "ফ্লাইট ডিলে বা ক্যানসেল হলে এয়ারলাইন ডেস্কে কথা বলা",
                "When flights get delayed, kindness wins the agent over: 'I'm worried about missing my connecting flight—what are my best rebooking options?'",
                "Could you check if there's an earlier connecting flight available?",
                "My flight was delayed and I might miss my connection—could you help me check alternative flights?"
            ),

            // --- 5. IELTS Speaking Band 7.5–8.5 Masterclass (73..90) ---
            LessonSeed(
                "IELTS speaking", "Beginner", "06:00", "james_ielts",
                "IELTS Part 1: Talking About Your Hometown & Home",
                "IELTS Part 1: নিজের এলাকা ও বাসা নিয়ে Band 7+ উত্তর",
                "Never give 1-word answers in Part 1. Use the 3-sentence rule: Direct Answer + Specific Detail + Personal Feeling.",
                "I hail from a vibrant city... / What I love most about my neighborhood is...",
                "I grew up in Dhaka, a bustling metropolis known for its warm hospitality and rich culinary heritage."
            ),
            LessonSeed(
                "IELTS speaking", "Beginner", "06:10", "james_ielts",
                "IELTS Part 1: Work vs Studies High-Scoring Vocabulary",
                "IELTS Part 1: পড়াশোনা বা চাকরি নিয়ে ন্যাচারাল উত্তর",
                "Instead of 'I study computer science', say 'I'm currently pursuing my undergraduate degree in computer science, focusing on AI.'",
                "I'm currently pursuing... / My day-to-day responsibilities revolve around...",
                "I'm currently pursuing my degree in software engineering, which I find both challenging and deeply rewarding."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "07:15", "james_ielts",
                "IELTS Part 2 Cue Card: A Person Who Inspired You",
                "IELTS Part 2: অনুপ্রেরণাদায়ী একজন মানুষ নিয়ে ২ মিনিট কথা বলা",
                "Structure your 2-minute talk chronologically: how you met them, their defining qualities, a specific story, and their lasting impact on you.",
                "Someone who has had a profound influence on my life is...",
                "Someone who has had a profound influence on my life is my high school English teacher."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "07:20", "james_ielts",
                "IELTS Part 2 Cue Card: A Memorable Journey",
                "IELTS Part 2: একটি স্মরণীয় ভ্রমণ নিয়ে ২ মিনিটের কিউ কার্ড",
                "Use vivid descriptive adjectives ('breathtaking', 'serene', 'unforgettable') and explain why the trip changed your perspective.",
                "What made this journey truly unforgettable was...",
                "What made this journey to the mountains truly unforgettable was the serene sunrise over the clouds."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "07:10", "james_ielts",
                "IELTS Part 2 Cue Card: A Practical Skill You Learned",
                "IELTS Part 2: নতুন শেখা কোনো দক্ষতা নিয়ে ২ মিনিট কথা বলা",
                "Describe the initial learning curve ('At first it was quite daunting') and the breakthrough moment when you mastered it.",
                "Initially I found it quite daunting, but with consistent practice...",
                "Initially I found public speaking quite daunting, but daily video practice transformed my confidence."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "06:55", "james_ielts",
                "IELTS Part 2 Cue Card: A Book or Film That Moved You",
                "IELTS Part 2: প্রিয় বই বা সিনেমা নিয়ে Band 8 স্টোরিটেলিং",
                "Don't spend 2 minutes summarizing the plot—spend 60% of your time analyzing the theme and why it resonated with you personally.",
                "The central theme of the story really resonated with me because...",
                "The film's message about perseverance really resonated with me during my university exams."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "06:30", "james_ielts",
                "Mastering the 1-Minute Cue Card Preparation Timer",
                "IELTS Part 2-এর ১ মিনিট প্রস্তুতির সময় কাজে লাগানোর কৌশল",
                "Never write full sentences on your notepad! Write 4 bullet keywords and 3 high-band collocations you want to weave into your talk.",
                "Write keywords + 3 Band-8 collocations during your 60-second prep.",
                "To wrap up my talk, that experience taught me resilience and adaptability."
            ),
            LessonSeed(
                "IELTS speaking", "Advanced", "07:30", "james_ielts",
                "IELTS Part 3: Discussing Technology & AI in Society",
                "IELTS Part 3: প্রযুক্তি ও ভবিষ্যৎ সমাজ নিয়ে গভীর আলোচনা",
                "In Part 3, speak about society in general rather than yourself: 'By and large, automation has transformed how younger generations work.'",
                "By and large... / It is widely argued that...",
                "By and large, artificial intelligence has democratized access to personalized education worldwide."
            ),
            LessonSeed(
                "IELTS speaking", "Advanced", "07:25", "james_ielts",
                "IELTS Part 3: Education Systems & Future Skills",
                "IELTS Part 3: শিক্ষাব্যবস্থা ও ভবিষ্যৎ দক্ষতা নিয়ে প্রশ্নোত্তর",
                "Compare two sides of an issue using 'On the one hand... whereas on the other hand...' to showcase grammatical range.",
                "While traditional exams test memory, project-based learning fosters creativity.",
                "While theoretical knowledge is essential, schools should place greater emphasis on practical communication."
            ),
            LessonSeed(
                "IELTS speaking", "Advanced", "07:15", "james_ielts",
                "IELTS Part 3: Environment & Sustainable Cities",
                "IELTS Part 3: পরিবেশ ও নগরায়ন নিয়ে উচ্চমানের ভোকাবুলারি",
                "Use topic-specific vocabulary like 'carbon footprint', 'renewable energy transition', and 'urban congestion'.",
                "Transitioning to renewable energy is vital for reducing our carbon footprint.",
                "Investing in green public transport is one of the most effective ways to reduce urban congestion."
            ),
            LessonSeed(
                "IELTS speaking", "Advanced", "06:50", "james_ielts",
                "20 Natural Idioms for Band 7.5+ (Without Sounding Forced)",
                "জোরপূর্বক না শুনিয়ে স্বাভাবিকভাবে Band 8 Idioms ব্যবহার",
                "Avoid outdated clichés like 'raining cats and dogs'. Use modern idioms like 'a double-edged sword', 'once in a blue moon', and 'second nature'.",
                "Social media can be a double-edged sword for productivity.",
                "Smartphone notifications can be a double-edged sword when you are trying to study deeply."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "06:20", "james_ielts",
                "Cohesive Linking Words & Discourse Markers",
                "বাক্যের মাঝে সুন্দর সংযোগ তৈরি করার লিঙ্কিং ওয়ার্ডস",
                "Replace repetitive 'And / Also / But' with 'Furthermore', 'Having said that', 'Consequently', and 'To put it another way'.",
                "Having said that... / Not only that, but also...",
                "Living abroad can be expensive; having said that, the cultural experience is priceless."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "06:05", "james_ielts",
                "Buying Thinking Time Without Saying 'Um' or 'Uh'",
                "উত্তর ভাবার সময় 'Um/Uh' না বলে স্মার্ট ফিলার ফ্রেজ ব্যবহার",
                "When the examiner asks a tough question, buy 3 seconds naturally: 'That's an interesting question—I haven't thought about it that way before, but...'",
                "That's a fascinating question—let me think about that for a moment.",
                "That's a really interesting question—if I had to pick one main reason, I'd say..."
            ),
            LessonSeed(
                "IELTS speaking", "Advanced", "07:00", "james_ielts",
                "Using Conditionals & Complex Sentences Effortlessly",
                "Grammatical Range বাড়াতে Conditional বাক্যের সহজ ব্যবহার",
                "Drop one 'If I had known...' or 'If governments were to invest...' into your Part 3 answer to immediately boost your Grammar band.",
                "If schools were to introduce speaking labs earlier, students would feel far more confident.",
                "If cities invested more in cycling lanes, air quality would improve dramatically."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "06:15", "james_ielts",
                "Paraphrasing the Examiner's Prompt Like a Native",
                "এক্সামিনারের প্রশ্নের শব্দ হুবহু রিপিট না করে প্যারাফ্রেজ করা",
                "If the examiner asks 'Is it important for children to play sports?', don't start with 'Yes, it is important...'—say 'Without a doubt, physical activity plays a vital role...'",
                "Without a doubt, physical activity plays a crucial role in child development.",
                "Undoubtedly, regular physical exercise is vital for both mental and physical well-being."
            ),
            LessonSeed(
                "IELTS speaking", "Intermediate", "05:50", "james_ielts",
                "Graceful Self-Correction During the Speaking Test",
                "কথা বলার মাঝে ভুল হলে নার্ভাস না হয়ে স্মার্টভাবে ঠিক করে নেওয়া",
                "Native speakers self-correct too! Simply say 'Or rather...' or 'What I mean is...' and keep flowing without apologizing.",
                "Or rather, what I mean to say is...",
                "He moved to London in 2018—or rather, early 2019—to start his master's degree."
            ),
            LessonSeed(
                "IELTS speaking", "Advanced", "06:40", "james_ielts",
                "English Intonation, Word Stress & Rhythm for Band 8",
                "রোবটের মতো না বলে কণ্ঠের ওঠানামা ও ছন্দ (Intonation) ঠিক রাখা",
                "English is a stress-timed language. Stress content words (nouns, main verbs, adjectives) and glide quickly over grammar words.",
                "Stress key content words and vary your vocal pitch naturally.",
                "It was an ABSOLUTELY unforgettable experience that COMPLETELY changed my perspective."
            ),
            LessonSeed(
                "IELTS speaking", "Advanced", "07:40", "james_ielts",
                "Full 14-Minute IELTS Speaking Day Checklist",
                "পরীক্ষার দিন প্রথম মিনিট থেকে শেষ মিনিট পর্যন্ত পূর্ণাঙ্গ গাইড",
                "Maintain warm eye contact, smile when entering, speak at a steady conversational pace, and keep speaking until the examiner transitions.",
                "Walk in with a warm smile, maintain eye contact, and expand every answer naturally.",
                "Good afternoon! I'm thrilled to be here today and ready to begin."
            ),

            // --- 6. Study, University & Bangla-to-English Grammar Hacks (91..108) ---
            LessonSeed(
                "Study & Grammar", "Beginner", "05:20", "sarah_us",
                "Talking to University Professors During Office Hours",
                "ইউনিভার্সিটির প্রফেসরের সাথে অ্যাসাইনমেন্ট বা রিসার্চ নিয়ে কথা বলা",
                "Approach professors respectfully: 'Hi Professor James, do you have five minutes to clarify a question about the research paper?'",
                "Could you clarify the grading rubric for the second section?",
                "Excuse me Professor, could I ask for your feedback on my research topic outline?"
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "05:10", "maya_au",
                "Contributing Confidently in University Group Projects",
                "গ্রুপ অ্যাসাইনমেন্ট বা প্রজেক্টে সহপাঠীদের সাথে আলোচনা",
                "Divide responsibilities clearly: 'How about I handle the data analysis and slides, and we review the script together on Thursday?'",
                "How about I take care of the research section, and we sync up on Wednesday?",
                "How about I design the presentation slides while you finalize the literature review?"
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "06:00", "alex_uk",
                "Powerful Seminar & Class Presentation Openings",
                "ক্লাস বা সেমিনার প্রেজেন্টেশনের প্রথম ১ মিনিটে সবার মনোযোগ কাড়া",
                "Start your presentation with a surprising question or story instead of 'Today my topic is...'",
                "Have you ever wondered why 80% of learners understand English but hesitate to speak?",
                "Imagine walking into your dream interview and speaking English without a single moment of hesitation."
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "05:50", "alex_uk",
                "Handling Q&A Sessions After a Class Presentation",
                "প্রেজেন্টেশনের শেষে দর্শকদের প্রশ্নের স্মার্ট উত্তর দেওয়া",
                "Thank the person for their question, answer concisely, and if you need to check a statistic, say 'I'd be happy to verify that exact figure after the session.'",
                "That's a great question—let me break that down into two parts.",
                "Thank you for raising that point—the main reason we chose this approach was scalability."
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "04:45", "sarah_us",
                "Asking Librarians & Academic Advisors for Help",
                "লাইব্রেরি বা একাডেমিক অ্যাডভাইজারের কাছে সাহায্য চাওয়া",
                "Use clear academic terms: 'Could you show me how to access the peer-reviewed journal database?'",
                "Which electives would you recommend for next semester?",
                "Could you help me check if I've met all the credit requirements for graduation?"
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "06:30", "james_ielts",
                "Winning International Scholarship Interviews",
                "বিদেশি স্কলারশিপ ইন্টারভিউতে নিজের লক্ষ্য ও ইমপ্যাক্ট তুলে ধরা",
                "Scholarship panels look for community impact. Explain how your degree will solve a real problem in your home country.",
                "Upon completing my degree, my goal is to contribute to...",
                "This scholarship will equip me to build accessible educational technology for students in Bangladesh."
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "06:40", "alex_uk",
                "Student Visa (F-1 / UK Tier-4) Interview Preparation",
                "স্টুডেন্ট ভিসা ইন্টারভিউতে ভিসা অফিসারের প্রশ্নের নিখুঁত উত্তর",
                "Be specific about why you chose your university, how your sponsors are funding your tuition, and your career plan after graduation.",
                "I chose this university specifically for its research lab in...",
                "I chose this program because its curriculum aligns directly with my career plan in data science."
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "04:50", "maya_au",
                "Joining Campus Clubs & Introducing Yourself at Events",
                "ক্যাম্পাসের ক্লাব বা সোসাইটিতে নিজের পরিচয় দেওয়া",
                "Share your major, year, and what drew you to the club: 'Hey! I'm a second-year CS student and I'd love to join the debate club.'",
                "I'm a second-year student and I'm super keen to get involved!",
                "Hi everyone! I'm a freshman in business administration and excited to join the speaking club."
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "06:15", "sarah_us",
                "How to Stop Translating Bangla Word-by-Word in Your Head",
                "মাথায় বাংলা থেকে শব্দ ধরে অনুবাদ না করে সরাসরি ইংরেজিতে ভাবা",
                "Instead of translating word-by-word, learn English in 'sentence chunks' (like 'I was wondering if...' or 'It turns out that...').",
                "Think in 3-word English chunks rather than isolated words.",
                "It turns out that practicing in chunks makes speaking ten times faster."
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "05:30", "sarah_us",
                "Bangla-to-English Hack: 'I Have Done' vs 'I Did'",
                "বাংলা 'করেছি' (Present Perfect) বনাম 'করেছিলাম' (Past Simple) এর সঠিক ব্যবহার",
                "If you mention a finished past time (yesterday, last night, in 2022), ALWAYS use Past Simple ('I went yesterday'), never 'I have gone yesterday'.",
                "I finished the report yesterday (NOT 'I have finished yesterday').",
                "I submitted the assignment last night, and I have already started the next module."
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "05:25", "sarah_us",
                "Mastering 'I Am Used To' vs 'I Used To'",
                "'অভ্যস্ত ছিলাম' (Used to do) বনাম 'এখন অভ্যস্ত' (Be used to doing)",
                "'I used to wake up late' means a past habit that stopped. 'I am used to waking up early' means you are comfortable with it now!",
                "I used to feel nervous, but now I'm used to speaking on camera.",
                "I used to hesitate in meetings, but now I am used to sharing my ideas openly."
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "05:15", "alex_uk",
                "Polite Modal Verbs: Could, Would, Should & Might",
                "কথায় ভদ্রতা ও নমনীয়তা আনতে Could, Would, Should-এর জাদুকরী ব্যবহার",
                "'Can you send this?' sounds like an order; 'Could you send this when you get a chance?' sounds like a leader.",
                "Would it be okay if...? / Could we possibly...?",
                "Could we possibly reschedule our call to three o'clock this afternoon?"
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "05:45", "james_ielts",
                "Real-Life Conditionals (If I Were You / Had I Known)",
                "কথোপকথনে শতভাগ সঠিক Conditional বাক্য তৈরির নিয়ম",
                "Use 'If I were you, I'd...' for friendly advice, and 'If I had known earlier, I would have...' for past situations.",
                "If I were in your shoes, I would definitely apply for that role.",
                "If I had known you were in town, I would have invited you for coffee!"
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "05:35", "maya_au",
                "15 Essential Phrasal Verbs for Daily Conversation",
                "দৈনন্দিন কথায় সবচেয়ে বেশি ব্যবহৃত ১৫টি Phrasal Verbs",
                "Native speakers say 'figure out' instead of 'solve', 'put off' instead of 'postpone', and 'run into' instead of 'meet unexpectedly'.",
                "Let's figure this out together / Don't put it off until tomorrow.",
                "I ran into an old university friend while grabbing coffee this morning."
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "05:00", "sarah_us",
                "Fixing In, On, At Preposition Mistakes Forever",
                "সময় ও স্থানের ক্ষেত্রে In, On, At-এর ভুল চিরতরে দূর করার উপায়",
                "Use AT for exact times/points (at 5 PM, at the door), ON for days/surfaces (on Monday, on the table), and IN for months/years/cities (in July, in Dhaka).",
                "See you on Monday at 10 AM in the main conference room!",
                "Our flight leaves on Friday at seven in the morning."
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "05:40", "alex_uk",
                "When to Use Passive Voice in Business & Reports",
                "অফিস বা রিপোর্টে কাউকে দোষারোপ না করে কথা বলতে Passive Voice",
                "When a mistake happens at work, use polite passive voice: 'The file wasn't attached' sounds much gentler than 'You forgot to attach the file.'",
                "The shipment has been dispatched / A few updates were made to the schedule.",
                "A small adjustment was made to the project timeline this morning."
            ),
            LessonSeed(
                "Study & Grammar", "Beginner", "05:05", "maya_au",
                "Sounding Natural with Contractions (I'll, We've, They'd)",
                "রোবটের মতো না শুনিয়ে ন্যাচারাল শুনতে Contractions-এর ব্যবহার",
                "In spoken English, saying 'I will not be able to' sounds stiff. Practice flowing smoothly with 'I won't be able to' and 'We've been waiting.'",
                "I'll let you know as soon as we've finalized the plan.",
                "We've been working on this all week, and I'm sure you'll love the result."
            ),
            LessonSeed(
                "Study & Grammar", "Intermediate", "06:30", "sarah_us",
                "Your 30-Day Unstoppable English Speaking Blueprint",
                "৩০ দিনে ইংরেজিতে কথা বলার জড়তা পুরোপুরি কাটানোর মাস্টারপ্ল্যান",
                "Combine 5 minutes of daily 3D Video Calls, 3 new phrases in your 3-Tone Vocab Bank, and 1 Phoneme drill every day for 30 days.",
                "Consistency beats perfection—speak out loud for 5 minutes every single day!",
                "Every single day I am becoming a clearer, calmer, and more confident English speaker."
            )
        )

        rawSpecs.mapIndexed { idx, seed ->
            val num = idx + 1
            VideoLesson(
                id = "vid_lesson_$num",
                lessonNumber = num,
                title = seed.title,
                category = seed.category,
                levelBadge = seed.level,
                durationLabel = seed.duration,
                tutorId = seed.tutorId,
                banglaTitle = seed.banglaTitle,
                videoScriptEnglish = seed.scriptEn,
                banglaSummary = "লেসন #$num (${seed.category}): ${seed.banglaTitle}। এই ভিডিও লেসনে টিউটর আপনাকে উচ্চারণ, ভোকাবুলারি এবং বাস্তব উদাহরণসহ ধাপে ধাপে শেখাবেন।",
                keyPatternOrVocab = seed.keyPattern,
                shadowPracticeSentence = seed.shadowSentence,
                roleplayPrompt = "You are coaching the user on Video Lesson #$num: '${seed.title}'. Practice the key pattern '${seed.keyPattern}' in a realistic face-to-face conversation."
            )
        }
    }

    private data class LessonSeed(
        val category: String,
        val level: String,
        val duration: String,
        val tutorId: String,
        val title: String,
        val banglaTitle: String,
        val scriptEn: String,
        val keyPattern: String,
        val shadowSentence: String
    )
}
