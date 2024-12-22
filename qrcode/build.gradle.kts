//qrcoba.w3engineers.com.qrcoba


//        debug {
//            //BC:23:88:BF:E1:9B:E7:21:22:11:B9:5E:2D:2B:7A:BB:F0:A7:35:E8
//            storeFile file('D:\\android\\keystore\\debug.keystore')
//            storePassword 'android'
//            keyAlias 'AndroidDebugKey'
//            keyPassword 'android'
//        }
//28:36:1A:9B:4D:17:83:49:B6:F4:BE:60:EC:F4:8B:FC:A2:F9:12:9E
//D:\walhalla\Qrcode\app\keystore
import java.text.SimpleDateFormat
import java.util.Date

// Получаем значения из gradle.properties
val targetSdkVersion0: Int by lazy { (project.findProperty("targetSdkVersion0") as String).toInt() }

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.walhalla.qrcode"

    compileSdk = 35
    buildToolsVersion = rootProject.extra["buildToolsVersion0"] as String
    val code = versionCodeDate()
    defaultConfig {
        resConfigs(
            "en",
            "es",
            "fr",
            "de",
            "it",
            "pt",
            "el",
            "ru",
            "ja",
            "zh-rCN",
            "zh-rTW",
            "ko",
            "ar",
            "uk",
            "vi",
            "uz",
            "az"
        )

        vectorDrawables {
            useSupportLibrary = false
        }

        applicationId = "com.walhalla.qrcode"
        minSdk = 21
        targetSdk = targetSdkVersion0

        versionCode = code
        versionName = "1.3.$code"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        setProperty("archivesBaseName", "BarcodeWizard")
    }

    signingConfigs {
        create("config") {
            keyAlias = "qrcode"
            keyPassword = "@!sfuQ123zpc"
            storeFile = file("D:\\walhalla\\Tools\\keystore/keystore.jks")
            storePassword = "@!sfuQ123zpc"
        }
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("config")
        }

        getByName("release") {
            versionNameSuffix = ".release"
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        signingConfig = signingConfigs.getByName("config")
    }
}

tasks.register<Copy>("copyAabToBuildFolder") {
    println("mmmmmmmmmmmmmmmmm"+"$buildDir/outputs/bundle/release")
    val outputDirectory = file("C:/build")
    if (!outputDirectory.exists()) {
        outputDirectory.mkdirs()
    }

    from("$buildDir/outputs/bundle/release") {
        include("*.aab")
    }
    into(outputDirectory)
}

apply(from = "../copyReports.gradle.kts")

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation("androidx.appcompat:appcompat:${rootProject.extra["compatVersion"]}")
    implementation("com.google.android.material:material:${rootProject.extra["materialVersion"]}")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference:1.2.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Room
    implementation("androidx.room:room-runtime:${rootProject.extra["roomVersion"]}")
    implementation("androidx.room:room-rxjava2:${rootProject.extra["roomVersion"]}")
    annotationProcessor("androidx.room:room-compiler:${rootProject.extra["roomVersion"]}")

    implementation("com.google.firebase:firebase-ads:${rootProject.extra["gmsAds"]}")
    implementation("com.google.android.gms:play-services-ads:${rootProject.extra["gmsAds"]}")
    implementation("com.google.firebase:firebase-crashlytics:${rootProject.extra["crashlyticsVersion"]}")
    implementation("com.google.firebase:firebase-analytics:${rootProject.extra["analyticsVersion"]}")

    // Reactive Extensions (Rx)
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")

    // MultiDex
    implementation("androidx.multidex:multidex:2.0.1")

    // Bar code generation
    implementation("com.google.zxing:core:3.5.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") {
        isTransitive = false
    }

    // Pdf generation
    implementation("com.itextpdf:itextg:5.5.10")
    implementation("com.github.pwittchen:reactivenetwork-rx2:3.0.8")
    //implementation("com.agilie:swipe2delete:1.0")
    implementation("com.github.agilie:SwipeToDelete:1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation(project(":features:wads"))
    implementation(project(":features:ui"))
    implementation(project(":threader"))
    implementation(project(":common"))
    implementation("androidx.lifecycle:lifecycle-viewmodel:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-process:${rootProject.extra["lifecycle_version"]}")

    // Animation
    implementation("com.airbnb.android:lottie:6.4.0")

    // Konfetti
    
}

fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}

