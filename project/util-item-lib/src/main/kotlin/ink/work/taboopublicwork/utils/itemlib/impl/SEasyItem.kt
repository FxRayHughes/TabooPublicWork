package ink.work.taboopublicwork.utils.itemlib.impl

import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.easyitem.manager.ItemManager
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SEasyItem : SourceItem {

    override val source: String = "EasyItem"

    override val priority: Int = 5

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("EasyItem")
    }

    override fun getItem(player: Player, id: String): ItemStack? {
        return ItemManager.getItemStack(id)
    }

    override fun getItemId(itemStack: ItemStack): String? {
        return ItemManager.items.values.firstOrNull {
            it.getItemStack()?.isSimilar(itemStack) ?: false
        }?.id
    }
}
