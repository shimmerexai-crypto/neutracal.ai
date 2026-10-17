package com.example.data

import com.example.model.FoodItem
import com.example.model.GlycemicLevel

data class PresetFood(
    val nameEn: String,
    val nameHi: String,
    val nameHinglish: String,
    val servingDesc: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val glycemicLevel: GlycemicLevel,
    val category: String,
    val defaultHealthFix: String
)

object IndianFoodDatabase {
    val items = listOf(
        PresetFood(
            nameEn = "Bajre Ki Roti",
            nameHi = "बाजरे की रोटी",
            nameHinglish = "Bajre Ki Roti",
            servingDesc = "1 medium (45g flour)",
            calories = 115,
            protein = 3.2f,
            carbs = 22.0f,
            fat = 1.4f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Breads & Roti",
            defaultHealthFix = "Rich in dietary fiber and magnesium. Pair with roasted garlic lasun chutney or green sabzi."
        ),
        PresetFood(
            nameEn = "Gatte Ki Sabzi",
            nameHi = "गट्टे की सब्जी",
            nameHinglish = "Gatte Ki Sabzi",
            servingDesc = "1 katori (150g curd gravy)",
            calories = 185,
            protein = 6.8f,
            carbs = 14.5f,
            fat = 11.2f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Sabzi & Curries",
            defaultHealthFix = "Gram flour (besan) provides slow-release complex carbs. Enjoy with cucumber salad."
        ),
        PresetFood(
            nameEn = "Dal Baati Churma",
            nameHi = "दाल बाटी चूरमा",
            nameHinglish = "Dal Baati Churma",
            servingDesc = "2 baatis + 1 katori dal + 2 tbsp churma",
            calories = 580,
            protein = 16.5f,
            carbs = 76.0f,
            fat = 24.0f,
            glycemicLevel = GlycemicLevel.HIGH,
            category = "Traditional Thali",
            defaultHealthFix = "High calorie festive meal. Take a 20-minute gentle brisk walk post meal to blunt glucose spikes."
        ),
        PresetFood(
            nameEn = "Ker Sangri",
            nameHi = "केर सांगरी",
            nameHinglish = "Ker Sangri",
            servingDesc = "1 small katori (100g)",
            calories = 145,
            protein = 4.2f,
            carbs = 12.0f,
            fat = 8.5f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Rajasthani Specialty",
            defaultHealthFix = "Desert wild berries and pods packed with polyphenols and gut-friendly prebiotic fiber."
        ),
        PresetFood(
            nameEn = "Chapati / Phulka",
            nameHi = "गेहूं की रोटी / फुल्का",
            nameHinglish = "Ghar Ki Roti / Phulka",
            servingDesc = "1 medium without ghee",
            calories = 85,
            protein = 3.0f,
            carbs = 17.5f,
            fat = 0.6f,
            glycemicLevel = GlycemicLevel.MEDIUM,
            category = "Breads & Roti",
            defaultHealthFix = "Eat with plenty of raw salad first to reduce the overall glycemic response."
        ),
        PresetFood(
            nameEn = "Yellow Dal Tadka",
            nameHi = "पीली दाल तड़का",
            nameHinglish = "Yellow Dal Tadka",
            servingDesc = "1 medium katori (150g)",
            calories = 140,
            protein = 8.2f,
            carbs = 18.0f,
            fat = 4.2f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Dals & Legumes",
            defaultHealthFix = "Jeera and hing tadka relieves bloating and enhances mineral absorption."
        ),
        PresetFood(
            nameEn = "Dal Makhani",
            nameHi = "दाल मखनी",
            nameHinglish = "Dal Makhani",
            servingDesc = "1 katori (150g)",
            calories = 260,
            protein = 9.5f,
            carbs = 24.0f,
            fat = 14.5f,
            glycemicLevel = GlycemicLevel.MEDIUM,
            category = "Dals & Legumes",
            defaultHealthFix = "Substitute heavy cream with homemade hung curd or skimmed milk for lighter fat."
        ),
        PresetFood(
            nameEn = "Paneer Butter Masala",
            nameHi = "पनीर बटर मसाला",
            nameHinglish = "Paneer Butter Masala",
            servingDesc = "1 katori (150g, 4-5 cubes)",
            calories = 310,
            protein = 13.5f,
            carbs = 12.0f,
            fat = 23.5f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Sabzi & Curries",
            defaultHealthFix = "High quality dairy protein with low carb content. Pair with whole wheat roti."
        ),
        PresetFood(
            nameEn = "Palak Paneer",
            nameHi = "पालक पनीर",
            nameHinglish = "Palak Paneer",
            servingDesc = "1 katori (160g)",
            calories = 210,
            protein = 12.0f,
            carbs = 8.5f,
            fat = 14.0f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Sabzi & Curries",
            defaultHealthFix = "Rich in iron and dietary calcium. Squeeze fresh lemon to maximize non-heme iron absorption."
        ),
        PresetFood(
            nameEn = "Steamed Basmati Rice",
            nameHi = "उबले चावल",
            nameHinglish = "Steamed White Rice",
            servingDesc = "1 katori (130g cooked)",
            calories = 160,
            protein = 3.2f,
            carbs = 35.0f,
            fat = 0.4f,
            glycemicLevel = GlycemicLevel.HIGH,
            category = "Rice & Grains",
            defaultHealthFix = "Combine 1:2 ratio of rice to thick dal or curd to slow down the sugar spike."
        ),
        PresetFood(
            nameEn = "Vegetable Biryani",
            nameHi = "वेज बिरयानी",
            nameHinglish = "Veg Biryani with Raita",
            servingDesc = "1 medium plate (220g)",
            calories = 340,
            protein = 7.5f,
            carbs = 54.0f,
            fat = 10.5f,
            glycemicLevel = GlycemicLevel.MEDIUM,
            category = "Rice & Grains",
            defaultHealthFix = "Always pair with cucumber or onion raita to buffer insulin surge."
        ),
        PresetFood(
            nameEn = "Chicken Biryani",
            nameHi = "चिकन बिरयानी",
            nameHinglish = "Chicken Biryani",
            servingDesc = "1 plate (250g with 2 pcs)",
            calories = 430,
            protein = 24.5f,
            carbs = 48.0f,
            fat = 14.0f,
            glycemicLevel = GlycemicLevel.MEDIUM,
            category = "Non-Veg",
            defaultHealthFix = "High protein meal! Choose breast pieces and enjoy with mint coriander chutney."
        ),
        PresetFood(
            nameEn = "Idli Sambar",
            nameHi = "इडली सांभर",
            nameHinglish = "Idli Sambar (2 idlis)",
            servingDesc = "2 steamed idlis + 1 bowl sambar",
            calories = 175,
            protein = 6.8f,
            carbs = 33.0f,
            fat = 1.8f,
            glycemicLevel = GlycemicLevel.MEDIUM,
            category = "Breakfast & South Indian",
            defaultHealthFix = "Fermented batter supports gut microbiome. Add extra drumstick and pumpkin in sambar."
        ),
        PresetFood(
            nameEn = "Masala Dosa",
            nameHi = "मसाला डोसा",
            nameHinglish = "Masala Dosa with Chutney",
            servingDesc = "1 medium dosa with aloo masala",
            calories = 295,
            protein = 5.8f,
            carbs = 44.0f,
            fat = 10.8f,
            glycemicLevel = GlycemicLevel.HIGH,
            category = "Breakfast & South Indian",
            defaultHealthFix = "Dip in sambar rather than sweet coconut chutney to maintain glycemic balance."
        ),
        PresetFood(
            nameEn = "Poha",
            nameHi = "पोहा",
            nameHinglish = "Kanda Batata Poha",
            servingDesc = "1 medium plate (150g with peanuts)",
            calories = 220,
            protein = 5.2f,
            carbs = 36.0f,
            fat = 6.4f,
            glycemicLevel = GlycemicLevel.MEDIUM,
            category = "Breakfast",
            defaultHealthFix = "Roasted peanuts add good fats and protein. Add extra grated carrots or boiled peas."
        ),
        PresetFood(
            nameEn = "Besan Chilla",
            nameHi = "बेसन चीला",
            nameHinglish = "Besan Veg Chilla",
            servingDesc = "1 large chilla with veggies",
            calories = 145,
            protein = 7.5f,
            carbs = 18.0f,
            fat = 4.8f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Breakfast & Snacks",
            defaultHealthFix = "Excellent low-GI breakfast choice. Add paneer filling for double protein."
        ),
        PresetFood(
            nameEn = "Rajma Chawal",
            nameHi = "राजमा चावल",
            nameHinglish = "Rajma Chawal Comfort Bowl",
            servingDesc = "1 bowl (100g rice + 150g rajma)",
            calories = 360,
            protein = 13.0f,
            carbs = 62.0f,
            fat = 5.5f,
            glycemicLevel = GlycemicLevel.MEDIUM,
            category = "North Indian Meals",
            defaultHealthFix = "Kidney beans contain soluble fiber. Squeeze fresh lemon juice for vitamin C."
        ),
        PresetFood(
            nameEn = "Chole Bhature",
            nameHi = "छोले भटूरे",
            nameHinglish = "Chole Bhature (1 bhatura)",
            servingDesc = "1 bhatura + 1 katori chole",
            calories = 490,
            protein = 12.0f,
            carbs = 58.0f,
            fat = 23.0f,
            glycemicLevel = GlycemicLevel.HIGH,
            category = "North Indian Meals",
            defaultHealthFix = "Enjoy mindfully! Eat pickled ginger and onion salad first to stimulate digestion."
        ),
        PresetFood(
            nameEn = "Spiced Chaas",
            nameHi = "मसाला छाछ",
            nameHinglish = "Spiced Chaas (Buttermilk)",
            servingDesc = "1 glass (250ml) with roasted jeera",
            calories = 55,
            protein = 3.2f,
            carbs = 5.0f,
            fat = 2.2f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Beverages & Dairy",
            defaultHealthFix = "Ideal natural probiotic cooler. Roasted cumin and rock salt eliminate bloating."
        ),
        PresetFood(
            nameEn = "Moong Dal Khichdi",
            nameHi = "मूँग दाल खिचड़ी",
            nameHinglish = "Moong Dal Khichdi with Ghee",
            servingDesc = "1 bowl (180g with 1 tsp ghee)",
            calories = 240,
            protein = 9.8f,
            carbs = 38.0f,
            fat = 5.2f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Comfort Food",
            defaultHealthFix = "Complete amino acid profile. Gentle on digestion, ideal for light dinners."
        ),
        PresetFood(
            nameEn = "Mixed Sprouts Salad",
            nameHi = "अंकुरित मूंग सलाद",
            nameHinglish = "Sprouts Salad with Lemon",
            servingDesc = "1 medium bowl (120g)",
            calories = 110,
            protein = 8.5f,
            carbs = 16.0f,
            fat = 1.0f,
            glycemicLevel = GlycemicLevel.LOW,
            category = "Salads & Snacks",
            defaultHealthFix = "Sprouting multiplies active B-vitamins and enzymes. Blunts glucose absorption."
        )
    )

    fun search(query: String): List<PresetFood> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return items.take(8)
        return items.filter {
            it.nameEn.lowercase().contains(q) ||
            it.nameHi.lowercase().contains(q) ||
            it.nameHinglish.lowercase().contains(q) ||
            it.category.lowercase().contains(q)
        }
    }
}
