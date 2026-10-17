package com.example.model

enum class AppLanguage(val code: String, val label: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिंदी"),
    HINGLISH("hinglish", "Hinglish")
}

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other")
}

enum class FitnessGoal(val titleEn: String, val titleHi: String, val titleHinglish: String) {
    WEIGHT_LOSS("Weight Loss", "वजन घटाएं", "Weight Loss / Fat Burn"),
    MUSCLE_GAIN("Muscle Gain", "मसल और ताकत", "Muscle Gain / High Protein"),
    MAINTAIN_SUGAR("Sugar Control & Health", "शुगर नियंत्रण और स्वास्थ्य", "Sugar Control & Active Lifestyle")
}

enum class CookingStyle(
    val titleEn: String,
    val titleHi: String,
    val titleHinglish: String,
    val oilNote: String,
    val calorieMultiplier: Float,
    val fatMultiplier: Float
) {
    HOME_NORMAL(
        "Home Normal",
        "घर का सादा खाना",
        "Ghar Ka Khana",
        "1 tsp oil/ghee",
        1.0f,
        1.0f
    ),
    EXTRA_GHEE(
        "Extra Ghee",
        "शुद्ध देसी घी",
        "Extra Ghee / Makhan",
        "+1.5 tbsp pure desi ghee",
        1.25f,
        1.45f
    ),
    DHABA_STYLE(
        "Dhaba / Restaurant",
        "ढाबा / रेस्टोरेंट",
        "Dhaba Style Rich Tadka",
        "Heavy tadka, cream & butter",
        1.35f,
        1.60f
    )
}

enum class GlycemicLevel(val labelEn: String, val labelHi: String, val labelHinglish: String) {
    LOW("Low Spike", "कम स्पाइक (सुरक्षित)", "Low Sugar Spike"),
    MEDIUM("Moderate Spike", "मध्यम स्पाइक", "Medium Spike (Safe with walk)"),
    HIGH("High Spike", "तीव्र स्पाइक", "High Spike (Watch portion)")
}

data class FoodItem(
    val name: String,
    val estimatedQuantity: String,
    val baseCalories: Int,
    val baseProteinG: Float,
    val baseCarbsG: Float,
    val baseFatG: Float,
    val portionScale: Float = 1.0f
) {
    val calories: Int get() = (baseCalories * portionScale).toInt()
    val proteinG: Float get() = ((baseProteinG * portionScale * 10).toInt()) / 10f
    val carbsG: Float get() = ((baseCarbsG * portionScale * 10).toInt()) / 10f
    val fatG: Float get() = ((baseFatG * portionScale * 10).toInt()) / 10f
}

data class ScanResult(
    val items: List<FoodItem>,
    val totalCalories: Int,
    val totalProteinG: Float,
    val totalCarbsG: Float,
    val totalFatG: Float,
    val glycemicLoadLevel: GlycemicLevel,
    val spikeExplanation: String,
    val desiHealthFix: String,
    val confidence: Int = 92,
    val cookingStyle: CookingStyle = CookingStyle.HOME_NORMAL,
    val imageBase64: String? = null
)

enum class MealType(val labelEn: String, val labelHi: String, val labelHinglish: String, val iconName: String) {
    BREAKFAST("Breakfast", "नाश्ता", "Breakfast", "wb_twilight"),
    LUNCH("Lunch", "दोपहर का खाना", "Lunch / Thali", "wb_sunny"),
    SNACKS("Snacks", "शाम का नाश्ता", "Evening Snack / Chai", "bakery_dining"),
    DINNER("Dinner", "रात का खाना", "Dinner", "nights_stay")
}

data class WeightRecord(
    val id: Long = 0,
    val timestamp: Long,
    val weightKg: Float,
    val note: String = ""
)

data class UserProfile(
    val name: String = "User",
    val email: String = "aarav@example.com",
    val gender: Gender = Gender.MALE,
    val age: Int = 26,
    val heightCm: Float = 172f,
    val weightKg: Float = 70f,
    val startingWeightKg: Float = 74f,
    val targetWeightKg: Float = 66f,
    val weeklyPaceKg: Float = -0.5f,
    val source: String = "Instagram",
    val goal: FitnessGoal = FitnessGoal.WEIGHT_LOSS,
    val dailyCalorieTarget: Int = 1950,
    val dailyProteinTarget: Int = 95,
    val dailyCarbsTarget: Int = 210,
    val dailyFatTarget: Int = 50,
    val carbsPercent: Int = 45,
    val proteinPercent: Int = 25,
    val fatPercent: Int = 30,
    val dailyWaterGlassesTarget: Int = 10,
    val streakDays: Int = 5,
    val exerciseCaloriesBurnedToday: Int = 240,
    val stepCountToday: Int = 6840,
    val stepGoal: Int = 10000,
    val useMetricUnits: Boolean = true
) {
    val bmi: Float
        get() {
            val heightM = heightCm / 100f
            return if (heightM > 0) ((weightKg / (heightM * heightM)) * 10).toInt() / 10f else 22.0f
        }

    val bmiCategory: String
        get() = when {
            bmi < 18.5f -> "Underweight"
            bmi < 24.9f -> "Normal (Healthy)"
            bmi < 29.9f -> "Overweight"
            else -> "Obese"
        }
}
