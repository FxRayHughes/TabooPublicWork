package ink.work.taboopublicwork.module.kits

import ink.work.taboopublicwork.api.IModule
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap


object ModuleKits : IModule {

    // 标准 IModule 接口实现
    override val name = "礼包"
    override val id = "kits"
    override val author = "枫溪"
    override val description = "礼包模块"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    val kits = ConcurrentHashMap<String, KitData>()

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Kits 已启用")
            KitPlayerData.module = this
            loadKits()
        }

        reloadModule {
            loadKits()
        }
    }

    fun loadKits() {
        kits.clear()
        val configurationSection = config.getConfigurationSection("kits")
        configurationSection?.getKeys(false)?.forEach {
            val section = configurationSection.getConfigurationSection(it) ?: return@forEach
            val name = it
            val items = section.getStringList("items")
            val script = section.getString("script", "")
            val delay = section.getLong("delay", -1)
            val limit = section.getString("limit", "")
            val kitData = KitData(name, delay, items, script!!, limit!!)
            kits[name] = kitData
        }
    }
}
