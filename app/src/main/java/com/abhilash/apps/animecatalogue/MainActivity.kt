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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStoreFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abhilash.apps.animecatalogue.model.datastore.LoginMode
import com.abhilash.apps.animecatalogue.view.screens.AnimeDetailScreen
import com.abhilash.apps.animecatalogue.view.screens.HomeScreen
import com.abhilash.apps.animecatalogue.view.screens.LoginScreen
import com.abhilash.apps.animecatalogue.view.theme.AnimeCatalogueTheme
import com.abhilash.apps.animecatalogue.view.theme.LocalNavigateToLambda
import com.abhilash.apps.animecatalogue.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            AnimeCatalogueTheme {
                AnimeCatalogue()
            }
        }
    }
}

@Composable
fun AnimeCatalogueNavigator(
    navController: NavHostController,
    startScreen: Screen,
    changeLoginMethod: (LoginMode) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startScreen.name
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

        composable(Screen.LOGIN.name) {
            LoginScreen(
                navigateToGuest = {
                    changeLoginMethod(LoginMode.GUEST)
                    navController.navigate(Screen.HOME.name) {
                        this.popUpTo(Screen.LOGIN.name)
                    }
                },
                navigateToUserLogin = {
                    changeLoginMethod(LoginMode.USER_LOGIN)
                }
            )
        }

    }
}

@Composable
fun AnimeCatalogue() {
    val navController = rememberNavController()
    val viewModel = viewModel<LoginViewModel>()
    val loginMode = viewModel.getLoginMode.collectAsState(initial = LoginMode.UNKNOWN)

    AnimeProviders(navController = navController) {
        when(loginMode.value) {
            LoginMode.UNKNOWN -> {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "To Insert Logo",
                        )
                    }
                }
            }
            else -> {
                AnimeCatalogueNavigator(
                    navController = navController,
                    startScreen = when(loginMode.value) {
                        LoginMode.GUEST -> Screen.HOME
                        LoginMode.USER_LOGIN -> Screen.HOME
                        else -> Screen.LOGIN
                    }
                ) {
                    viewModel.setLoginMode(it)
                }
            }
        }
    }
}

@Composable
fun AnimeProviders(
    navController: NavController,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNavigateToLambda provides { screen, args ->
            if(args.isEmpty()) {
                navController.navigate(screen)
            } else {
                navController.navigate("$screen/$args")
            }
        }
    ) {
        content()
    }
}

enum class Screen {
    LOGIN,
    HOME,
    DETAIL
}