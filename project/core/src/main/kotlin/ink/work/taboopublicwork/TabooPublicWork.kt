package ink.work.taboopublicwork

import ink.work.taboopublicwork.api.IModule
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * 这是你插件对外开放的主类
 * 可以开放一些接口
 *
 */
object TabooPublicWork {

    @Config
    lateinit var config: Configuration

    val bukkitPlugin by lazy {
        BukkitPlugin.getInstance()
    }

    val modules = ConcurrentHashMap<String, IModule>()

    val modulesReloadAction = ConcurrentHashMap<String, IModule.() -> Unit>()
    val modulesInitAction = ConcurrentHashMap<String, IModule.() -> Unit>()

    val modulesEnable = ConcurrentHashMap<String, Boolean>()

    fun reload() {
        modulesReloadAction.forEach { (t, u) ->
            val iModule = modules[t]
            if (iModule != null) {
                u.invoke(iModule)
            }
        }
    }

    fun reloadModule(id: String) {
        val iModule = modules[id]
        if (iModule != null) {
            modulesReloadAction[id]?.invoke(iModule)
        }
    }


}
