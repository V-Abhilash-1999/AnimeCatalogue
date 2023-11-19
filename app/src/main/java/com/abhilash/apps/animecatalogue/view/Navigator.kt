package com.abhilash.apps.animecatalogue.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abhilash.apps.animecatalogue.model.datastore.LoginMode
import com.abhilash.apps.animecatalogue.view.screens.AnimeDetailScreen
import com.abhilash.apps.animecatalogue.view.screens.AnimeScreen
import com.abhilash.apps.animecatalogue.view.screens.HomeScreen
import com.abhilash.apps.animecatalogue.view.screens.LoginScreen
import com.abhilash.apps.animecatalogue.view.theme.LocalNavigateToLambda
import com.abhilash.apps.animecatalogue.BuildConfig
import kotlin.random.Random

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
        },
    ) {
        content()
    }
}

@Composable
fun AnimeCatalogue(
    startScreen: AnimeScreen,
    setLoginMode: (LoginMode) -> Unit
) {
    val navController = rememberNavController()

    AnimeProviders(navController = navController) {
        AnimeCatalogueNavigator(
            navController = navController,
            startScreen = startScreen,
            changeLoginMethod = setLoginMode
        )
    }
}

@Composable
fun AnimeCatalogueNavigator(
    navController: NavHostController,
    startScreen: AnimeScreen,
    changeLoginMethod: (LoginMode) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startScreen.name
    ) {

        composable(AnimeScreen.HOME.name) {
            HomeScreen()
        }

        composable(
            route = "${AnimeScreen.DETAIL.name}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val animeId = it.arguments?.getString("id") ?: "44081"
            AnimeDetailScreen(animeId = animeId)
        }

        composable(AnimeScreen.LOGIN.name) {
            val context = LocalContext.current

            LoginScreen(
                navigateToGuest = {
                    changeLoginMethod(LoginMode.GUEST)
                },
                navigateToUserLogin = {
                    context.launchSignInScreen()
                }
            )
        }
    }
}

private fun Context.launchSignInScreen() {
    val codeChallenge = getCodeChallengeString()
    val clientId = BuildConfig.API_KEY
    val redirectUri = "animecatalogue://auth"
    val state = "AnimeCatalogue"

    val authTokenLink = "https://myanimelist.net/v1/oauth2/authorize" +
            "?response_type=code" +
            "&client_id=$clientId" +
            "&state=$state" +
            "&code_challenge=$codeChallenge" +
            "&redirect_uri=$redirectUri"


    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authTokenLink))
    startActivity(intent)
}


private fun getCodeChallengeString(): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..128)
        .map { charPool[Random.nextInt(0, charPool.size)] }
        .joinToString("")
}

