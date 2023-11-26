package ink.work.taboopublicwork.utils.playerdata

import org.bukkit.OfflinePlayer

interface IPlayerData {

    fun get(player: OfflinePlayer, key: String): String?

    fun set(player: OfflinePlayer, key: String, value: String?)

    fun keys(player: OfflinePlayer): Set<String>

    fun remove(player: OfflinePlayer, key: String)

    fun load(player: OfflinePlayer)

    fun save(player: OfflinePlayer)

}
