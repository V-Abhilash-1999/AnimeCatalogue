package com.abhilash.apps.animecatalogue.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhilash.apps.animecatalogue.R
import com.abhilash.apps.animecatalogue.view.theme.StatusBarColorSetter
import com.abhilash.apps.animecatalogue.view.util.VerticalSpacer
import com.abhilash.apps.animecatalogue.view.util.drawBottomShade
import com.abhilash.apps.animecatalogue.view.util.drawTopShade

@Composable
fun LoginScreen(
    navigateToGuest: () -> Unit,
    navigateToUserLogin: () -> Unit
) {
    StatusBarColorSetter(
        color = MaterialTheme.colorScheme.background,
        isLightStatusBar = false
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .drawBottomShade(color = Color.Black)
                .drawTopShade(color = Color.Black)
                .padding(32.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            GreetingOtaku(
                navigateToGuest = navigateToGuest,
                navigateToUserLogin = navigateToUserLogin
            )
        }
    }

}

@Composable
private fun ColumnScope.GreetingOtaku(
    navigateToGuest: () -> Unit,
    navigateToUserLogin: () -> Unit
) {
    Text(
        modifier = Modifier
            .align(alignment = Alignment.CenterHorizontally)
            .padding(bottom = 16.dp),
        text = "Greetings Nakama!!",
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        color = Color.White
    )

    Text(
        modifier = Modifier
            .align(alignment = Alignment.CenterHorizontally),
        text = "Welcome to Anime Catalogue. A small catalogue which showcases a lot of anime.\n\n" +
                "To enjoy all features kindly login.",
        fontWeight = FontWeight(450),
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        color = Color.White
    )

    VerticalSpacer(32.dp)

    PrimaryButton(text = "Login") {
        navigateToUserLogin()
    }

    VerticalSpacer(16.dp)

    PrimaryButton(text = "Use as Guest") {
        navigateToGuest()
    }

}

@Composable
private fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(100),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        onClick = onClick,
    ) {
        Text(text = text)
    }
}