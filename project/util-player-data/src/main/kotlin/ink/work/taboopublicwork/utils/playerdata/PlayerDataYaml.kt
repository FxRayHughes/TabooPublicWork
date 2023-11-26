package ink.work.taboopublicwork.utils.playerdata

import org.bukkit.OfflinePlayer

class PlayerDataYaml:IPlayerData {
    override fun get(player: OfflinePlayer, key: String): String? {
        return getPlayerLocal(player).getString(key)
    }

    override fun set(player: OfflinePlayer, key: String, value: String?) {
        getPlayerLocal(player)[key] = value
    }

    override fun keys(player: OfflinePlayer): Set<String> {
        return getPlayerLocal(player).getKeys(false)
    }

    override fun remove(player: OfflinePlayer, key: String) {
        getPlayerLocal(player)[key] = null
    }

    override fun load(player: OfflinePlayer) {
        getPlayerLocal(player)
    }

    override fun save(player: OfflinePlayer) {
        PlayerConfig.fileMap[player.uniqueId]?.saveToFile()
        PlayerConfig.fileMap.remove(player.uniqueId)
    }


}
