package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.IndianFoodDatabase
import com.example.data.PresetFood
import com.example.data.local.MealEntity
import com.example.data.local.NutracalDatabase
import com.example.data.local.WeightEntity
import com.example.model.AppLanguage
import com.example.model.CookingStyle
import com.example.model.FoodItem
import com.example.model.FitnessGoal
import com.example.model.Gender
import com.example.model.MealType
import com.example.model.ScanResult
import com.example.model.UserProfile
import com.example.service.GeminiFoodService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ScanUiState {
    object Idle : ScanUiState
    data class Loading(val message: String) : ScanUiState
    data class Success(val result: ScanResult) : ScanUiState
    data class Error(val message: String) : ScanUiState
}

class NutracalViewModel(application: Application) : AndroidViewModel(application) {

    private val db = NutracalDatabase.getDatabase(application)
    private val mealDao = db.mealDao()
    private val weightDao = db.weightDao()
    private val geminiService = GeminiFoodService()

    private val _language = MutableStateFlow(AppLanguage.HINGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(true)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _isOnboarded = MutableStateFlow(true)
    val isOnboarded: StateFlow<Boolean> = _isOnboarded.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "Aarav Sharma",
            email = "aarav@example.com",
            gender = Gender.MALE,
            age = 27,
            heightCm = 175f,
            weightKg = 70f,
            startingWeightKg = 74.5f,
            targetWeightKg = 66f,
            weeklyPaceKg = -0.5f,
            source = "Instagram",
            goal = FitnessGoal.WEIGHT_LOSS,
            dailyCalorieTarget = 1950,
            dailyProteinTarget = 95,
            dailyCarbsTarget = 210,
            dailyFatTarget = 52,
            carbsPercent = 45,
            proteinPercent = 25,
            fatPercent = 30,
            dailyWaterGlassesTarget = 10,
            streakDays = 6,
            exerciseCaloriesBurnedToday = 260,
            stepCountToday = 7240,
            stepGoal = 10000,
            useMetricUnits = true
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _currentTab = MutableStateFlow("home") // "home", "search", "scan", "insights", "settings"
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    private val _ringMode = MutableStateFlow("detailed") // "simple" or "detailed"
    val ringMode: StateFlow<String> = _ringMode.asStateFlow()

    private val _waterGlasses = MutableStateFlow(7)
    val waterGlasses: StateFlow<Int> = _waterGlasses.asStateFlow()

    private val _cookingStyle = MutableStateFlow(CookingStyle.HOME_NORMAL)
    val cookingStyle: StateFlow<CookingStyle> = _cookingStyle.asStateFlow()

    private val _scanState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val scanState: StateFlow<ScanUiState> = _scanState.asStateFlow()

    private val _activeScanResult = MutableStateFlow<ScanResult?>(null)
    val activeScanResult: StateFlow<ScanResult?> = _activeScanResult.asStateFlow()

    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    private val _isQuickAddOpen = MutableStateFlow(false)
    val isQuickAddOpen: StateFlow<Boolean> = _isQuickAddOpen.asStateFlow()

    private val _activeMealTypeForAdd = MutableStateFlow(MealType.LUNCH)
    val activeMealTypeForAdd: StateFlow<MealType> = _activeMealTypeForAdd.asStateFlow()

    val allMeals: StateFlow<List<MealEntity>> = mealDao.getAllMeals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWeightLogs: StateFlow<List<WeightEntity>> = weightDao.getAllWeightLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            populateInitialSampleDataIfEmpty()
        }
    }

    private suspend fun populateInitialSampleDataIfEmpty() {
        val now = System.currentTimeMillis()
        val morning = now - 5 * 3600 * 1000
        val noon = now - 2 * 3600 * 1000

        // Sample breakfast & lunch
        val sampleBreakfast = MealEntity(
            mealTitle = "Poha with Roasted Peanuts & Lemon",
            mealType = MealType.BREAKFAST.name,
            timestamp = morning,
            calories = 240,
            proteinG = 5.8f,
            carbsG = 38.0f,
            fatG = 7.2f,
            glycemicLevel = "MEDIUM",
            cookingStyle = CookingStyle.HOME_NORMAL.name,
            itemsSummary = "Poha (1 plate), Peanuts, Lemon",
            spikeExplanation = "Moderate glycemic response buffered by peanuts and curry leaves.",
            desiHealthFix = "Add a boiled egg or 50g paneer cubes to boost breakfast protein."
        )

        val sampleLunch = MealEntity(
            mealTitle = "Bajre Ki Roti & Gatte Ki Sabzi",
            mealType = MealType.LUNCH.name,
            timestamp = noon,
            calories = 420,
            proteinG = 13.5f,
            carbsG = 52.0f,
            fatG = 14.5f,
            glycemicLevel = "LOW",
            cookingStyle = CookingStyle.HOME_NORMAL.name,
            itemsSummary = "Bajre ki Roti (2 pcs), Gatte ki Sabzi (1 katori), Chaas (1 glass)",
            spikeExplanation = "High millet fiber keeps post-prandial glucose stable.",
            desiHealthFix = "Sip a glass of chilled jeera chaas to assist gut motility."
        )

        mealDao.insertMeal(sampleBreakfast)
        mealDao.insertMeal(sampleLunch)

        // Sample weight logs (4-week progression)
        val oneDay = 24L * 3600 * 1000
        val sampleWeights = listOf(
            WeightEntity(timestamp = now - 28 * oneDay, weightKg = 74.2f, note = "Starting weight"),
            WeightEntity(timestamp = now - 21 * oneDay, weightKg = 73.1f, note = "Week 1 check-in"),
            WeightEntity(timestamp = now - 14 * oneDay, weightKg = 72.0f, note = "Consistent diet"),
            WeightEntity(timestamp = now - 7 * oneDay, weightKg = 71.0f, note = "Target in sight"),
            WeightEntity(timestamp = now, weightKg = 70.0f, note = "Today's check-in")
        )
        sampleWeights.forEach { weightDao.insertWeight(it) }
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    fun toggleRingMode() {
        _ringMode.value = if (_ringMode.value == "simple") "detailed" else "simple"
    }

    fun setCookingStyle(style: CookingStyle) {
        _cookingStyle.value = style
    }

    fun setDrawerOpen(open: Boolean) {
        _isDrawerOpen.value = open
    }

    fun setQuickAddOpen(open: Boolean) {
        _isQuickAddOpen.value = open
    }

    fun openQuickAddForMeal(mealType: MealType) {
        _activeMealTypeForAdd.value = mealType
        _isQuickAddOpen.value = true
    }

    fun addWater() {
        if (_waterGlasses.value < 20) {
            _waterGlasses.value += 1
        }
    }

    fun resetWater() {
        _waterGlasses.value = 0
    }

    fun logWeight(weightKg: Float, note: String = "") {
        viewModelScope.launch {
            val record = WeightEntity(
                timestamp = System.currentTimeMillis(),
                weightKg = weightKg,
                note = note
            )
            weightDao.insertWeight(record)
            _userProfile.value = _userProfile.value.copy(weightKg = weightKg)
        }
    }

    fun updateNutritionGoals(
        dailyCalories: Int,
        proteinG: Int,
        carbsG: Int,
        fatG: Int,
        carbsPct: Int,
        proteinPct: Int,
        fatPct: Int
    ) {
        _userProfile.value = _userProfile.value.copy(
            dailyCalorieTarget = dailyCalories,
            dailyProteinTarget = proteinG,
            dailyCarbsTarget = carbsG,
            dailyFatTarget = fatG,
            carbsPercent = carbsPct,
            proteinPercent = proteinPct,
            fatPercent = fatPct
        )
    }

    fun updateWeightGoals(
        startingKg: Float,
        currentKg: Float,
        targetKg: Float,
        weeklyPaceKg: Float
    ) {
        _userProfile.value = _userProfile.value.copy(
            startingWeightKg = startingKg,
            weightKg = currentKg,
            targetWeightKg = targetKg,
            weeklyPaceKg = weeklyPaceKg
        )
    }

    fun login(email: String, name: String) {
        _userProfile.value = _userProfile.value.copy(
            email = email,
            name = if (name.isNotBlank()) name else "Aarav Sharma"
        )
        _isAuthenticated.value = true
    }

    fun completeOnboarding(profile: UserProfile) {
        _userProfile.value = profile
        _isOnboarded.value = true
        _isAuthenticated.value = true
    }

    fun logout() {
        _isAuthenticated.value = false
    }

    fun clearDiary() {
        viewModelScope.launch {
            mealDao.clearAll()
            _waterGlasses.value = 0
        }
    }

    fun startFoodScan(bitmap: Bitmap?, dishHint: String? = null) {
        _scanState.value = ScanUiState.Loading("Consulting ICMR-NIN Indian food database...")
        viewModelScope.launch {
            try {
                val result = geminiService.analyzeFoodImage(
                    bitmap = bitmap,
                    cookingStyle = _cookingStyle.value,
                    language = _language.value,
                    dishHint = dishHint
                )
                _activeScanResult.value = result
                _scanState.value = ScanUiState.Success(result)
            } catch (e: Exception) {
                _scanState.value = ScanUiState.Error(e.message ?: "Failed to analyze meal")
            }
        }
    }

    fun resetScanState() {
        _scanState.value = ScanUiState.Idle
    }

    fun updateItemPortion(itemIndex: Int, newPortionScale: Float) {
        val current = _activeScanResult.value ?: return
        if (itemIndex !in current.items.indices) return

        val updatedItems = current.items.toMutableList()
        val old = updatedItems[itemIndex]
        updatedItems[itemIndex] = old.copy(portionScale = newPortionScale)

        val newTotalCal = updatedItems.sumOf { it.calories }
        val newProtein = updatedItems.sumOf { it.proteinG.toDouble() }.toFloat()
        val newCarbs = updatedItems.sumOf { it.carbsG.toDouble() }.toFloat()
        val newFat = updatedItems.sumOf { it.fatG.toDouble() }.toFloat()

        _activeScanResult.value = current.copy(
            items = updatedItems,
            totalCalories = newTotalCal,
            totalProteinG = newProtein,
            totalCarbsG = newCarbs,
            totalFatG = newFat
        )
    }

    fun logCurrentScanResult(mealType: MealType) {
        val result = _activeScanResult.value ?: return
        viewModelScope.launch {
            val title = result.items.joinToString(" + ") { it.name }
            val meal = MealEntity(
                mealTitle = if (title.length > 55) title.take(52) + "..." else title,
                mealType = mealType.name,
                timestamp = System.currentTimeMillis(),
                calories = result.totalCalories,
                proteinG = result.totalProteinG,
                carbsG = result.totalCarbsG,
                fatG = result.totalFatG,
                glycemicLevel = result.glycemicLoadLevel.name,
                cookingStyle = result.cookingStyle.name,
                itemsSummary = result.items.joinToString(", ") { "${it.name} (${(it.portionScale * 100).toInt()}%)" },
                spikeExplanation = result.spikeExplanation,
                desiHealthFix = result.desiHealthFix,
                thumbnailBase64 = result.imageBase64
            )
            mealDao.insertMeal(meal)
            _activeScanResult.value = null
            _scanState.value = ScanUiState.Idle
            _currentTab.value = "home"
        }
    }

    fun quickAddPresetFood(preset: PresetFood, mealType: MealType, portion: Float = 1.0f) {
        viewModelScope.launch {
            val multCal = (preset.calories * portion).toInt()
            val multProtein = preset.protein * portion
            val multCarbs = preset.carbs * portion
            val multFat = preset.fat * portion

            val meal = MealEntity(
                mealTitle = preset.nameEn,
                mealType = mealType.name,
                timestamp = System.currentTimeMillis(),
                calories = multCal,
                proteinG = multProtein,
                carbsG = multCarbs,
                fatG = multFat,
                glycemicLevel = preset.glycemicLevel.name,
                cookingStyle = CookingStyle.HOME_NORMAL.name,
                itemsSummary = "${preset.servingDesc} (x$portion)",
                spikeExplanation = "Directly logged from verified ICMR-NIN database.",
                desiHealthFix = preset.defaultHealthFix
            )
            mealDao.insertMeal(meal)
            _isQuickAddOpen.value = false
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            mealDao.deleteMeal(meal)
        }
    }
}
