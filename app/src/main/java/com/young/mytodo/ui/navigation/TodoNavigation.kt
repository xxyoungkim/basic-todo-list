package com.young.mytodo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.young.mytodo.ui.main.HomeScreen
import com.young.mytodo.ui.main.MainViewModel
import com.young.mytodo.ui.settings.SettingsScreen
import com.young.mytodo.ui.settings.SettingsThemeScreen
import com.young.mytodo.ui.settings.util.ThemeMode

// 네비게이션 경로 정의
object NavRoutes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val SETTINGS_THEME = "settings_theme"
    const val SETTINGS_EXPORT = "settings_export"
}

@Composable
fun TodoNavigation(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
    currentThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSettings = {
                    navController.navigate(NavRoutes.SETTINGS)
                },
            )
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToThemeSettings = {
                    navController.navigate(NavRoutes.SETTINGS_THEME)
                },
            )
        }

        composable(NavRoutes.SETTINGS_THEME) {
            SettingsThemeScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                currentThemeMode = currentThemeMode,
                onThemeModeChanged = onThemeModeChanged,
            )
        }
    }
}