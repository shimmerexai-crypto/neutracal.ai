package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomTabBar
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.InsightsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileDrawerContent
import com.example.ui.screens.QuickAddDialog
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.ScanScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.NutracalTheme
import com.example.ui.theme.NutracalWhite
import com.example.viewmodel.NutracalViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutracalTheme {
                NutracalApp()
            }
        }
    }
}

@Composable
fun NutracalApp(viewModel: NutracalViewModel = viewModel()) {
    val language by viewModel.language.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()
    val isOnboarded by viewModel.isOnboarded.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
    val isQuickAddOpen by viewModel.isQuickAddOpen.collectAsState()
    val activeScanResult by viewModel.activeScanResult.collectAsState()

    var showResultScreen by remember { mutableStateOf(false) }

    // If an active scan result is ready, automatically show ResultScreen
    LaunchedEffect(activeScanResult) {
        if (activeScanResult != null) {
            showResultScreen = true
        }
    }

    val drawerState = rememberDrawerState(
        initialValue = if (isDrawerOpen) DrawerValue.Open else DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(isDrawerOpen) {
        if (isDrawerOpen) drawerState.open() else drawerState.close()
    }

    LaunchedEffect(drawerState.isOpen) {
        if (!drawerState.isOpen && isDrawerOpen) {
            viewModel.setDrawerOpen(false)
        }
    }

    // Back handling
    BackHandler(enabled = isDrawerOpen) {
        scope.launch {
            drawerState.close()
            viewModel.setDrawerOpen(false)
        }
    }

    BackHandler(enabled = showResultScreen) {
        showResultScreen = false
        viewModel.resetScanState()
    }

    BackHandler(enabled = !showResultScreen && currentTab != "home") {
        viewModel.setTab("home")
    }

    when {
        !isAuthenticated -> {
            AuthScreen(
                currentLanguage = language,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onLoginSuccess = { email, name ->
                    viewModel.login(email, name)
                },
                onExploreDemo = {
                    viewModel.login("demo@nutracal.ai", "Demo User")
                }
            )
        }
        !isOnboarded -> {
            OnboardingScreen(
                currentLanguage = language,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onComplete = { profile ->
                    viewModel.completeOnboarding(profile)
                }
            )
        }
        else -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ProfileDrawerContent(
                        viewModel = viewModel,
                        onClose = {
                            scope.launch {
                                drawerState.close()
                                viewModel.setDrawerOpen(false)
                            }
                        }
                    )
                }
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NutracalWhite),
                    bottomBar = {
                        if (!showResultScreen) {
                            BottomTabBar(
                                selectedTab = currentTab,
                                onTabSelected = { tab ->
                                    viewModel.setTab(tab)
                                },
                                language = language
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = Pair(currentTab, showResultScreen),
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "tab_content"
                        ) { (tab, isViewingResult) ->
                            if (isViewingResult) {
                                ResultScreen(
                                    viewModel = viewModel,
                                    onBack = {
                                        showResultScreen = false
                                        viewModel.resetScanState()
                                    }
                                )
                            } else {
                                when (tab) {
                                    "home" -> DashboardScreen(
                                        viewModel = viewModel,
                                        onNavigateToScan = { viewModel.setTab("scan") }
                                    )
                                    "search" -> SearchScreen(viewModel = viewModel)
                                    "scan" -> ScanScreen(
                                        viewModel = viewModel,
                                        onScanSuccess = {
                                            showResultScreen = true
                                        }
                                    )
                                    "history" -> HistoryScreen(viewModel = viewModel)
                                    "insights" -> InsightsScreen(viewModel = viewModel)
                                    "settings" -> SettingsScreen(viewModel = viewModel)
                                    else -> DashboardScreen(
                                        viewModel = viewModel,
                                        onNavigateToScan = { viewModel.setTab("scan") }
                                    )
                                }
                            }
                        }

                        // Quick Add Dialog (pre-selects active meal category)
                        val activeMealType by viewModel.activeMealTypeForAdd.collectAsState()
                        if (isQuickAddOpen) {
                            QuickAddDialog(
                                language = language,
                                initialMealType = activeMealType,
                                onDismiss = { viewModel.setQuickAddOpen(false) },
                                onAddFood = { food, mealType, portion ->
                                    viewModel.quickAddPresetFood(food, mealType, portion)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
