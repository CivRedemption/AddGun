plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.3.3"
  id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing
}

group "com.programmerdan.minecraft"
version "NCBPFluffyBear"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://papermc.io/repo/repository/maven-public/")
  }
  maven {
    url = uri("https://raw.githubusercontent.com/CivClassic/artifacts/master/")
  }
  maven {
    url = uri("https://build.devotedmc.com/plugin/repository/everything/")
  }
  maven {
    url = uri("https://repo.aikar.co/content/groups/aikar/")
  }
  maven {
    url = uri("https://papermc.io/repo/repository/maven-public/")
  }

}

dependencies {
  paperDevBundle("1.18.1-R0.1-SNAPSHOT")
  // paperweightDevBundle("com.example.paperfork", "1.18.1-R0.1-SNAPSHOT")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

  compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
  compileOnly("vg.civcraft.mc.civmodcore:CivModCore:1.8.2D")
  compileOnly("net.minelink:CombatTagPlus:1.3.3-SNAPSHOT") {
    exclude("org.mcstats.bukkit", "metrics-lite")
  }

  compileOnly("org.mockito:mockito-core:4.2.0")

  implementation("co.aikar:acf-bukkit:0.5.0-SNAPSHOT")
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))) // Add all jar files in libs folder

  paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.18.1-R0.1-SNAPSHOT")
}
repositories {
  mavenCentral()
}

tasks {
  // Configure reobfJar to run when invoking the build task
  assemble {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release.set(17)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
  }

  /*
  reobfJar {
    // This is an example of how you might change the output location for reobfJar. It's recommended not to do this
    // for a variety of reasons, however it's asked frequently enough that an example of how to do it is included here.
    outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
  }
   */
}
