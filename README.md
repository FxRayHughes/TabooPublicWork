# TabooPublicWork

### 禁忌基建

#### 项目介绍

这是一个基于TabooLib 开发的多模块的可拆卸的基础插件

用于代替Ess CMI 等基础插件

### 功能列表

| 功能名称      | 功能ID    | 是否完成 |
|-----------|---------|------|
| 地标功能      | warp    | ✔️   |
| 额外限制      | yesod   | ✔️   |
| 礼包功能      | kit     | ✔️   |
| 更好的刷怪笼    | spawner | ❌    |
| 传送功能      | tpa     | ✔️   |
| 货币兼容(可跨服) | eco     | ❌    |

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

#### 物品管理

本插件不携带物品库但是兼容别的插件的物品库

兼容列表与快捷名称
排名根据首字母大小写决定

| 物品源插件名          | 源名         | 优先级 |
|-----------------|------------|-----|
| EasyItem        | EasyItem   | 5   |
| ItemsAdder      | ItemsAdder | 1   |
| ItemSystem      | ItemSystem | 2   |
| Minecraft       | Minecraft  | 6   |
| MMOItems        | MMOItem    | 1   |
| MythicMobs      | MythicItem | 4   |
| NeigeItems      | NeigeItem  | 2   |
| OriginAttribute | OriginItem | 1   |
| SX-Item         | SxItem     | 0   |
| Zaphkiel        | Zaphkiel   | 0   |

注: 插件中使用的是 "源名"

关于优先级:
开发者根据插件ID索引的复杂度进行排序 优先级越小越先索引

```kts
compileOnly(project(":project:util-item-lib"))
```

统一入口 ``ItemLib``

特殊说明: toString方法

这是转为了一个标准格式:
> [物品源] 物品ID 物品数量

例如:

1. [SxItem] 测试物品 1
2. [MythicItem] MM的物品 1-2

数量支持 1~2 1-2 这种写法 如果是可以携带参数的比如

+ SxItem
+ OA
+ NI

可以在物品名后面加入参数
例如:

> [SxItem] 测试物品:参数1:参数2 1-2

#### ItemTagReader 物品NBT读写工具

此工具包含在core模块下 无需额外引用

基本用法:

```kotlin
// 假装你有一个物品
val itemStack = ItemStack()

// 可以使用了
itemStack.itemTagReader {

}

```

然后你就可以像操作 Config 一样 操作物品的NBT

要注意的是 Set后 只是修改了这个ItemTag 并没有同步回物品

```kotlin
itemStack.itemTagReader {
    set("test", "test")
}
```

> 不过值得一提的是 虽然不会同步回物品 但是你如果在set后面继续 get的话 是可以get到set过的内容


如果想设置回物品你可以使用 这会设置回物品

但是如果后续reader里进行修改 还是不会同步回去

```kotlin
itemStack.itemTagReader {
    write(itemStack) // saveToItem(itemStack)
}
```
