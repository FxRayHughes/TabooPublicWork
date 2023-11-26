# TabooPublicWork

### 禁忌基建

#### 项目介绍

这是一个基于TabooLib 开发的多模块的可拆卸的基础插件

用于代替Ess CMI 等基础插件

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

