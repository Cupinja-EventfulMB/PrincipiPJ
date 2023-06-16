import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("io.realm.kotlin") version "1.8.0"

}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven ( "https://jitpack.io" )
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation ("org.jsoup:jsoup:1.14.2") // jsoup
                implementation("org.seleniumhq.selenium:selenium-java:3.8.1") // selenium
                implementation ("org.mongodb:mongodb-driver-sync:4.5.1") // mongodb
                implementation("org.litote.kmongo:kmongo:4.3.0") // mongodb kotlin dependencxy
                implementation ("io.github.cdimascio:dotenv-kotlin:6.4.1") // .env
                implementation ("com.squareup.okhttp3:okhttp:4.9.1") // OkHttp
                implementation ("com.google.code.gson:gson:2.8.8") // toJSON
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Podatki_PPJ_1"
            packageVersion = "1.0.0"
        }
    }
}