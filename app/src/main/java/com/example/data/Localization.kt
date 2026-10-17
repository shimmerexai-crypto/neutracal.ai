package com.example.data

import com.example.model.AppLanguage

object Localization {
    fun t(key: String, lang: AppLanguage): String {
        val strings = mapOf(
            "app_name" to mapOf(
                AppLanguage.ENGLISH to "Nutracal.ai",
                AppLanguage.HINDI to "Nutracal.ai",
                AppLanguage.HINGLISH to "Nutracal.ai"
            ),
            "tagline" to mapOf(
                AppLanguage.ENGLISH to "AI Food & Thali Scanner for India",
                AppLanguage.HINDI to "भारतीय भोजन के लिए स्मार्ट AI स्कैनर",
                AppLanguage.HINGLISH to "Apki Thali Ka Smart AI Scanner"
            ),
            // Bottom Tabs
            "tab_home" to mapOf(
                AppLanguage.ENGLISH to "Diary",
                AppLanguage.HINDI to "डायरी",
                AppLanguage.HINGLISH to "Diary"
            ),
            "tab_search" to mapOf(
                AppLanguage.ENGLISH to "Search",
                AppLanguage.HINDI to "सर्च",
                AppLanguage.HINGLISH to "Search"
            ),
            "tab_scan" to mapOf(
                AppLanguage.ENGLISH to "Scan AI",
                AppLanguage.HINDI to "स्कैन AI",
                AppLanguage.HINGLISH to "Scan AI"
            ),
            "tab_history" to mapOf(
                AppLanguage.ENGLISH to "History",
                AppLanguage.HINDI to "इतिहास",
                AppLanguage.HINGLISH to "History"
            ),
            "tab_insights" to mapOf(
                AppLanguage.ENGLISH to "Progress",
                AppLanguage.HINDI to "प्रगति",
                AppLanguage.HINGLISH to "Progress"
            ),
            "tab_settings" to mapOf(
                AppLanguage.ENGLISH to "Settings",
                AppLanguage.HINDI to "सेटिंग्स",
                AppLanguage.HINGLISH to "Settings"
            ),
            "add_food" to mapOf(
                AppLanguage.ENGLISH to "+ Add Food",
                AppLanguage.HINDI to "+ खाना जोड़ें",
                AppLanguage.HINGLISH to "+ Add Food"
            ),
            "exercise_burned" to mapOf(
                AppLanguage.ENGLISH to "Exercise",
                AppLanguage.HINDI to "व्यायाम",
                AppLanguage.HINGLISH to "Exercise"
            ),
            "remaining_cal" to mapOf(
                AppLanguage.ENGLISH to "Remaining",
                AppLanguage.HINDI to "बची हुई",
                AppLanguage.HINGLISH to "Remaining"
            ),
            "base_goal" to mapOf(
                AppLanguage.ENGLISH to "Base Goal",
                AppLanguage.HINDI to "दैनिक लक्ष्य",
                AppLanguage.HINGLISH to "Daily Goal"
            ),
            "food_eaten" to mapOf(
                AppLanguage.ENGLISH to "Food",
                AppLanguage.HINDI to "भोजन",
                AppLanguage.HINGLISH to "Food"
            ),
            // Auth
            "auth_welcome" to mapOf(
                AppLanguage.ENGLISH to "Scan. Eat. Thrive.",
                AppLanguage.HINDI to "स्कैन करें। सही खाएं। स्वस्थ रहें।",
                AppLanguage.HINGLISH to "Apni Diet Ko Smart Banayein"
            ),
            "auth_sub" to mapOf(
                AppLanguage.ENGLISH to "Instant ICMR-NIN calorie counts, sugar spike meter & desi health fixes for your Indian meals.",
                AppLanguage.HINDI to "भारतीय थाली की सटीक कैलोरी, शुगर स्पाइक मीटर और देसी स्वास्थ्य समाधान।",
                AppLanguage.HINGLISH to "Roti, dal, sabzi se lekar biryani tak - exact macros aur sugar spike meter."
            ),
            "google_signin" to mapOf(
                AppLanguage.ENGLISH to "Continue with Google",
                AppLanguage.HINDI to "Google से आगे बढ़ें",
                AppLanguage.HINGLISH to "Continue with Google"
            ),
            "email_signin" to mapOf(
                AppLanguage.ENGLISH to "Sign In with Email",
                AppLanguage.HINDI to "ईमेल से साइन इन करें",
                AppLanguage.HINGLISH to "Email se Login Karein"
            ),
            "quick_demo" to mapOf(
                AppLanguage.ENGLISH to "Explore Demo Account",
                AppLanguage.HINDI to "डेमो अकाउंट देखें",
                AppLanguage.HINGLISH to "Quick Guest Demo"
            ),
            // Onboarding
            "onboarding_title" to mapOf(
                AppLanguage.ENGLISH to "Let's personalize your plan",
                AppLanguage.HINDI to "आइए आपकी डाइट पर्सनलाइज़ करें",
                AppLanguage.HINGLISH to "Apna Daily Target Set Karein"
            ),
            "step_name" to mapOf(
                AppLanguage.ENGLISH to "What's your name?",
                AppLanguage.HINDI to "आपका नाम क्या है?",
                AppLanguage.HINGLISH to "Aapka Shubh Naam?"
            ),
            "step_gender" to mapOf(
                AppLanguage.ENGLISH to "Select your biological sex",
                AppLanguage.HINDI to "अपना जेंडर चुनें",
                AppLanguage.HINGLISH to "Apna Gender Chunein"
            ),
            "step_age" to mapOf(
                AppLanguage.ENGLISH to "How old are you?",
                AppLanguage.HINDI to "आपकी उम्र कितनी है?",
                AppLanguage.HINGLISH to "Aapki Age Kitni Hai?"
            ),
            "step_body" to mapOf(
                AppLanguage.ENGLISH to "Height & Weight (BMI)",
                AppLanguage.HINDI to "लंबाई और वजन (BMI)",
                AppLanguage.HINGLISH to "Height & Weight Check"
            ),
            "step_referral" to mapOf(
                AppLanguage.ENGLISH to "Where did you hear about us?",
                AppLanguage.HINDI to "आपने हमारे बारे में कहाँ सुना?",
                AppLanguage.HINGLISH to "Nutracal kahan se pata chala?"
            ),
            "step_goal" to mapOf(
                AppLanguage.ENGLISH to "What is your main health goal?",
                AppLanguage.HINDI to "आपका मुख्य स्वास्थ्य लक्ष्य क्या है?",
                AppLanguage.HINGLISH to "Main Goal Kya Hai?"
            ),
            "continue_btn" to mapOf(
                AppLanguage.ENGLISH to "Continue",
                AppLanguage.HINDI to "आगे बढ़ें",
                AppLanguage.HINGLISH to "Next"
            ),
            "finish_setup" to mapOf(
                AppLanguage.ENGLISH to "Start Healthy Journey",
                AppLanguage.HINDI to "शुरू करें",
                AppLanguage.HINGLISH to "Let's Start"
            ),
            // Dashboard
            "banner_title" to mapOf(
                AppLanguage.ENGLISH to "Scan Your Thali",
                AppLanguage.HINDI to "थाली स्कैन करें",
                AppLanguage.HINGLISH to "Thali Scan Karein"
            ),
            "banner_sub" to mapOf(
                AppLanguage.ENGLISH to "Instant ICMR-NIN macro breakdown & sugar spike alert in 3 seconds",
                AppLanguage.HINDI to "3 सेकंड में भोजन की कैलोरी, प्रोटीन और शुगर स्पाइक देखें",
                AppLanguage.HINGLISH to "Photo kheecho aur pata karo exact calories, protein aur sugar spike"
            ),
            "ring_simple" to mapOf(
                AppLanguage.ENGLISH to "Simple",
                AppLanguage.HINDI to "सादा",
                AppLanguage.HINGLISH to "Simple"
            ),
            "ring_detailed" to mapOf(
                AppLanguage.ENGLISH to "Detailed",
                AppLanguage.HINDI to "विस्तृत",
                AppLanguage.HINGLISH to "Detailed"
            ),
            "calories_left" to mapOf(
                AppLanguage.ENGLISH to "Calories Left",
                AppLanguage.HINDI to "कैलोरी शेष",
                AppLanguage.HINGLISH to "Calories Bachi Hain"
            ),
            "eaten_today" to mapOf(
                AppLanguage.ENGLISH to "Consumed",
                AppLanguage.HINDI to "खाई गई",
                AppLanguage.HINGLISH to "Consumed"
            ),
            "weekly_insight_title" to mapOf(
                AppLanguage.ENGLISH to "Weekly Habit Insight",
                AppLanguage.HINDI to "साप्ताहिक डाइट सुझाव",
                AppLanguage.HINGLISH to "Weekly Desi Insight"
            ),
            "water_tracker" to mapOf(
                AppLanguage.ENGLISH to "Hydration Tracker",
                AppLanguage.HINDI to "पानी का स्तर",
                AppLanguage.HINGLISH to "Pani Tracker (Water)"
            ),
            "add_glass" to mapOf(
                AppLanguage.ENGLISH to "+250 ml Glass",
                AppLanguage.HINDI to "+250 मिली",
                AppLanguage.HINGLISH to "+1 Glass (+250ml)"
            ),
            "todays_meals" to mapOf(
                AppLanguage.ENGLISH to "Today's Meals",
                AppLanguage.HINDI to "आज का खाना",
                AppLanguage.HINGLISH to "Aaj Ka Khana"
            ),
            "quick_add" to mapOf(
                AppLanguage.ENGLISH to "Quick Add",
                AppLanguage.HINDI to "जल्दी जोड़ें",
                AppLanguage.HINGLISH to "+ Quick Add"
            ),
            // Cooking Styles
            "cooking_style_label" to mapOf(
                AppLanguage.ENGLISH to "Cooking Style",
                AppLanguage.HINDI to "पकाने का तरीका",
                AppLanguage.HINGLISH to "Khane Ka Style"
            ),
            "style_home" to mapOf(
                AppLanguage.ENGLISH to "Home Normal (1 tsp oil)",
                AppLanguage.HINDI to "घर का सादा (1 चम्मच तेल)",
                AppLanguage.HINGLISH to "Home Normal (1 tsp oil)"
            ),
            "style_ghee" to mapOf(
                AppLanguage.ENGLISH to "Extra Ghee / Makhan",
                AppLanguage.HINDI to "शुद्ध देसी घी / मक्खन",
                AppLanguage.HINGLISH to "Extra Ghee / Makhan"
            ),
            "style_dhaba" to mapOf(
                AppLanguage.ENGLISH to "Dhaba / Restaurant Style",
                AppLanguage.HINDI to "ढाबा / रेस्टोरेंट रिच",
                AppLanguage.HINGLISH to "Dhaba / Restaurant Style"
            ),
            // Scanner
            "camera_instructions" to mapOf(
                AppLanguage.ENGLISH to "Frame your plate inside the circle",
                AppLanguage.HINDI to "थाली को गोले के अंदर रखें",
                AppLanguage.HINGLISH to "Plate ko circle ke andar rakhein"
            ),
            "take_photo" to mapOf(
                AppLanguage.ENGLISH to "Capture Thali",
                AppLanguage.HINDI to "फोटो लें",
                AppLanguage.HINGLISH to "Photo Kheecho"
            ),
            "pick_gallery" to mapOf(
                AppLanguage.ENGLISH to "Upload from Gallery",
                AppLanguage.HINDI to "गैलरी से चुनें",
                AppLanguage.HINGLISH to "Gallery Se Upload"
            ),
            "try_demo" to mapOf(
                AppLanguage.ENGLISH to "Or try popular sample thali:",
                AppLanguage.HINDI to "या लोकप्रिय थाली टेस्ट करें:",
                AppLanguage.HINGLISH to "Ya demo sample thali test karein:"
            ),
            "scanning_loader" to mapOf(
                AppLanguage.ENGLISH to "Consulting ICMR-NIN Indian food database...",
                AppLanguage.HINDI to "ICMR-NIN भारतीय डेटाबेस से विश्लेषण हो रहा है...",
                AppLanguage.HINGLISH to "Tadka, roti aur portions analyze ho rahe hain..."
            ),
            // Results
            "result_title" to mapOf(
                AppLanguage.ENGLISH to "Meal Breakdown",
                AppLanguage.HINDI to "भोजन का विश्लेषण",
                AppLanguage.HINGLISH to "Thali Analysis Result"
            ),
            "sugar_spike_meter" to mapOf(
                AppLanguage.ENGLISH to "Sugar Spike Meter",
                AppLanguage.HINDI to "शुगर स्पाइक मीटर",
                AppLanguage.HINGLISH to "Sugar Spike Meter"
            ),
            "desi_health_fix" to mapOf(
                AppLanguage.ENGLISH to "Desi Health Fix",
                AppLanguage.HINDI to "देसी स्वास्थ्य उपाय",
                AppLanguage.HINGLISH to "Desi Health Fix"
            ),
            "portion_confirm" to mapOf(
                AppLanguage.ENGLISH to "Confirm / Adjust Portions",
                AppLanguage.HINDI to "मात्रा की पुष्टि या बदलाव करें",
                AppLanguage.HINGLISH to "Portion Adjust Karein"
            ),
            "disclaimer" to mapOf(
                AppLanguage.ENGLISH to "AI estimate based on ICMR-NIN standards. Not medical advice.",
                AppLanguage.HINDI to "ICMR-NIN मानकों पर आधारित AI अनुमान। चिकित्सीय सलाह नहीं।",
                AppLanguage.HINGLISH to "AI estimate, not medical advice. ICMR-NIN guidelines applied."
            ),
            "log_meal" to mapOf(
                AppLanguage.ENGLISH to "Log to Today's Diary",
                AppLanguage.HINDI to "आज की डायरी में जोड़ें",
                AppLanguage.HINGLISH to "Thali Log Karein"
            ),
            // Insights
            "weekly_report_title" to mapOf(
                AppLanguage.ENGLISH to "Weekly Nutritional Report Card",
                AppLanguage.HINDI to "साप्ताहिक पोषण रिपोर्ट कार्ड",
                AppLanguage.HINGLISH to "Weekly Report Card"
            ),
            "share_report" to mapOf(
                AppLanguage.ENGLISH to "Share Report Card",
                AppLanguage.HINDI to "कार्ड शेयर करें",
                AppLanguage.HINGLISH to "Share Report"
            )
        )
        return strings[key]?.get(lang) ?: strings[key]?.get(AppLanguage.ENGLISH) ?: key
    }
}
