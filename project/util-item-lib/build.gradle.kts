dependencies {
    // 如果你不需要跨平台，你可以在这里引入 Bukkit 核心
     compileOnly("ink.ptms.core:v12002:12002:universal")
     compileOnly("ink.ptms.core:v12002:12002:mapped")

    compileOnly(project(":project:core"))
    compileOnly(fileTree("libs"))

    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.2-beta-r3-b")
    compileOnly("ink.ptms:Zaphkiel:2.0.14")
}
