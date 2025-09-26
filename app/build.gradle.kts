import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.compose)
    // id ("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "org.com.hcmurs"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.com.hcmurs"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Read from local.properties
        val properties = Properties()
        if (rootProject.file("local.properties").exists()) {
            properties.load(project.rootProject.file("local.properties").inputStream())
        } else {
            throw RuntimeException("local.properties file not found")
        }

        val error = "variable not found in local.properties"

        var auth0Domain = properties.getProperty("auth0.domain")
        var auth0ClientId = properties.getProperty("auth0.client.id")

        // Define BuildConfig fields without revealing fallback values
        buildConfigField(
            "String",
            "AUTH0_DOMAIN",
            "\"${auth0Domain ?: throw RuntimeException(error)}\"",
        )
        buildConfigField(
            "String",
            "AUTH0_CLIENT_ID",
            "\"${auth0ClientId ?: throw RuntimeException(error)}\"",
        )

        manifestPlaceholders["appAuthRedirectScheme"] = "org.com.hcmurs"
        manifestPlaceholders["auth0Domain"] = auth0Domain
        manifestPlaceholders["auth0Scheme"] = applicationId.toString()
        manifestPlaceholders["auth0CallbackUrl"] = "/android/org.com.hcmurs/callback"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.play.services.auth) // hoặc bản mới nhất
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp.urlconnection)

    implementation(libs.androidx.work.runtime.ktx)

    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.composeIcons.fontAwesome)

    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)

    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.accompanist.pager)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)

    implementation(libs.coil.network.okhttp)

    // QR Code Scanning
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view.v131)
    implementation(libs.barcode.scanning)

    implementation(libs.osmdroid.android)

    implementation(libs.appauth)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.animation)
    implementation(libs.stripe.android)

    // firebase
    implementation(platform(libs.firebase.bom))
    // https://mvnrepository.com/artifact/com.google.firebase/firebase-analytics-ktx
    implementation(libs.google.firebase.analytics.ktx)
    // messaging
    implementation(libs.firebase.messaging.ktx)

    // notification permission
    implementation(libs.accompanist.permissions)

    // auth0
    implementation("com.auth0.android:auth0:2.9.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// kapt {
//    correctErrorTypes = true
// }
