package ink.work.taboopublicwork.utils.itemlib.impl

import com.skillw.itemsystem.api.ItemAPI
import ink.work.taboopublicwork.utils.getString
import ink.work.taboopublicwork.utils.itemlib.SourceItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SItemSystemItem : SourceItem {

    override val source: String = "ItemSystem"
    override val priority: Int = 2

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("ItemSystem")
    }

    override fun getItem(player: Player, id: String): ItemStack? {
        return ItemAPI.productItem(id, player)?.apply {
            this.amount = amount
        }
    }

    override fun getItemId(itemStack: ItemStack): String? {
        return if (itemStack.getString("ITEM_SYSTEM.key") == "null") {
            null
        } else {
            itemStack.getString("ITEM_SYSTEM.key")
        }
    }
}
