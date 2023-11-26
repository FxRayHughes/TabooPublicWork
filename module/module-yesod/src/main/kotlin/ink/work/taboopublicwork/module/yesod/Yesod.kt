package ink.work.taboopublicwork.module.yesod

import ink.work.taboopublicwork.api.IModule
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.generator.ChunkGenerator
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.createLocal
import taboolib.platform.BukkitWorldGenerator

object Yesod : BukkitWorldGenerator, IModule {

    override val name = "限制服务"
    override val id = "yesod"
    override val author = "坏黑"
    override val description = "基础功能修正由坏黑编写的Yesod移植&e https://github.com/Bkm016/Yesod".colored()
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    val data by lazy {
        createLocal(getSubFilePath() + "/data.yml")
    }

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Yesod 已启用")
            loadValues()
        }
        reloadModule {
            loadValues()
        }
    }

    var voidProtect = false
    var allowCraft = false
    var allowCraftDisplay = false
    lateinit var blockInventory: List<String>
    lateinit var blockInteract: List<String>
    var thornOverride = false
    lateinit var blockFeatures: List<String>
    lateinit var blockTeleport: List<String>

    fun loadValues() {
        config.reload()
        voidProtect = config.getBoolean("void-protect")
        allowCraft = config.getBoolean("allow-craft")
        allowCraftDisplay = config.getBoolean("allow-craft-display")
        blockInventory = config.getStringList("block-inventory")
        blockInteract = config.getStringList("block-interact")
        thornOverride = config.getBoolean("thorn-override")
        blockFeatures = config.getStringList("block-features")
        blockTeleport = config.getStringList("block-teleport")
    }

    fun Entity.bypass(hard: Boolean = false): Boolean {
        return this !is Player || isOp && gameMode == GameMode.CREATIVE && (!hard || isSneaking)
    }

    override fun getDefaultWorldGenerator(worldName: String, name: String?): ChunkGenerator {
        return YesodGenerator()
    }
}
