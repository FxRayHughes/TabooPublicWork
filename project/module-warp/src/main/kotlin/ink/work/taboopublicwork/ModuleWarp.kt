package ink.work.taboopublicwork

import ink.work.taboopublicwork.api.IModule
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration


object ModuleWarp : IModule {

    override val name: String = "地标"

    override val id: String = "warp"

    override lateinit var config: Configuration

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Warp 已启用")
        }
    }

}
