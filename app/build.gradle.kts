plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services") // Using the traditional id method
}

android {
    namespace = "com.shambavi.thericecompany"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shambavi.thericecompany"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources=true
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
        dataBinding = true
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("de.hdodenhof:circleimageview:3.1.0")

//    api
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.hbb20:ccp:2.5.0")

    //image
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    // Firebase BOM to manage Firebase library versions
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
    // Firebase Messaging
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")

        implementation ("com.google.android.gms:play-services-location:21.2.0") // Use the latest version


    implementation ("com.google.android.libraries.places:places:4.0.0")
}