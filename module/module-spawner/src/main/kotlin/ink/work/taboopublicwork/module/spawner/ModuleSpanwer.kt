package ink.work.taboopublicwork.module.spawner

import ink.work.taboopublicwork.api.IModule
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration


object ModuleSpanwer : IModule {

    // 标准 IModule 接口实现
    override val name = "刷怪笼"
    override val id = "spawner"
    override val author = "枫溪 & 坏黑"
    override val description = "自定义刷怪笼 刷MM 或 MC的实体 部分代码来自 https://github.com/Bkm016/Sandalphon"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration


    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Spawner 已启用")

        }

        reloadModule {
        }
    }
}
