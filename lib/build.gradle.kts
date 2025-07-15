import java.util.Date

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

//// Версионирование библиотеки
//extra["versionMajor"] = 1
//extra["versionMinor"] = 5
//extra["versionPatch"] = 0
//version = "${extra["versionMajor"]}.${extra["versionMinor"]}.${extra["versionPatch"]}"
//group = "ua.at.tsvetkov"
//
//// URL-ы для публикации, если нужны:
//extra["gitUser"] = "lordtao"
//extra["gitProject"] = "android-tao-core"
//extra["siteUrl"] = "https://github.com/${extra["gitUser"]}/${extra["gitProject"]}"
//extra["gitUrl"] = "https://github.com/${extra["gitUser"]}/${extra["gitProject"]}.git"

val skipCommitsCount = 0
val versionMajor = 2
val versionMinor = 2
val versionPatch = providers
    .exec {
        commandLine("git", "rev-list", "--count", "HEAD")
    }.standardOutput.asText
    .get()
    .trim()
    .toInt()

val versionName = "${versionMajor}.${versionMinor}.${versionPatch - skipCommitsCount}"

fun TaskContainer.registerCopyAarTask(variant: String) {
    val capVariant = variant.replaceFirstChar { it.uppercaseChar() }
    register<Delete>("deleteOld${capVariant}Aar") {
        group = "aar"
        description = "Удаляет ранее собранные AAR в ../aar для $variant"
        delete(fileTree("../aar") {
            include("taocore-$variant-*.aar")
        })
    }

    register<Copy>("copy${capVariant}Aar") {
        group = "aar"
        description = "Copy AAR $variant to ../aar"
        dependsOn("assemble${capVariant}")
        dependsOn("deleteOld${capVariant}Aar")
        val aarFile = file("build/outputs/aar/taocore-$versionName-$variant.aar")
        println("Registering copy${capVariant}Aar")
        doFirst {
            // Создать ../aar если не существует
            file("../aar").mkdirs()
            if (!aarFile.exists()) {
                throw GradleException("AAR file does not exist: $aarFile")
            }
            println("Copying $aarFile to ../aar/taocore-$variant.aar")
        }
        from(aarFile)
        outputs.upToDateWhen { false }
        into("../aar")
        if(variant == "release") {
            rename { "taocore.aar" }
        } else {
            rename { "taocore-$variant.aar" }
        }
        doLast {
            val versionFile = file("../aar/README.txt")
            versionFile.writeText("Library: taocore\nVersion: $versionName\nCreated: ${Date()}")
            println("Created version file: ${versionFile.absolutePath}")
        }
    }
}

tasks.registerCopyAarTask("release")
tasks.registerCopyAarTask("debug")

android {
    namespace = "ua.at.tsvetkov"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        setProperty("archivesBaseName", "taocore-$versionName")
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    debugImplementation(mapOf("name" to "taolog-debug", "ext" to "aar"))
    releaseImplementation(mapOf("name" to "taolog", "ext" to "aar"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.annotation)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
}

afterEvaluate {
    tasks.named("assembleDebug").configure {
        finalizedBy("copyDebugAar")
    }
    tasks.named("assembleRelease").configure {
        finalizedBy("copyReleaseAar")
    }
    tasks.named("build").configure {
        dependsOn("copyReleaseAar")
        dependsOn("copyDebugAar")
    }
}


