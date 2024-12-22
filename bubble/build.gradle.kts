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
//com.demo.bubblelevel
android {



    compileSdk = 35
    //compileSdkVersion(35)

    buildToolsVersion = rootProject.extra["buildToolsVersion0"] as String

    namespace = "a.bubblelevel.spiritpro"

    val code = versionCodeDate()

    defaultConfig {
        applicationId = "a.bubblelevel.spiritpro"
        minSdk = 21
        targetSdk = 35
        //targetSdkVersion(35)

        versionCode = code
        versionName = "1.3.$code"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    signingConfigs {
        create("config") {
            keyAlias = "bubble"
            keyPassword = "@!sfuQ123zpc"
            storeFile = file("D:\\walhalla\\Tools\\keystore/keystore.jks")
            storePassword = "@!sfuQ123zpc"
        }
    }

    buildTypes {
        getByName("debug") {
            versionNameSuffix = ".release"
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

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

//    kotlinOptions {
//        jvm00Target = "21"
//    }
}



//tasks.register<Copy>("copyAabToBuildFolder") {
//
//    println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
//    val outputDirectory = file("C:/build")
//    if (!outputDirectory.exists()) {
//        outputDirectory.mkdirs()
//    }
//
//    from("$buildDir/outputs/bundle/release") {
//        include("*.aab")
//    }
//    into(outputDirectory)
//}
//tasks.register("copyAabToBuildFolder") {
//    doLast {
//        println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
//    }
//}


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


//build.dependsOn publishToMavenLocal

//// Настройка порядка выполнения задач
//tasks.named("bundleRelease") {
//    finalizedBy(tasks.named("test123"))
//}
dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.ads)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.glide)
    implementation(libs.androidx.preference.ktx.ktx)
    implementation(libs.pulsator4droid)
    implementation(libs.androidx.core.ktx)

    implementation(project(":features:ui"))
    implementation(project(":common"))

    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // This dependency is downloaded from the Google’s Maven repository.
    // So, make sure you also include that repository in your project's build.gradle file.
    implementation (libs.app.update)

    // For Kotlin users also add the Kotlin extensions library for Play In-App Update:
    implementation (libs.app.update.ktx)
}

fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()-200
}