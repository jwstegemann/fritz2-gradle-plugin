package dev.fritz2

val fritz_version = "0.9"

plugins {
    kotlin("multiplatform")
    kotlin("kapt")
}

afterEvaluate {
    tasks.getByName("compileKotlinJs").dependsOn("kaptKotlinJvm")
    tasks.getByName("compileKotlinMetadata").dependsOn("kaptKotlinJvm")
}

kotlin {
    jvm() // otherwise: Configuration with name 'kapt' not found error

    sourceSets {
        val commonMain by getting {
            dependencies {
                configurations["kapt"]?.dependencies?.add(compileOnly("dev.fritz2:lenses-annotation-processor:$fritz_version"))
            }
            kotlin.srcDir("$buildDir/generated/source/kaptKotlin/main")
        }
        val generated by creating {
            dependsOn(commonMain)
            kotlin.srcDir("$buildDir/generated/source/kaptKotlin")
        }
        val jvmMain by getting { dependsOn(generated) }
        val jvmTest by getting { dependsOn(generated) }
        findByName("jsMain")?.dependsOn(generated)
        findByName("jsTest")?.dependsOn(generated)
    }
}