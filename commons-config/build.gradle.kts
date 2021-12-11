import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compileOnly(project(":commons-core"))

    implementation("org.spongepowered:configurate-hocon:4.1.2")
}

tasks.withType<ShadowJar> {
    relocate("com.typesafe.config", "me.xneox.commons.libs.libs.config")
    relocate("org.spongepowered.configurate", "me.xneox.commons.libs.libs.configurate")
    relocate("io.leangen.geantyref", "me.xneox.commons.libs.libs.geantyref")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}