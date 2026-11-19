package com.sportspulse.app.ui.screens.terms

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sportspulse.app.R
import com.sportspulse.app.ui.components.ForceStatusBarIcons
import com.sportspulse.app.ui.components.TopBarHeight
import com.sportspulse.app.ui.theme.ThemeState

private data class TermsSection(@StringRes val heading: Int, @StringRes val body: Int)
// Textul propriu-zis e in res/values/strings_terms.xml (terms_sN_heading / terms_sN_body);
// aici e doar ordinea in care se afiseaza sectiunile.
private val TERMS_SECTIONS = listOf(
    TermsSection(R.string.terms_s1_heading, R.string.terms_s1_body),
    TermsSection(R.string.terms_s2_heading, R.string.terms_s2_body),
    TermsSection(R.string.terms_s3_heading, R.string.terms_s3_body),
    TermsSection(R.string.terms_s4_heading, R.string.terms_s4_body),
    TermsSection(R.string.terms_s5_heading, R.string.terms_s5_body),
    TermsSection(R.string.terms_s6_heading, R.string.terms_s6_body),
    TermsSection(R.string.terms_s7_heading, R.string.terms_s7_body),
    TermsSection(R.string.terms_s8_heading, R.string.terms_s8_body),
    TermsSection(R.string.terms_s9_heading, R.string.terms_s9_body),
    TermsSection(R.string.terms_s10_heading, R.string.terms_s10_body),
    TermsSection(R.string.terms_s11_heading, R.string.terms_s11_body),
    TermsSection(R.string.terms_s12_heading, R.string.terms_s12_body),
)

@Composable
fun TermsScreen(onBack: () -> Unit) {
    val darkTheme = ThemeState.darkModeOverride.value ?: isSystemInDarkTheme()
    ForceStatusBarIcons(useLightIcons = darkTheme)

    Scaffold(
        topBar = {
            // Bara custom, la fel ca in FeedScreen/SettingsScreen - NU CenterAlignedTopAppBar.
            // Acel component isi aplica singur statusBarsPadding intern; combinat cu
            // Modifier.height(TopBarHeight) fortat din exterior, titlul era strivit langa bara
            // de status si aparea "foarte sus" in loc sa fie centrat pe cele 64dp.
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
                        text = stringResource(R.string.terms_title),
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
            Text(
                text = stringResource(R.string.terms_heading),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(R.string.terms_operator_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )

            TERMS_SECTIONS.forEach { section ->
                Text(
                    text = stringResource(section.heading),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                )
                Text(
                    text = stringResource(section.body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
