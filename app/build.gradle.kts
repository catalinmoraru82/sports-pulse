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

android {
    namespace = "com.sportspulse.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sportspulse.app"
        // Android 12 (API 31) e cel mai vechi OS suportat, conform cerintei.
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // URL-ul admin-ului si cheia API sunt citite din local.properties,
        // NU sunt hardcodate in cod sursa (vezi local.properties.example).
        val apiBaseUrl = localProperties.getProperty("API_BASE_URL") ?: "https://CHANGE-ME.vercel.app/"
        val androidApiKey = localProperties.getProperty("ANDROID_API_KEY") ?: ""
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        buildConfigField("String", "ANDROID_API_KEY", "\"$androidApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    implementation("androidx.compose.material:material-icons-extended")

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
