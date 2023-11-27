package ink.work.taboopublicwork.module.tpa

import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap


object TpaPlayerData {
    var askTime: Long = 30

    val askTimeout = ConcurrentHashMap<Player, TeleportTimeout>()
    val getAsked = ConcurrentHashMap<Player, ArrayList<String>>()

    class TeleportTimeout(val player: Player) {
        val timeout = ConcurrentHashMap<String, Long>()
    }

    fun Player.isTimeout(target: String) : Boolean {
        val playerData = askTimeout.computeIfAbsent(this) { TeleportTimeout(this) }
        return (playerData.timeout[target] ?: 0) < System.currentTimeMillis()
    }
}