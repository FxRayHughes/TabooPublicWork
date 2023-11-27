package ink.work.taboopublicwork.utils.playerdata

import ink.work.taboopublicwork.TabooPublicWork
import org.bukkit.OfflinePlayer
import taboolib.expansion.*

class PlayerDataSql : IPlayerData {

    init {
        val config = TabooPublicWork.config
        val table = config.getString("database.player_data_table", "player_data_table")!!
        setupPlayerDatabase(config.getConfigurationSection("database")!!, table)
    }

    override fun get(player: OfflinePlayer, key: String): String? {
        return player.uniqueId.getPlayerDataContainer()[key]
    }

    override fun set(player: OfflinePlayer, key: String, value: String?) {
        if (value == null) {
            player.uniqueId.getPlayerDataContainer().remove(key)
            return
        }
        player.uniqueId.getPlayerDataContainer()[key] = value
    }

    override fun keys(player: OfflinePlayer): Set<String> {
        return player.uniqueId.getPlayerDataContainer().keys()
    }

    override fun remove(player: OfflinePlayer, key: String) {
        player.uniqueId.getPlayerDataContainer().remove(key)
    }

    override fun load(player: OfflinePlayer) {
        player.uniqueId.setupPlayerDataContainer()
    }

    override fun save(player: OfflinePlayer) {
        player.uniqueId.releasePlayerDataContainer()
    }

    private fun DataContainer.remove(key: String){
        val database1 = this.database
        database1.type.tableVar().delete(database1.dataSource){
            where("user" eq user and ("key" eq key))
        }
    }
}
