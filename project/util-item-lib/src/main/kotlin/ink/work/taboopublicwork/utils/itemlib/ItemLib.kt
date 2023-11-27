package ink.work.taboopublicwork.utils.itemlib

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

object ItemLib {

    val items = ConcurrentHashMap<String, SourceItem>()

    fun toString(item: ItemStack): String {
        // 根据 source给 items排序
        items.values.sortedBy { it.priority }.forEach {
            it.getItemId(item)?.let { id ->
                return "[${it.source}] $id ${item.amount}"
            }
        }
        error("未知物品: ${item.type.name}")
    }

    fun fromString(player: Player, string: String): ItemStack? {
        val split = string.split(" ")
        if (split.size != 3) {
            return null
        }
        val source = split[0].substring(1, split[0].length - 1)
        val id = split[1]
        val amount = split[2].randomInt()
        return getItem(player, source, id)?.apply {
            this.amount = amount
        }
    }

    fun giveString(player: Player, string: String): Boolean {
        val split = string.split(" ")
        if (split.size != 3) {
            return false
        }
        val source = split[0].substring(1, split[0].length - 1)
        val id = split[1]
        val amount = split[2].randomInt()
        return giveItem(player, source, id, amount)
    }

    // 1-2 或 1~2
    fun String.randomInt(): Int {
        val split = this.split("-", "~")
        if (split.size != 2) {
            return this.toIntOrNull() ?: 0
        }
        val min = split[0].toIntOrNull() ?: return 0
        val max = split[1].toIntOrNull() ?: return 0
        return (min..max).random()
    }

    fun getItem(player: Player, source: String, id: String): ItemStack? {
        return items[source]?.getItem(player, id)
    }

    fun getItemSource(player: Player, id: String): String? {
        // 根据 source给 items排序
        items.values.sortedBy { it.priority }.forEach {
            it.getItem(player, id)?.let { item ->
                return it.source
            }
        }
        return null
    }

    fun giveItem(player: Player, source: String, id: String, amount: Int): Boolean {
        return items[source]?.giveItem(player, id, amount) ?: false
    }

    fun giveItem(player: Player, id: String, amount: Int): Boolean {
        return getItemSource(player, id)?.let {
            giveItem(player, it, id, amount)
        } ?: false
    }

    fun takeItem(player: Player, source: String, id: String, amount: Int): Boolean {
        return items[source]?.takeItem(player, id, amount) ?: false
    }

    fun takeItem(player: Player, id: String, amount: Int): Boolean {
        return getItemSource(player, id)?.let {
            takeItem(player, it, id, amount)
        } ?: false
    }

    fun getNumber(player: Player, source: String, id: String): Int {
        return items[source]?.getNumber(player, id) ?: 0
    }

    fun getNumber(player: Player, id: String): Int {
        return getItemSource(player, id)?.let {
            getNumber(player, it, id)
        } ?: 0
    }


}
