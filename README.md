# taboolib-multi-module-sdk
多模块 `TabooLib` 项目模板  

## 准备工作

项目结构如下所示

    MyHelloWorld
    ├── plugin                     -- 插件打包模块，用于将子模块合并打包
    │   └── build.gradle.kts
    ├── project                    -- 项目目录
    │   ├── core                   -- 核心模块，公开出去的代码
    │   │   └── build.gradle.kts
    │   └── runtime-bukkit         -- Bukkit 平台启动类，不要把你的业务逻辑写到这里面
    │       └── build.gradle.kts
    ├── build.gradle.kts           -- 全局构建文件
    ├── gradle.properties          -- 全局配置
    ├── settings.gradle.kts        -- 全局配置
    ...

## 配置插件

首先打开 `gradle.properties` 文件，编辑插件的基本信息和依赖文件版本：

```properties
# 这个改成你的
group=me.skymc.helloworld
version=1.0.0
# 这个看情况改
taboolib_version=6.0.10-86
```

## 安装 TabooLib 模块

你可以在 `build.gradle.kts` 文件中你可以添加或删除 **TabooLib** 模块，这和一般的项目可能不太一样。

```kotlin
dependencies {
    ...
    // 引入 Taboolib
    compileOnly("io.izzel.taboolib:common:$taboolib_version")
    implementation("io.izzel.taboolib:common-5:$taboolib_version")
    implementation("io.izzel.taboolib:module-chat:$taboolib_version")
    implementation("io.izzel.taboolib:module-configuration:$taboolib_version")
    implementation("io.izzel.taboolib:platform-bukkit:$taboolib_version")
    implementation("io.izzel.taboolib:expansion-command-helper:$taboolib_version")
    // 如果你要加新的 TabooLib 模块，就写到下面
    // implementation("io.izzel.taboolib:module-lang:$taboolib_version")
}
```

注意！只有需要打包到插件里的依赖才可以使用 `implementation`。

## 添加新的模块

多模块是 Gradle 的基本操作，不用我教的吧。别忘记在 `plugin/build.gradle.kts` 文件里添加新的模块。

```kotlin
dependencies {
    // 打包子项目
    implementation(project(":project:core"))
    implementation(project(":project:runtime-bukkit"))
    // 添加新的模块就写到下面
    // implementation(project(":project:module-abab"))
}
```

## 总结

插件该怎么写就在 `core` 里怎么写，`runtime-bukkit` 基本不需要改。
