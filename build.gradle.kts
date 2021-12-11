import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

allprojects {
    group = "me.xneox"
    version = "1.0.1"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains:annotations:22.0.0")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    // Publishing to jitpack.org

    tasks.withType<ShadowJar> {
        // once these deprecated fields gets removed i'm gonna be fucked because nothing else works
        archiveFileName.set("$baseName-$version.$extension")
    }

    artifacts {
        archives(tasks["shadowJar"])
    }

    publishing {
        publications {
            create<MavenPublication>("shadow") {
                from(components["java"])
            }
        }
    }
}