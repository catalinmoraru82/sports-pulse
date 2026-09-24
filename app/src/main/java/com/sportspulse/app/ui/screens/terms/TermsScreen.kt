package com.sportspulse.app.ui.screens.terms

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sportspulse.app.ui.components.ForceStatusBarIcons
import com.sportspulse.app.ui.components.TopBarHeight
import com.sportspulse.app.ui.theme.ThemeState

private data class TermsSection(val heading: String, val body: String)
// Continutul primit de la user (documentul de Termeni si Conditii), structurat pe
// sectiuni pentru afisare - il actualizam manual aici daca textul se schimba,
// nu e generat dinamic dintr-o sursa externa.
private val TERMS_SECTIONS = listOf(
    TermsSection(
        "1. Acceptarea termenilor",
        "Prin descărcarea, instalarea sau utilizarea aplicației Sports Pulse (\"Aplicația\"), " +
            "sunteți de acord cu prezentele Termeni și Condiții de utilizare. Accesul și " +
            "utilizarea Aplicației sunt condiționate de acceptarea, fără limitări sau rezerve, " +
            "a acestor termeni și a legislației aplicabile. Dacă nu sunteți de acord cu acești " +
            "termeni, vă rugăm să nu utilizați Aplicația.",
    ),
    TermsSection(
        "2. Despre Aplicație",
        "Sports Pulse este o aplicație mobilă dedicată știrilor și informațiilor din domeniul " +
            "sportiv. Aplicația centralizează, sintetizează și afișează informații sportive " +
            "(rezultate, clasamente, articole, actualizări) provenite din surse publice sau din " +
            "surse cu care Sports Pulse are acorduri, respectând regulile de citare și, unde este " +
            "cazul, atașând link către sursa originală a informației.\n\n" +
            "Atunci când Aplicația oferă acces la conținut al unor terți (de exemplu prin " +
            "deschiderea unui articol într-un browser intern sau prin redirecționare către " +
            "site-ul sursă), acel conținut rămâne proprietatea și responsabilitatea publicației " +
            "care l-a produs. Sports Pulse nu își asumă răspunderea pentru corectitudinea, " +
            "actualitatea sau caracterul complet al informațiilor furnizate de aceste surse terțe.",
    ),
    TermsSection(
        "3. Proprietatea conținutului",
        "Aplicația, structura sa, elementele grafice, logo-ul, denumirea \"Sports Pulse\", " +
            "precum și orice conținut original creat de echipa Sports Pulse (denumite în " +
            "continuare \"Conținut\") sunt proprietatea Sports Pulse și sunt protejate de " +
            "legislația privind dreptul de autor și proprietatea intelectuală. Excepție face " +
            "conținutul provenit de la terți (site-uri de știri, surse sportive) accesat prin " +
            "intermediul Aplicației, asupra căruia drepturile aparțin exclusiv acelor terți.\n\n" +
            "Este interzisă copierea, reproducerea, distribuirea sau utilizarea Conținutului " +
            "aparținând Sports Pulse fără acordul scris prealabil al acestuia, cu excepția " +
            "utilizării normale, personale și necomerciale a Aplicației.",
    ),
    TermsSection(
        "4. Confidențialitate și date personale",
        "Sports Pulse respectă confidențialitatea utilizatorilor săi. Aplicația nu efectuează " +
            "activități de tracking (urmărire a comportamentului utilizatorului) și nu " +
            "utilizează servicii de analytics (precum Google Analytics sau similare) pentru " +
            "monitorizarea utilizatorilor. Sports Pulse nu colectează, nu solicită și nu are " +
            "acces la date care ar permite identificarea utilizatorului.\n\n" +
            "Aplicația poate stoca local, pe dispozitivul utilizatorului, anumite preferințe " +
            "(de exemplu echipe sau competiții favorite) exclusiv în scopul personalizării " +
            "experienței de utilizare, fără ca aceste informații să fie transmise către " +
            "servere terțe în scop de profilare sau publicitate comportamentală.",
    ),
    TermsSection(
        "5. Publicitate",
        "Aplicația este gratuită. Pentru susținerea serviciului, Sports Pulse poate afișa, în " +
            "prezent sau în viitor, reclame furnizate de rețele de publicitate terțe. Afișarea " +
            "reclamelor nu implică activități de tracking al comportamentului individual al " +
            "utilizatorului din partea Sports Pulse; orice date colectate strict în scopul " +
            "livrării reclamelor sunt gestionate exclusiv de furnizorii respectivi de " +
            "publicitate, potrivit propriilor politici de confidențialitate, pe care vă " +
            "recomandăm să le consultați.\n\n" +
            "Sports Pulse nu girează și nu își asumă responsabilitatea pentru produsele, " +
            "serviciile sau conținutul promovat prin materialele publicitare afișate în " +
            "Aplicație.",
    ),
    TermsSection(
        "6. Notificări",
        "La momentul actual, Aplicația nu trimite notificări push. Sports Pulse își rezervă " +
            "dreptul de a introduce, în viitor, un sistem de notificări (de exemplu pentru " +
            "rezultate live, goluri sau știri de ultimă oră). Dacă un astfel de sistem va fi " +
            "implementat, utilizatorii vor putea activa sau dezactiva notificările din setările " +
            "Aplicației sau ale dispozitivului, iar prezentul document va fi actualizat " +
            "corespunzător.",
    ),
    TermsSection(
        "7. Disponibilitatea și funcționarea Aplicației",
        "Sports Pulse depune eforturi rezonabile pentru a asigura funcționarea corectă și " +
            "continuă a Aplicației, dar nu garantează accesul neîntrerupt, lipsa erorilor sau " +
            "disponibilitatea permanentă a serviciului. Aplicația poate fi actualizată, " +
            "modificată sau întreruptă temporar sau definitiv, fără notificare prealabilă.\n\n" +
            "Costurile de trafic de date necesare pentru descărcarea și funcționarea Aplicației " +
            "sunt suportate de utilizator, conform planului tarifar al furnizorului său de " +
            "internet/telefonie.",
    ),
    TermsSection(
        "8. Exonerarea de răspundere",
        "Sports Pulse nu poate fi făcut responsabil pentru daune directe sau indirecte " +
            "rezultate din utilizarea Aplicației, din imposibilitatea de a o utiliza, din erori, " +
            "întârzieri sau inexactități ale informațiilor prezentate, acestea din urmă fiind, " +
            "în majoritatea cazurilor, preluate din surse terțe.\n\n" +
            "Un utilizator care acționează pe baza informațiilor afișate în Aplicație (de " +
            "exemplu rezultate, cote, program de meciuri) o face pe propria răspundere, Sports " +
            "Pulse neputând fi tras la răspundere pentru eventuale prejudicii rezultate din " +
            "utilizarea acestor informații.",
    ),
    TermsSection(
        "9. Legături către site-uri terțe",
        "Aplicația poate conține legături către site-uri sau conținut aflate în proprietatea " +
            "sau operate de terțe părți. Aceste legături sunt furnizate exclusiv pentru " +
            "confortul utilizatorului. Sports Pulse nu controlează și nu răspunde pentru " +
            "conținutul, politicile de confidențialitate sau securitatea acestor site-uri terțe " +
            "și nu girează produsele, serviciile sau reclamele prezentate pe acestea.",
    ),
    TermsSection(
        "10. Modificarea termenilor",
        "Sports Pulse își rezervă dreptul de a modifica periodic prezentul document, pentru a " +
            "reflecta schimbări legale, funcționale sau organizatorice. Versiunea actualizată va " +
            "fi disponibilă în Aplicație, iar continuarea utilizării Aplicației după publicarea " +
            "modificărilor constituie acceptarea acestora.",
    ),
    TermsSection(
        "11. Legislație aplicabilă și soluționarea litigiilor",
        "Prezentele Termeni și Condiții sunt guvernate de legislația din România, indiferent de " +
            "locația din care utilizatorul accesează Aplicația. Instanțele competente din " +
            "România au jurisdicție exclusivă asupra oricăror dispute derivate din sau în " +
            "legătură cu utilizarea Aplicației sau cu prezentul document.",
    ),
    TermsSection(
        "12. Contact",
        "Pentru întrebări, sugestii sau reclamații legate de funcționarea Aplicației, ne puteți " +
            "contacta la adresa:\nEmail: [completați adresa de email de contact]",
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(onBack: () -> Unit) {
    val darkTheme = ThemeState.darkModeOverride.value ?: isSystemInDarkTheme()
    ForceStatusBarIcons(useLightIcons = darkTheme)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Termeni și condiții") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Inapoi")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier.height(TopBarHeight),
            )
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
                text = "Termeni și Condiții de utilizare a Aplicației Sports Pulse",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "[Notă: completați aici denumirea completă a firmei/entității juridice " +
                    "care operează aplicația (SRL, PFA etc.) și datele sale de identificare, de " +
                    "îndată ce vor fi disponibile.]",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )

            TERMS_SECTIONS.forEach { section ->
                Text(
                    text = section.heading,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                )
                Text(
                    text = section.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Text(
                text = "Document informativ, adaptat pentru aplicația Sports Pulse. Recomandăm " +
                    "ca, înainte de publicarea în App Store / Google Play, acest document să fie " +
                    "revizuit de un consultant juridic, mai ales în ceea ce privește secțiunile " +
                    "referitoare la publicitate și eventuale date colectate în viitor (de " +
                    "exemplu, dacă veți introduce notificări push sau conturi de utilizator).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 32.dp, bottom = 24.dp),
            )
        }
    }
}
