package ink.work.taboopublicwork.utils.playerdata

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.Isolated
import taboolib.common.LifeCycle
import taboolib.common.io.newFile
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submit
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type
import java.io.File
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

fun getPlayerLocal(player: OfflinePlayer, type: Type = Type.YAML): Configuration {
    if (PlayerConfig.fileMap.containsKey(player.uniqueId)) {
        return PlayerConfig.fileMap[player.uniqueId]!!
    }
    return PlayerConfig.fileMap.computeIfAbsent(player.uniqueId) {
        Configuration.loadFromFile(
            newFile(getDataFolder(), "${PlayerDatabase.getSubFilePath()}/${player.uniqueId}.yml", create = true),
            type
        )
    }
}

object PlayerConfig {

    val fileMap = ConcurrentHashMap<UUID, Configuration>()

    @Schedule(period = 1200, async = true)
    fun update(){
        saveAll()
    }

    @Awake(LifeCycle.DISABLE)
    fun saveAll() {
        fileMap.forEach { it.value.saveToFile(File(getDataFolder(), "${PlayerDatabase.getSubFilePath()}/${it.key}.yml")) }
    }
}
