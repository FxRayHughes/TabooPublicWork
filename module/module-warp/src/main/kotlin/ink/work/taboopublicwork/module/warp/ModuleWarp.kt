package ink.work.taboopublicwork.module.warp

import ink.work.taboopublicwork.api.IModule
import ink.work.taboopublicwork.module.warp.database.IWarpDatabase
import ink.work.taboopublicwork.module.warp.database.WarpDatabaseSql
import ink.work.taboopublicwork.module.warp.database.WarpDatabaseYaml
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration


object ModuleWarp : IModule {

    override val name = "地标"

    override val id = "warp"

    override val author = "枫溪"

    override lateinit var config: Configuration

    override lateinit var langFile: Configuration

    lateinit var database: IWarpDatabase

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Warp 已启用")
            val databaseType = config.getString("database.type", "yaml")!!
            database = when (databaseType) {
                "yaml" -> WarpDatabaseYaml()
                "sql" -> WarpDatabaseSql()
                else -> WarpDatabaseYaml()
            }
            database.load()
            info("Module - Warp 数据库类型: $databaseType")
        }

        reloadModule {
            database.load()
        }
    }

}
