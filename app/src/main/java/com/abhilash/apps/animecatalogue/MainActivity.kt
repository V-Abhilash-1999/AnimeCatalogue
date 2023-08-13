package com.abhilash.apps.animecatalogue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abhilash.apps.animecatalogue.view.HomeScreen
import com.abhilash.apps.animecatalogue.view.theme.AnimeCatalogueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeCatalogueTheme {
                HomeScreen()
            }
        }
    }
}