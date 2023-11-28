package ink.work.taboopublicwork.api

import ink.work.taboopublicwork.TabooPublicWork
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.io.newFile
import taboolib.common.io.newFolder
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import taboolib.common.platform.function.unregisterCommand
import taboolib.common.util.replaceWithOrder
import taboolib.module.chat.component
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.compat.replacePlaceholder
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
     *  模块开发者
     */
    val author: String

    /**
     *  模块描述
     */
    val description: String
        get() = ""

    /**
     *  模块支持的最低版本
     *
     *  使用 MinecraftVersion
     *
     *  默认最低1.12.2 最高不限制
     */
    fun versionMin(): Int = MinecraftVersion.V1_12
    fun versionMax(): Int = 99

    abstract var config: Configuration

    abstract var langFile: Configuration

    /**
     *  加载配置文件
     *
     *  默认的模块配置文件路径
     *  "moules/${id}/config.yml"
     *  会从jar中释放 未找到会创建一个新的
     */
    fun initConfig() {
        val path = "${getSubFilePath()}/config.yml"
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
        config = Configuration.loadFromFile(releaseResourceFile("module/${id}/config.yml"), Type.YAML)
    }

    /**
     *  获取模块目录
     */
    fun getFilePath(): File {
        return newFolder(TabooPublicWork.bukkitPlugin.dataFolder, getSubFilePath())
    }

    /**
     *  获取子文件目录
     */
    fun getSubFilePath(): String {
        return "module/${id}"
    }

    /**
     *  模块是否启用
     *
     *  规范: setting.enable
     */
    fun isEnable(): Boolean {
        return TabooPublicWork.modulesEnable[id] ?: false
    }

    /**
     *  检查是否启用
     */
    fun checkEnable() {
        TabooPublicWork.modulesEnable[id] = MinecraftVersion.isIn(versionMin(), versionMax()) && config.getBoolean("setting.enable")
    }

    /**
     *  初始化模块
     *
     *  里面的参数是模块启用时触发的回调
     */
    fun initModule(action: IModule.() -> Unit = {}) {
        initConfig()
        checkEnable()
        if (isEnable()) {
            // 释放语言文件
            mergeLanguageFile()
            action.invoke(this)
        } else {
            TabooPublicWork.modulesCommand[id]?.forEach {
                unregisterCommand(it.command)
            }
        }
        // 注册到全局
        TabooPublicWork.modules[id] = this
        TabooPublicWork.modulesInitAction[id] = action
    }

    /**
     *  重载模块标准入口
     *
     *  会协助重载配置文件与语言文件
     */
    fun reloadModule(action: IModule.() -> Unit = {}) {
        TabooPublicWork.modulesReloadAction[id] = {
            // 重载配置文件与语言文件
            initConfig()
            config.reload()
            mergeLanguageFile()
            langFile.reload()
            // 不允许热重载启动模块
//            val old = isEnable()
//            checkEnable()
//            if (isEnable() != old && isEnable()) {
//                // 重新启用
//                TabooPublicWork.modulesInitAction[id]?.invoke(this)
//            }

            // 运行action
            action.invoke(this)
        }
    }

    /**
     *  获取语言文件
     */
    fun getLanguage(): String {
        return config.getString("setting.language", "zh_CN")!!
    }

    /**
     *  释放语言文件
     */
    fun mergeLanguageFile() {
        val targetFile = File(getFilePath(), "lang/${getLanguage()}.yml")
        if (!targetFile.exists()) {
            // 判断一下Jar里面有没有对应的语言文件 没有就使用玩家写的文件
            val res = try {
                releaseResourceFile("${getSubFilePath()}/lang/${getLanguage()}.yml", false)
            } catch (e: Exception) {
                newFile(getFilePath(), "lang/${getLanguage()}.yml", create = true)
            }
            langFile = Configuration.loadFromFile(res)
            return
        }
        langFile = Configuration.loadFromFile(targetFile)
    }

    /**
     *  发送语言
     */
    fun sendLangW(player: CommandSender, key: String, vararg args: Any) {
        val message = langFile.getString(key, key)!!
        if (message.isEmpty()) {
            return
        }
        if (player is Player) {
            message.replacePlaceholder(player).replaceWithOrder(*args).component().buildColored().sendTo(adaptCommandSender(player))
            return
        }
        message.replaceWithOrder(*args).component().buildColored().sendTo(adaptCommandSender(player))
    }

    fun CommandSender.sendLang(key: String, vararg args: Any) {
        sendLangW(this, key, *args)
    }

}
