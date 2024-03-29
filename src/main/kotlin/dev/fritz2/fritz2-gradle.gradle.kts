package dev.fritz2

val fritz_version = "0.13"

plugins {
    kotlin("multiplatform")
    kotlin("kapt")
}

afterEvaluate {
    //    tasks.getByName("compileKotlinJs").dependsOn("kaptKotlinJvm")
    //    tasks.getByName("compileKotlinMetadata").dependsOn("kaptKotlinJvm")
    the<org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension>().apply {
        targets.all {
            compilations.all {
                if (platformType == org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.js ||
                        platformType == org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.common) {
                    compilations.all {
                        if (name == org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME) {
                            compileKotlinTaskProvider {
                                dependsOn("kaptKotlinJvm")
                            }
                        }
                    }
                }
            }
        }
    }
}

kotlin {
    jvm() // otherwise: Configuration with name 'kapt' not found error

    sourceSets {
        val commonMain by getting {
            dependencies {
                configurations["kapt"]?.dependencies?.add(compileOnly("dev.fritz2:lenses-annotation-processor:$fritz_version"))
                implementation("dev.fritz2:core:$fritz_version")
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