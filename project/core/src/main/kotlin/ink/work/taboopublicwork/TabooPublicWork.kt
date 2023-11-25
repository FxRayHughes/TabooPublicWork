package ink.work.taboopublicwork

import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin

/**
 * 这是你插件对外开放的主类
 * 可以开放一些接口
 *
 */
object TabooPublicWork {

    @Config
    lateinit var conf: Configuration
        private set

    val bukkitPlugin by lazy {
        BukkitPlugin.getInstance()
    }
}
