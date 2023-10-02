package com.abhilash.apps.animecatalogue

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.abhilash.apps.animecatalogue.model.datastore.LoginMode
import com.abhilash.apps.animecatalogue.view.AnimeCatalogue
import com.abhilash.apps.animecatalogue.view.theme.AnimeCatalogueTheme
import com.abhilash.apps.animecatalogue.view.util.SplashScreen
import com.abhilash.apps.animecatalogue.view.util.rememberStartScreen
import com.abhilash.apps.animecatalogue.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            AnimeCatalogueTheme {
                AnimeCatalogueLauncher()
            }
        }
    }

    @Composable
    fun AnimeCatalogueLauncher() {
        val loginMode = loginViewModel
            .loginMode
            .collectAsState(initial = LoginMode.UNKNOWN)

        when(loginMode.value) {
            LoginMode.UNKNOWN -> {
                SplashScreen()
            }

            else -> {
                val startScreen = rememberStartScreen(
                    loginMode = loginMode.value,
                    authTokenState = loginViewModel.viewState.value
                )

                AnimeCatalogue(
                    startScreen = startScreen,
                    setLoginMode = loginViewModel::setLoginMode
                )
            }
        }
    }


    override fun onResume() {
        super.onResume()
        intent?.data?.getQueryParameter("code")?.let { code ->
            loginViewModel.setAuthToken(code)
        } ?: loginViewModel.authTokenSetFailure()
    }
}
