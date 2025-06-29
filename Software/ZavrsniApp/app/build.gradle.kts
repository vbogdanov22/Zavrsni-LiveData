import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.disableClassloaderCacheForProcessors

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

fun getApiKey(propertyKey: String): String {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if(localPropertiesFile.exists()){
        properties.load(localPropertiesFile.inputStream())
    }
    return properties.getProperty(propertyKey) ?: ""
}

android {
    namespace = "hr.foi.zavrsniapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "hr.foi.zavrsniapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "WEATHER_API_KEY", getApiKey("WEATHER_API_KEY"))
        buildConfigField("String", "SPORTS_API_KEY", getApiKey("SPORTS_API_KEY"))
    }

    // razmislit o tome da li je potrebno
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // RETROFIT
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converterGson)
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation(libs.glide)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}