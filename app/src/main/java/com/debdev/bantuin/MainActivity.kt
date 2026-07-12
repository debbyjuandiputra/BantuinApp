package com.debdev.bantuin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.debdev.bantuin.auth.AuthManager
import com.debdev.bantuin.model.featureList
import com.debdev.bantuin.ui.screens.*
import com.debdev.bantuin.ui.screens.features.*
import com.debdev.bantuin.ui.theme.BantuinTheme
import com.debdev.bantuin.ui.theme.ThemeManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authManager = AuthManager(this)
        val themeManager = ThemeManager(this)

        setContent {
            var isDarkMode by remember { mutableStateOf(themeManager.isDarkMode()) }

            BantuinTheme(isDarkMode = isDarkMode) {
                val navController = rememberNavController()
                val startDestination = if (authManager.isLoggedIn()) "dashboard" else "login"

                NavHost(navController = navController, startDestination = startDestination) {

                    composable("login") {
                        LoginScreen(
                            authManager = authManager,
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onGoToRegister = { navController.navigate("register") }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            authManager = authManager,
                            onRegisterSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onGoToLogin = { navController.popBackStack() },
                            onOpenPolicy = { navController.navigate("policy") }
                        )
                    }

                    composable("dashboard") {
                        DashboardScreen(
                            isDarkMode = isDarkMode,
                            onToggleMode = {
                                isDarkMode = themeManager.toggle()
                            },
                            onOpenPolicy = { navController.navigate("policy") },
                            onViewProfile = { navController.navigate("profile") },
                            onEditProfile = { navController.navigate("edit_profile") },
                            onLogout = {
                                authManager.logout()
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onFeatureClick = { feature ->
                                navController.navigate(feature.route)
                            }
                        )
                    }

                    composable("policy") {
                        PolicyScreen(onBack = { navController.popBackStack() })
                    }

                    composable("profile") {
                        ProfileScreen(
                            authManager = authManager,
                            onBack = { navController.popBackStack() },
                            onEdit = { navController.navigate("edit_profile") }
                        )
                    }

                    composable("edit_profile") {
                        EditProfileScreen(
                            authManager = authManager,
                            onBack = { navController.popBackStack() },
                            onSaved = { navController.popBackStack() }
                        )
                    }

                    // ----- Fitur -----
                    composable("convert_document") {
                        ConvertDocumentScreen(onBack = { navController.popBackStack() })
                    }
                    composable("premium_app") {
                        PremiumScreen(onBack = { navController.popBackStack() })
                    }
                    composable("char_counter") {
                        CharCounterScreen(onBack = { navController.popBackStack() })
                    }
                    composable("uuid_generator") {
                        UuidGeneratorScreen(onBack = { navController.popBackStack() })
                    }
                    composable("authenticator") {
                        AuthenticatorScreen(onBack = { navController.popBackStack() })
                    }
                    composable("base64") {
                        Base64Screen(onBack = { navController.popBackStack() })
                    }
                    composable("url_encode") {
                        UrlEncodeScreen(onBack = { navController.popBackStack() })
                    }
                    composable("calc_scientific") {
                        ScientificCalculatorScreen(onBack = { navController.popBackStack() })
                    }
                    composable("calc_statistic") {
                        StatisticalCalculatorScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
