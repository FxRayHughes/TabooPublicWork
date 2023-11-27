package ink.work.taboopublicwork.module.home

import ink.work.taboopublicwork.api.IModule
import ink.work.taboopublicwork.module.home.internal.command.OtherCommand
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration

object ModuleHome : IModule {
    override val name: String = "家"
    override val id: String = "Home"
    override val author: String = "冰子"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Home 已启用")
            OtherCommand.registerCommand()
        }

        reloadModule {
            info("Module - Home 已重载")
        }
    }
}
