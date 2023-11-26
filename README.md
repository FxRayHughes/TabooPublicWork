# TabooPublicWork

### 禁忌基建

#### 项目介绍

这是一个基于TabooLib 开发的多模块的可拆卸的基础插件

用于代替Ess CMI 等基础插件

### 功能列表

| 功能名称          | 功能ID    | 是否完成 |
|---------------|---------|------|
| 地标功能          | warp    | ✔️   |
| 额外限制          | yesod   | ✔️   |
| 礼包功能          | kit     | ❌    |
| 更好的刷怪笼(原版和MM) | spawner | ❌    |
| 传送功能          | tpa     | ❌    |
| 货币兼容(可跨服)     | eco     | ❌    |

### 创建模块

您可以使用fork 或者是 附属插件的形式进行拓展

#### 第一步 创建一个模块主类

需要继承 IModule

替换下方代码字符串部分

```kotlin

object ModuleWarp : IModule {

    // 标准 IModule 接口实现
    override val name = "地标"
    override val id = "warp"
    override val author = "枫溪"
    override val description = "地标模块"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Warp 已启用")
            // 模块加载时执行
            // TODO
        }

        reloadModule {
            // 模块重载时执行
            // TODO
        }
    }

}


```

需要统一模块的初始化和重载方法

初始化说明

首先会初始化出配置文件 然后判断内部的config (setting.enable = true)

然后会对比服务器版本 是否符合需求

```kotlin

/**
 *  模块支持的最低版本
 *
 *  使用 MinecraftVersion
 *
 *  默认最低1.12.2 最高不限制
 */
fun versionMin(): Int = MinecraftVersion.V1_12
fun versionMax(): Int = 99

```

然后才会进行释放语言文件 和 执行初始化代码

#### 第二步 创建一个模块配置类

框架已经为您创建好配置管理 只需要在resources内创建配置文件yml即可

路径位置为: resources/modules/模块id/config.yml

当然你也可以自定义这个配置文件的路径 需要重写 IModule 的 configPath 字段

```kotlin

override fun getFilePath(): File {
    return super.getFilePath()
}

override fun getSubFilePath(): String {
    return super.getSubFilePath()
}

```

#### 第三步 实现基础逻辑

接下来就是自己实现基础逻辑了

### 一些工具

#### 玩家持久化数据管理

首先导入模块
```kts
compileOnly(project(":project:util-player-data"))
```

用法如下

```kotlin
fun OfflinePlayer.getDataString(key: String): String? {
    return PlayerDatabase.database.get(this, key)
}

fun OfflinePlayer.getDataString(key: String, default: String): String {
    return PlayerDatabase.database.get(this, key) ?: default
}

fun OfflinePlayer.setDataString(key: String, value: String?) {
    PlayerDatabase.database.set(this, key, value)
}

fun OfflinePlayer.getDataKeys(): Set<String> {
    return PlayerDatabase.database.keys(this)
}

fun OfflinePlayer.removeDataString(key: String) {
    PlayerDatabase.database.remove(this, key)
}

fun OfflinePlayer.loadData() {
    PlayerDatabase.database.load(this)
}

fun OfflinePlayer.saveData() {
    PlayerDatabase.database.save(this)
}

fun OfflinePlayer.hasDataString(key: String): Boolean {
    return PlayerDatabase.database.get(this, key) != null
}

```
