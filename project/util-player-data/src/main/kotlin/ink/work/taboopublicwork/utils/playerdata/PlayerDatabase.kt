package ink.work.taboopublicwork.utils.playerdata

import ink.work.taboopublicwork.TabooPublicWork
import ink.work.taboopublicwork.api.IModule
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.configuration.Configuration

object PlayerDatabase : IModule {

    // 标准 IModule 接口实现
    override val name = "玩家数据存储"
    override val id = "playerdata"
    override val author = "枫溪"
    override val description = "用于存储本插件的玩家数据, 例如: 礼包等."
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    lateinit var database: IPlayerData

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            val string = TabooPublicWork.config.getString("playerdata.type", "YAML")
            database = when (string) {
                "YAML", "yaml" -> PlayerDataYaml()
                "SQL", "sql" -> PlayerDataSql()
                else -> PlayerDataYaml()
            }
        }
    }

    @SubscribeEvent
    fun onLeave(event: PlayerQuitEvent) {
        database.save(event.player)
    }

    @SubscribeEvent
    fun onLogin(event: PlayerLoginEvent) {
        database.load(event.player)
    }

    override fun isEnable(): Boolean {
        return true
    }

    override fun initConfig() {
        // 不生成配置文件
        // 一定会启用本模块
    }

    override fun mergeLanguageFile() {
        // 不生成语言文件
    }

}
