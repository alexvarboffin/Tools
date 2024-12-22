import java.text.SimpleDateFormat
import java.util.Date

val minSdkVersion by extra(21)
val retrofitVersion by extra("2.11.0")
val okHttpVersion by extra("4.12.0")

buildscript {
    val kotlinVersion by extra("1.8.10")

    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")

        //noinspection JcenterRepositoryObsolete
        //jcenter()
        mavenLocal()
    }

    dependencies {
        classpath(libs.gradle)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")

        //noinspection JcenterRepositoryObsolete
        //jcenter()
        mavenLocal()

    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}