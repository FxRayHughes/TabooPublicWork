package ink.work.taboopublicwork.module.kits

import ink.work.taboopublicwork.api.IModule
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration


object ModuleKits : IModule {

    // 标准 IModule 接口实现
    override val name = "礼包"
    override val id = "kits"
    override val author = "枫溪"
    override val description = "礼包模块"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Kits 已启用")
        }

        reloadModule {
        }
    }
}
