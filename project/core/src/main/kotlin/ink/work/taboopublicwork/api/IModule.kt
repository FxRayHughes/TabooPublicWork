package ink.work.taboopublicwork.api

import ink.work.taboopublicwork.TabooPublicWork
import taboolib.common.io.newFile
import taboolib.common.io.newFolder
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.nms.MinecraftVersion
import java.io.File

interface IModule {

    /**
     *  模块名称 （显示名 中文）
     */
    val name: String

    /**
     *  模块ID 英文
     *  用于配置文件路径创建等
     */
    val id: String

    /**
     *  模块支持的最低版本
     *
     *  使用 MinecraftVersion
     *
     *  默认最低1.12.2 最高不限制
     */
    fun versionMin(): Int = MinecraftVersion.V1_12
    fun versionMax(): Int = 99

    var config: Configuration

    /**
     *  加载配置文件
     *
     *  默认的模块配置文件路径
     *  "moules/${id}/config.yml"
     *  会从jar中释放 未找到会创建一个新的
     */
    fun initConfig() {
        val path = "moules/${id}/config.yml"
        val resource = TabooPublicWork.bukkitPlugin.getResource(path)

        if (resource == null) {
            val first = File(getDataFolder(), path).exists()
            config = Configuration.loadFromFile(newFile(getDataFolder(), path, create = true), Type.YAML)
            if (!first) {
                config["setting.enable"] = false
                config.saveToFile()
            }
            return
        }
        config = Configuration.loadFromFile(releaseResourceFile("moules/${id}/config.yml"), Type.YAML)
    }

    /**
     *  获取模块目录
     */
    fun getFilePath(): File {
        return newFolder("moules/${id}/")
    }

    /**
     *  模块是否启用
     *
     *  规范: setting.enable
     */
    fun isEnable(): Boolean {
        return MinecraftVersion.isIn(versionMin(), versionMax()) && config.getBoolean("setting.enable")
    }

    /**
     *  初始化模块
     *
     *  里面的参数是模块启用时触发的回调
     */
    fun initModule(action: IModule.() -> Unit = {}) {
        initConfig()
        if (isEnable()) {
            action.invoke(this)
        }
    }


}
