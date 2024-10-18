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
    namespace = "com.walhalla.vibro"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    signingConfigs {
        create("config") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("keystore/keystore.jks")
            storePassword = "release"
        }
    }

    val versionPropsFile = file("version.properties")

    if (versionPropsFile.canRead()) {
        val code = versionCodeDate()

        defaultConfig {
            applicationId = "com.walhalla.vibro"
            minSdk = rootProject.extra["minSdkVersion"] as Int
            targetSdk = targetSdkVersion0
            versionCode = code
            versionName = "1.1.$code"

            multiDexEnabled = true
            vectorDrawables.useSupportLibrary = true
            resConfigs("ru", "en", "uk")
        }
    } else {
        throw GradleException("Could not read version.properties!")
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-DEBUG"
            isDebuggable = true
            isJniDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("config")
            multiDexEnabled = true
        }

        getByName("release") {
            versionNameSuffix = ".release"
            isDebuggable = false
            isJniDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("config")
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            multiDexEnabled = true
            //renderscriptDebuggable = false
            //pseudoLocalesEnabled = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:${rootProject.extra["compatVersion"]}")
    implementation("com.google.android.material:material:${rootProject.extra["materialVersion"]}")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-ads:${rootProject.extra["gmsAds"]}")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-crashlytics:${rootProject.extra["crashlyticsVersion"]}")
    implementation("com.google.firebase:firebase-analytics:${rootProject.extra["analyticsVersion"]}")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation(project(":features:ui"))
    implementation(project(":features:wads"))
    implementation(project(":threader"))
    implementation(project(":common"))
    implementation("androidx.lifecycle:lifecycle-viewmodel:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
}


fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}

//        config {
//            keyAlias 'isaidit'
//            keyPassword 'isaidit'
//            storeFile file('../keystore/1016_isaidit.key')
//            storePassword 'isaidit'
//        }
//            keyAlias 'vibrator'
//            keyPassword '@!sfuQ123zpc'
//            storeFile file('D:/walhalla/sign/keystore.jks')
//            storePassword '@!sfuQ123zpc'

//    flavorDimensions "version"
//
//    productFlavors {
//        promo {
//            // Assigns this product flavor to the "version" flavor dimension.
//            // If you are using only one dimension, this property is optional,
//            // and the plugin automatically assigns all the module's flavors to
//            // that dimension.
//            dimension "version"
//            versionCode 111
//            versionName "1.1." + 111 + ".release"
//        }
//        full {
//            dimension "version"
//            //applicationIdSuffix ".full"
//            //versionNameSuffix "-full"
//        }
//    }