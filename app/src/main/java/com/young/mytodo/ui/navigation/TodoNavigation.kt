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
                    // launchSingleTop으로 중복 네비게이션 방지
                    navController.navigate(NavRoutes.SETTINGS) {
                        launchSingleTop = true
                        // 현재 홈 화면을 백스택에 유지하면서 설정 화면으로 이동
                        restoreState = true
                    }
                },
            )
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                onBackClick = {
                    // popBackStack 대신 명시적으로 HOME으로 네비게이션
                    if (navController.currentDestination?.route != NavRoutes.HOME) {
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.HOME) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                },
                onNavigateToThemeSettings = {
                    navController.navigate(NavRoutes.SETTINGS_THEME) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(NavRoutes.SETTINGS_THEME) {
            SettingsThemeScreen(
                onBackClick = {
                    // 테마 설정에서는 설정 메인으로 돌아가기
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        // 백스택이 없다면 설정 메인으로 이동
                        navController.navigate(NavRoutes.SETTINGS) {
                            popUpTo(NavRoutes.HOME)
                            launchSingleTop = true
                        }
                    }
                },
                currentThemeMode = currentThemeMode,
                onThemeModeChanged = onThemeModeChanged,
            )
        }
    }
}