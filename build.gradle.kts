import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("com.modrinth.minotaur") version "2.+"
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.compileJava {
    options.release.set(21)
}

group = "net.thenextlvl.portals"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.thenextlvl.net/releases")
}

dependencies {
    implementation(project(":api"))

    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.4.0-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }

    compileOnly("net.thenextlvl:vault-api:1.7.1")
    compileOnly("net.thenextlvl:service-io:2.3.3")

    implementation("net.thenextlvl.core:i18n:3.2.2")
    implementation("net.thenextlvl.core:paper:2.3.1")

    implementation("org.bstats:bstats-bukkit:3.1.0")
}

tasks.shadowJar {
    archiveBaseName.set("portals")
    relocate("org.bstats", "net.thenextlvl.portals.bstats")
}

paper {
    name = "Portals"
    main = "net.thenextlvl.portals.PortalsPlugin"
    apiVersion = "1.21.4"
    description = "Create, delete and manage custom portals"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    website = "https://thenextlvl.net"
    authors = listOf("NonSwag")
    foliaSupported = true
    serverDependencies {
        register("WorldEdit") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        // fixme: temporary solution, FAWE provides WorldEdit so this should not be required
        register("FastAsyncWorldEdit") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("ServiceIO") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
        register("Vault") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = false
        }
    }
    permissions {
        register("portals.command.portal")
        register("portals.command.cooldown") { children = listOf("portals.command.portal") }
        register("portals.command.create") { children = listOf("portals.command.portal") }
        register("portals.command.delete") { children = listOf("portals.command.portal") }
        register("portals.command.permission") { children = listOf("portals.command.portal") }
        register("portals.command.teleport") { children = listOf("portals.command.portal") }
    }
}

val versionString: String = project.version as String
val isRelease: Boolean = !versionString.contains("-pre")

val versions: List<String> = (property("gameVersions") as String)
    .split(",")
    .map { it.trim() }

hangarPublish { // docs - https://docs.papermc.io/misc/hangar-publishing
    publications.register("plugin") {
        id.set("Portals")
        version.set(versionString)
        changelog = System.getenv("CHANGELOG")
        channel.set(if (isRelease) "Release" else "Snapshot")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms.register(Platforms.PAPER) {
            jar.set(tasks.shadowJar.flatMap { it.archiveFile })
            platformVersions.set(versions)
            dependencies {
                hangar("WorldEdit") {
                    required.set(false)
                }
                hangar("ServiceIO") {
                    required.set(false)
                }
                url("Vault", "https://www.spigotmc.org/resources/vault.34315/") {
                    required.set(false)
                }
            }
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("R2dgUCYq")
    changelog = System.getenv("CHANGELOG")
    versionType = if (isRelease) "release" else "beta"
    uploadFile.set(tasks.shadowJar)
    gameVersions.set(versions)
    syncBodyFrom.set(rootProject.file("README.md").readText())
    loaders.addAll((property("loaders") as String).split(",").map { it.trim() })
    dependencies {
        optional.project("worldedit")
        optional.project("service-io")
    }
}
