plugins {
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.13.0"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.4.30"))
}

group = "dev.fritz2"
version = "0.9.1"

pluginBundle {
    website = "https://www.fritz2.dev"
    vcsUrl = "https://github.com/jwstegemann/fritz2-gradle-plugin.git"
    tags = listOf("kotlin", "fritz2")
}

gradlePlugin
        .plugins
        .find { it.name == "dev.fritz2.fritz2-gradle" }!!
        .apply {
            id = "dev.fritz2.fritz2-gradle"
            version = project.version
            displayName = "A plugin that sets up your kotlin multiplatform-project for fritz2"
            description = "A plugin that sets up code-generation for lenses"
        }

publishing {
    repositories {
        maven {
            name = "bintray"
            val bintrayUsername = "jwstegemann"
            val bintrayRepoName = "fritz2"
            val bintrayPackageName = "fritz2-gradle-plugin"
            setUrl(
                    "https://api.bintray.com/maven/" +
                            "$bintrayUsername/$bintrayRepoName/$bintrayPackageName/;" +
                            "publish=0;" + // Never auto-publish to allow override.
                            "override=1"
            )
            credentials {
                username = "jwstegemann"
                password = System.getenv("BINTRAY_API_KEY")
            }
        }
    }
}
