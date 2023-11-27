package ink.work.taboopublicwork.utils.itemlib

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.info
import taboolib.platform.util.countItem
import taboolib.platform.util.giveItem
import taboolib.platform.util.takeItem

interface SourceItem {

    /**
     * 物品来源的索引ID
     */
    val source: String

    /**
     *  优先级越高越先判断
     *
     *  0. 自己可以通过NBT标记获取的 （SXItem）
     *  1. 通过物品ID获取 (ItemAdder)
     *  4. 通过物品显示名获取 (MM)
     *  5. 必须得通过物品列表对比获取 (EasyItem)
     */
    val priority: Int

    /**
     * 获取物品
     * @param player 玩家
     * @param id 物品ID
     * @param amount 物品数量
     * @return 物品
     */
    fun getItem(player: Player, id: String): ItemStack?

    /**
     * 给予玩家物品
     * @param player 玩家
     * @param id 物品ID
     * @param amount 物品数量
     * @return 是否成功
     */
    fun giveItem(player: Player, id: String, amount: Int): Boolean {
        return getItem(player, id)?.let {
            player.giveItem(it, amount)
            true
        } ?: false
    }

    /**
     * 是否有物品
     * @param player 玩家
     * @param id 物品ID
     * @param amount 物品数量
     * @return 是否有物品
     */
    fun isMeet(player: Player, id: String, amount: Int): Boolean {
        return getNumber(player, id) >= amount
    }

    /**
     * 获取物品数量
     * @param player 玩家
     * @param id 物品ID
     * @param amount 物品数量
     * @return 是否有物品
     */
    fun getNumber(player: Player, id: String): Int {
        return player.inventory.countItem { getItemId(it) == id }
    }

    /**
     * 扣除物品
     * @param player 玩家
     * @param id 物品ID
     * @param amount 物品数量
     * @return 是否成功
     */
    fun takeItem(player: Player, id: String, amount: Int): Boolean {
        if (isMeet(player, id, amount)) {
            player.inventory.takeItem(amount) {
                getItemId(it) == id
            }
        }
        return false
    }

    /**
     * 获取物品的ID
     * @param itemStack 物品
     * @return 物品ID
     * @throws Exception 物品不是该来源的物品
     */
    fun getItemId(itemStack: ItemStack): String?

    fun registerItem(pluginName: String = "NONE") {
        if (pluginName == "NONE") {
            ItemLib.items[source] = this
            info("注册物品来源 $source")
            return
        }
        if (Bukkit.getPluginManager().getPlugin(pluginName) != null) {
            runCatching { ItemLib.items[source] = this }.getOrNull() ?: error("§c物品来源 $source Hook错误")
        }

    }
}
