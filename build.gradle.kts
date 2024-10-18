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
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.google.firebase:firebase-crashlytics-gradle:3.0.2")
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