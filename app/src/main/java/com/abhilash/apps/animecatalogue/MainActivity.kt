package com.abhilash.apps.animecatalogue

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abhilash.apps.animecatalogue.view.screens.AnimeDetailScreen
import com.abhilash.apps.animecatalogue.view.screens.HomeScreen
import com.abhilash.apps.animecatalogue.view.theme.AnimeCatalogueTheme
import com.abhilash.apps.animecatalogue.view.theme.LocalNavigateToLambda


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.statusBarColor = Color.WHITE
            window.isStatusBarContrastEnforced = false
        }
        setContent {
            AnimeCatalogueTheme {
                AnimeCatalogueNavigator()
            }
        }
    }
}

@Composable
fun AnimeCatalogueNavigator() {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavigateToLambda provides { screen, args -> navController.navigate("$screen/$args") }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.name //"${Screen.DETAIL.name}/44081"
        ) {
            composable(Screen.HOME.name) {
                HomeScreen()
            }

            composable(
                route = "${Screen.DETAIL.name}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val animeId = it.arguments?.getString("id") ?: "44081"
                AnimeDetailScreen(animeId = animeId)
            }
        }
    }
}

enum class Screen {
    HOME,
    DETAIL
}