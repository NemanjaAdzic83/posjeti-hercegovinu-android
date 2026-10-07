plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.posjetihercegovinu.app"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }

    }

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.posjetihercegovinu.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit - biblioteka koja pretvara Java interfejs u HTTP pozive
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    // Gson konverter - pretvara JSON iz odgovora u Java objekte
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // Logging interceptor - ispisuje zahtjeve i odgovore U Logcat (odlicno za debug)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // RecyclerView - efikasna lista koja pravi samo kartice koje su trenutno vidljive
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    // ViewModel i LiveData cuvaju podatke ekrana i obavjestavaju ekran kad se promjene
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.11.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.11.0")

}