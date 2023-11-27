@file:Suppress("PropertyName", "SpellCheckingInspection")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val taboolib_version: String by project

val jimmer_version:String by project

val kotlinVersionNum: String
    get() = project.kotlin.coreLibrariesVersion.replace(".", "")

plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("http://ptms.ink:8081/repository/releases/")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("https://libraries.minecraft.net")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
        // 引入 Taboolib
        // 注意 common 模块不可使用 implementation 引入（即使用 ShadowJar 插件打包）
        // 因为需要借助 TabooLib Gradle 插件修改一些东西
        compileOnly("io.izzel.taboolib:common:$taboolib_version")
        // 使用 shadowJar 插件打包的模块
        implementation("io.izzel.taboolib:common-5:$taboolib_version")
        implementation("io.izzel.taboolib:module-chat:$taboolib_version")
        implementation("io.izzel.taboolib:module-configuration:$taboolib_version")
        implementation("io.izzel.taboolib:platform-bukkit:$taboolib_version")
        implementation("io.izzel.taboolib:expansion-command-helper:$taboolib_version")
        implementation("io.izzel.taboolib:expansion-player-database:$taboolib_version")

        implementation("io.izzel.taboolib:module-lang:$taboolib_version")
        implementation("io.izzel.taboolib:module-nms:$taboolib_version")
        implementation("io.izzel.taboolib:module-nms-util:$taboolib_version")
        implementation("io.izzel.taboolib:module-database:$taboolib_version")
        implementation("io.izzel.taboolib:module-kether:$taboolib_version")

        compileOnly("com.google.code.gson:gson:2.10")
        compileOnly("com.mojang:brigadier:1.0.18")

        implementation("ink.ptms:um:1.0.0-beta-34")

    }


    // =============================
    //       下面的东西不用动
    // =============================
    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
        }
    }
    // 这里不要乱改
    tasks.withType<ShadowJar> {
        // 重定向 TabooLib
        relocate("taboolib", "${rootProject.group}.taboolib")
        // 重定向 Kotlin
        relocate("kotlin.", "kotlin${kotlinVersionNum}.") { exclude("kotlin.Metadata") }

        // =============================
        //     如果你要重定向就在下面加
        // =============================
        // relocate("org.spongepowered.math", "${rootProject.group}.library.math")
        relocate("ink.ptms.um","ink.work.taboopublicwork.lib.um")
    }
    kotlin {
        sourceSets.main {
            kotlin.srcDir("build/generated/ksp/main/kotlin")
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}
