package com.sportspulse.app.ui.screens.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sportspulse.app.R
import com.sportspulse.app.ui.components.ForceStatusBarIcons
import com.sportspulse.app.ui.components.TopBarHeight
import com.sportspulse.app.ui.theme.ThemeState

@Composable
fun SettingsScreen(onBack: () -> Unit, onTermsClick: () -> Unit) {
    val context = LocalContext.current
    val contactEmail = stringResource(R.string.contact_email)
    // Fundalul acestui ecran urmeaza tema normal (spre deosebire de Feed/WebView, care
    // au banda neagra fixa) - reafirmam explicit iconitele corecte pt tema curenta,
    // altfel ar ramane cele albe fortate de ecranul anterior (Feed).
    val darkTheme = ThemeState.darkModeOverride.value ?: isSystemInDarkTheme()
    ForceStatusBarIcons(useLightIcons = darkTheme)
    Scaffold(
        topBar = {
            // Bara custom, la fel ca in FeedScreen/ArticleWebViewScreen - NU CenterAlignedTopAppBar.
            // Acel component Material3 isi aplica singur statusBarsPadding intern, iar combinat cu
            // Modifier.height(TopBarHeight) fortat din exterior, continutul (titlul) era strivit
            // langa bara de status in loc sa fie centrat vertical pe cele 64dp - de-asta titlul
            // aparea "foarte sus". Aici aplicam noi statusBarsPadding(), apoi height() separat,
            // exact ca la celelalte ecrane - elimina conflictul.
            Surface(color = MaterialTheme.colorScheme.surface) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(TopBarHeight),
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
        ) {
            SettingsSection(title = stringResource(R.string.settings_section_appearance)) {
                // Legat direct de ThemeState, care e citit din SportsPulseTheme la nivel de app -
                // schimbarea aici se reflecta imediat in toata aplicatia, nu doar local pe acest ecran.
                val systemDark = isSystemInDarkTheme()
                val darkMode = ThemeState.darkModeOverride.value ?: systemDark
                ToggleRow(
                    label = stringResource(R.string.settings_dark_mode),
                    checked = darkMode,
                    onCheckedChange = { ThemeState.darkModeOverride.value = it },
                )
            }

            androidx.compose.foundation.layout.Spacer(Modifier.height(24.dp))

            SettingsSection(title = stringResource(R.string.settings_section_info)) {
                LinkRow(
                    label = stringResource(R.string.terms_title),
                    showChevron = true,
                    onClick = onTermsClick,
                )
            }

            androidx.compose.foundation.layout.Spacer(Modifier.height(24.dp))

            SettingsSection(title = stringResource(R.string.settings_section_contact)) {
                LinkRow(
                    label = stringResource(R.string.settings_email_label),
                    onClick = { openEmailApp(context, contactEmail) },
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        androidx.compose.foundation.layout.Spacer(Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
private fun LinkRow(label: String, value: String? = null, showChevron: Boolean = false, onClick: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(51.dp)
            .let { if (onClick != null) it.clickable(onClick = onClick) else it },
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value != null) {
                Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (showChevron) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ACTION_SENDTO cu schema mailto: face sa apara DOAR aplicatiile de email (nu si alte aplicatii
// care stiu sa trimita text, cum ar fi mesageria). Daca userul nu are nicio aplicatie de email
// instalata, startActivity arunca ActivityNotFoundException - il prindem si afisam un mesaj.
private fun openEmailApp(context: android.content.Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(
            context,
            context.getString(R.string.settings_no_email_app, email),
            Toast.LENGTH_LONG,
        ).show()
    }
}
