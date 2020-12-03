package dev.fritz2

val fritz_version = "0.8"

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.kapt")
}

repositories {
    jcenter()
}

kotlin {
    jvm()
    js().browser()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("dev.fritz2:core:$fritz_version")

                configurations.get("kapt").dependencies.add(compileOnly("dev.fritz2:lenses-annotation-processor:$fritz_version"))
            }
            tasks.getByName("compileKotlinJs").dependsOn("kaptKotlinJvm")
            tasks.getByName("compileKotlinMetadata").dependsOn("kaptKotlinJvm")
            //tasks.getByName("jvmMainClasses").dependsOn("metadataMainClasses")
            //tasks.getByName("jsMainClasses").dependsOn("metadataMainClasses")

            kotlin.srcDir("$buildDir/generated/source/kaptKotlin/main")
        }
        val generated by creating {
            dependsOn(commonMain)
            kotlin.srcDir("$buildDir/generated/source/kaptKotlin")
        }
        val jvmMain by getting {
            dependsOn(generated)
        }
        val jvmTest by getting {
            dependsOn(generated)
        }
        val jsMain by getting {
            dependsOn(generated)
        }
        val jsTest by getting {
            dependsOn(generated)
        }
    }
}