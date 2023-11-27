package ink.work.taboopublicwork.module.kits

import ink.work.taboopublicwork.module.kits.ModuleKits.sendLang
import ink.work.taboopublicwork.utils.evalKether
import ink.work.taboopublicwork.utils.evalKetherBoolean
import ink.work.taboopublicwork.utils.itemlib.ItemLib
import org.bukkit.entity.Player

data class KitData(
    val id: String,
    val delay: Long,
    val items: List<String>,
    val script: String,
    val limit: String
) {

    fun give(player: Player) {
        if (!ModuleKits.isEnable()) {
            return
        }
        // 首先对比限制时间
        if (delay != -1L) {
            KitPlayerData.get(player, id)?.let {
                if (it.toLong() > System.currentTimeMillis()) {
                    // 还剩多少秒
                    val left = (it.toLong() - System.currentTimeMillis()) / 1000
                    player.sendLang("module-kits-delay", left, { id })
                    return
                }
            }
        }
        // 对比limit
        if (limit.isNotEmpty()) {
            if (limit.evalKetherBoolean(player)) {
                return
            }
        }

        // 给予物品
        items.forEach {
            ItemLib.giveString(player, it)
        }

        // 运行脚本
        script.evalKether(player)
    }

}
