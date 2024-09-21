import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("io.freefair.lombok") version "8.10"
    id("com.gradleup.shadow") version "8.3.0"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "nl.openminetopia"
version = "1.0-SNAPSHOT"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION


repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "aikar"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "extendedclip"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "enginehub"
        url = uri("https://maven.enginehub.org/repo/")
    }
}

dependencies {
    /* Paper */
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")

    /* Configuration */
    compileOnly("org.spongepowered:configurate-yaml:4.1.2")
    compileOnly("org.spongepowered:configurate-core:4.1.2")

    /* Database */
    compileOnly("com.zaxxer:HikariCP:5.1.0")
    compileOnly("mysql:mysql-connector-java:8.0.33")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.4.1")
    compileOnly("org.xerial:sqlite-jdbc:3.46.1.0")
    implementation("com.github.Mindgamesnl:storm:e1f961b480")

    /* Command Framework */
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")

    /* Scoreboard */
    val scoreboardLibraryVersion = "2.1.12"
    implementation("net.megavex:scoreboard-library-api:$scoreboardLibraryVersion")
    runtimeOnly("net.megavex:scoreboard-library-implementation:$scoreboardLibraryVersion")
    runtimeOnly("net.megavex:scoreboard-library-modern:$scoreboardLibraryVersion:mojmap")

    /* PlaceholderAPI */
    compileOnly("me.clip:placeholderapi:2.11.6")

    /* WorldGuard */
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.12")
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    if (JavaVersion.current() < javaVersion) {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
//    options.isFork = true
//    options.forkOptions.executable = "javac"

    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")

    relocate("co.aikar.commands", "nl.openminetopia.shaded.acf")
    relocate("co.aikar.locales", "nl.openminetopia.shaded.locales")
    relocate("net.megavex.scoreboardlibrary", "nl.openminetopia.shaded.scoreboard")
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}