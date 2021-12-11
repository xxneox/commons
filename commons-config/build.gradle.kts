import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compileOnly(project(":commons-core"))

    implementation("org.spongepowered:configurate-hocon:4.1.2")
}

tasks.withType<ShadowJar> {
    relocate("com.typesafe.config", "$group.libs.config")
    relocate("org.spongepowered.configurate", "$group.libs.configurate")
    relocate("io.leangen.geantyref", "$group.libs.geantyref")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}