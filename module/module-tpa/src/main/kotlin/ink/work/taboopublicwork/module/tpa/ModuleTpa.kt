package ink.work.taboopublicwork.module.tpa

import ink.work.taboopublicwork.api.IModule
import ink.work.taboopublicwork.module.tpa.TpaPlayerData.askTime
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration

object ModuleTpa : IModule {
    override val name: String = "传送"
    override val id: String = "tpa"
    override val author: String = "嘿鹰"
    override val description = "传送模块"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Tpa 已启用")
            load()
        }

        reloadModule {
            info("Module - Tpa 已重载")
            load()
        }
    }

    fun load() {
        askTime = config.getLong("tpa-timeout", 30)
    }
}