package ink.work.taboopublicwork.module.home

import ink.work.taboopublicwork.api.IModule
import ink.work.taboopublicwork.module.home.internal.command.HomeCommand
import ink.work.taboopublicwork.utils.fromSaveLocation
import ink.work.taboopublicwork.utils.getPermissionNumber
import ink.work.taboopublicwork.utils.playerdata.IModulePlayerData
import ink.work.taboopublicwork.utils.saveToString
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration

object ModuleHome : IModule, IModulePlayerData {
    override val name: String = "家"
    override val id: String = "Home"
    override val author: String = "冰子 & 枫溪"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    override lateinit var module: IModule

    var delayTp = 60L

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Home 已启用")
            module = this@ModuleHome
            loadValues()
            HomeCommand.registerCommand()
        }

        reloadModule {
            info("Module - Home 已重载")
            loadValues()
        }
    }

    fun loadValues() {
        delayTp = config.getLong("delay_tp", 60L)
    }

    fun setHome(player: Player, name: String) {
        val old = get(player, "home.$name")
        if (old != null) {
            player.sendLang("module-home-sethome-exist", name)
        }

        val max = player.getPermissionNumber("taboopublicwork.home.set.max", config.getInt("max_home", 3))
        if (max != -1 && homeList(player).size >= max) {
            player.sendLang("module-home-sethome-max", max)
            return
        }
        set(player, "home.$name", player.location.saveToString())
        player.sendLang("module-home-sethome-success", name)
    }

    fun deleteHome(player: Player, name: String) {
        val old = get(player, "home.$name")
        if (old == null) {
            player.sendLang("module-home-deletehome-notexist", name)
            return
        }
        remove(player, "home.$name")
        player.sendLang("module-home-deletehome-success", name)
    }

    fun getHome(player: Player, name: String): Pair<String, Location>? {
        val get = get(player, "home.$name") ?: return null
        return name to get.fromSaveLocation()
    }

    fun homeList(player: Player): List<String> {
        return getList(player)
    }

}
