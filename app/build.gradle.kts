plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-kapt")
    id("kotlin-android")

    id("androidx.navigation.safeargs")

    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.tuhoc.dreamtunes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tuhoc.dreamtunes"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.media3:media3-session:1.3.0")
    implementation("androidx.palette:palette-ktx:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val nav_version = "2.5.3"
    // navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // intuit
    implementation("com.intuit.ssp:ssp-android:1.1.0")
    implementation("com.intuit.sdp:sdp-android:1.1.0")

    // retrofit call api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // gson
    implementation("com.google.code.gson:gson:2.10.1")

    // circle imageview
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // slide
    implementation("com.github.smarteist:autoimageslider:1.4.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")

//    // realtime
//    implementation("com.google.firebase:firebase-database")

//    // authentication
//    implementation("com.google.firebase:firebase-auth")

    // image picker
    implementation("com.github.dhaval2404:imagepicker:2.1")

    // storage
    implementation("com.google.firebase:firebase-storage")

    // media
    implementation("androidx.media:media:1.7.0")

    implementation("androidx.palette:palette:1.0.0")

    implementation("androidx.media:media:1.7.0")

    // keyboard event
    implementation("net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:2.1.0")
}