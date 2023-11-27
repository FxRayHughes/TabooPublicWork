package ink.work.taboopublicwork.utils.itemlib.impl

import ink.work.taboopublicwork.utils.getString
import ink.work.taboopublicwork.utils.itemlib.SourceItem
import net.Indyuce.mmoitems.MMOItems
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SMMOItem : SourceItem {

    override val source: String = "MMOItem"
    override val priority: Int = 1

    @Awake(LifeCycle.ENABLE)
    fun init() {
        registerItem("MMOItems")
    }

    override fun getItem(player: Player, id: String): ItemStack? {
        val sub = id.split("::")
        val type = sub.getOrNull(0) ?: return null
        val item = sub.getOrNull(1) ?: return null
        val mmiType = MMOItems.plugin.types.get(type) ?: return null
        val mmoitem = MMOItems.plugin.getItem(mmiType, item)
        return mmoitem?.apply {
            this.amount = amount
        }
    }

    override fun getItemId(itemStack: ItemStack): String? {
        return if (itemStack.getString("MMOITEMS_ITEM_ID") == "null") {
            null
        } else {
            "${itemStack.getString("MMOITEMS_ITEM_TYPE")}::${itemStack.getString("MMOITEMS_ITEM_ID")}"
        }
    }
}
