import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

// Citim local.properties manual: Gradle nu il incarca automat ca proprietati de proiect.
val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(localFile))
    }
}

// Semnarea build-ului de release: datele cheii se citesc din keystore.properties (in radacina
// proiectului). Fisierul NU e in git - vezi keystore.properties.example si .gitignore.
val keystoreProperties = Properties().apply {
    val keystoreFile = rootProject.file("keystore.properties")
    if (keystoreFile.exists()) {
        FileInputStream(keystoreFile).use { load(it) }
    }
}

android {
    namespace = "com.sportspulse.app"
    // Google Play cere targetSdk 36 pentru aplicatii noi (din 31 august 2026).
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sportspulse.app"
        // Android 12 (API 31) e cel mai vechi OS suportat, conform cerintei.
        minSdk = 31
        targetSdk = 36
        // versionCode trebuie sa creasca la FIECARE incarcare in Play Console (1, 2, 3...).
        // versionName e doar textul afisat userilor.
        versionCode = 1
        versionName = "1.0.0"

        // URL-ul admin-ului si cheia API sunt citite din local.properties,
        // NU sunt hardcodate in cod sursa (vezi local.properties.example).
        val apiBaseUrl = localProperties.getProperty("API_BASE_URL") ?: "https://CHANGE-ME.vercel.app/"
        val androidApiKey = localProperties.getProperty("ANDROID_API_KEY") ?: ""
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        buildConfigField("String", "ANDROID_API_KEY", "\"$androidApiKey\"")
    }

    signingConfigs {
        // Se creeaza doar daca exista keystore.properties. Fara el, build-ul de release iese
        // nesemnat si poate fi semnat din Android Studio (Build > Generate Signed Bundle / APK).
        if (keystoreProperties.isNotEmpty()) {
            create("release") {
                fun required(key: String) = requireNotNull(keystoreProperties.getProperty(key)) {
                    "keystore.properties: lipseste valoarea pentru '$key'"
                }
                storeFile = rootProject.file(required("storeFile"))
                storePassword = required("storePassword")
                keyAlias = required("keyAlias")
                keyPassword = required("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core + lifecycle
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose (BOM gestioneaza automat versiunile compatibile intre ele)
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    // material-icons-extended NU e nevoie - contine toate iconitele Material (mii),
    // e o dependinta foarte mare (documentatia oficiala Google recomanda explicit
    // sa nu fie inclusa direct). Folosim doar 3 iconite (Settings, ArrowBack,
    // KeyboardArrowRight), toate deja incluse in material-icons-core (bundlat
    // automat cu material3 mai sus) - eliminarea asta scade mult dimensiunea APK-ului.

    // Navigare intre ecrane
    implementation("androidx.navigation:navigation-compose:2.8.4")

    // Retea: Retrofit + kotlinx.serialization (fara Gson, mai mic si mai rapid)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Incarcare imagini, cache automat pe disc/memorie
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    // Splash screen nativ (Android 12+ are API dedicat)
    implementation("androidx.core:core-splashscreen:1.0.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.12.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
}

// Ocolitor pentru un ciudat al sincronizarii din Android Studio: IDE-ul cere uneori taskul
// "prepareKotlinBuildScriptModel" in acest proiect si da eroare daca lipseste. Il cream doar
// daca nu exista deja (altfel Gradle da "task with that name already exists").
if (tasks.findByName("prepareKotlinBuildScriptModel") == null) {
    tasks.register("prepareKotlinBuildScriptModel") {}
}
