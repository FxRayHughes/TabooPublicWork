package ink.work.taboopublicwork.utils.itemlib.impl

import dev.lone.itemsadder.api.CustomStack
import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SItemsAdderItem : SourceItem {

    override val source: String = "ItemsAdder"

    override val priority: Int = 1

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("ItemsAdder")
    }

    override fun getItem(player: Player, id: String): ItemStack? {
        CustomStack.getInstance(id)?.let {
            return it.itemStack
        }
        return null
    }

    override fun getItemId(itemStack: ItemStack): String? {
        return CustomStack.byItemStack(itemStack)?.namespacedID
    }
}
