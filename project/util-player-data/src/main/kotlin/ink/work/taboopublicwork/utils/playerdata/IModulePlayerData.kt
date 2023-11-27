package ink.work.taboopublicwork.utils.playerdata

import ink.work.taboopublicwork.api.IModule
import org.bukkit.OfflinePlayer

/**
 *  规范化各个模块调用玩家数据
 *  减少对Key的污染
 */
interface IModulePlayerData {

    var module: IModule

    fun get(player: OfflinePlayer, key: String): String? {
        return PlayerDatabase.database.get(player, "${module.id}-$key".replace(".", "-"))
    }

    fun set(player: OfflinePlayer, key: String, value: String?) {
        PlayerDatabase.database.set(player, "${module.id}-$key".replace(".", "-"), value)
    }

    fun keys(player: OfflinePlayer): Set<String> {
        return PlayerDatabase.database.keys(player).filter { it.startsWith("${module.id}-") }.map { it.replace("${module.id}-", "") }.toSet()
    }

    fun remove(player: OfflinePlayer, key: String) {
        PlayerDatabase.database.remove(player, "${module.id}-$key".replace(".", "-"))
    }

    fun load(player: OfflinePlayer) {
        PlayerDatabase.database.load(player)
    }

    fun save(player: OfflinePlayer) {
        PlayerDatabase.database.save(player)
    }

}
