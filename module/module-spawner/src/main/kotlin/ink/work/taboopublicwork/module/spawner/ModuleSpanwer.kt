package ink.work.taboopublicwork.module.spawner

import ink.work.taboopublicwork.api.IModule
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration


object ModuleSpanwer : IModule {

    // 标准 IModule 接口实现
    override val name = "地标"
    override val id = "spawner"
    override val author = "枫溪"
    override val description = "地标模块"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration


    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Warp 已启用")

        }

        reloadModule {
        }
    }
}
